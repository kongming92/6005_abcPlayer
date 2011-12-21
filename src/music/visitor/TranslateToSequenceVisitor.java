package music.visitor;

import java.util.HashMap;
import java.util.Map;

import sound.SequencePlayer;
import sound.Pitch;

import music.ast.Accidental;
import music.ast.Bar;
import music.ast.Chord;
import music.ast.Music;
import music.ast.MusicSequence;
import music.ast.MusicalElement;
import music.ast.MusicalLength;
import music.ast.Note;
import music.ast.Repeat;
import music.ast.Rest;
import music.ast.Tuplet;
import music.ast.Voice;

/**
 * Convert the Music Abstract Syntax Tree into a linearized stream of notes that
 * can be played by the MIDI interface.
 * 
 * This Visitor faithfully translates the AST to a linearized stream of notes to
 * the MIDI, ignoring any errors about well-formedness that may have occurred.
 * Bars that are too short or too long will be played; chords that have
 * different note lengths will be played using the length of the first note.
 * 
 */
public class TranslateToSequenceVisitor implements MusicVisitor<Void> {

    private Map<Pitch, Accidental> currentAccidentals;
    private Map<Pitch, Accidental> keyAccidentals;
    private MusicalLength defaultLength;
    private MusicalLength scale;
    private boolean advanceTime;
    private int timeElapsed;
    private int ticksPerQuarter;
    private SequencePlayer sp;

    public TranslateToSequenceVisitor(SequencePlayer player, int tpq) {
        timeElapsed = 0;
        sp = player;
        ticksPerQuarter = tpq;
        advanceTime = true;
        scale = new MusicalLength(1, 1);
    }

    private int getTime(MusicalElement me) {
        return scale.getTicks(defaultLength.getTicks(4 * me.getLength()
                .getTicks(ticksPerQuarter)));
    }

    private void initAccidentals() {
        currentAccidentals = new HashMap<Pitch, Accidental>();
        currentAccidentals.putAll(keyAccidentals);
    }

    @Override
    public Void visit(Accidental a) {
        currentAccidentals.put(a.getBasePitch(), a);
        return null;
    }

    @Override
    public Void visit(Bar b) {
        initAccidentals();
        for (MusicalElement me : b.getElements()) {
            me.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Chord c) {
        advanceTime = false;
        MusicalLength length = c.getNotes().get(0).getLength();
        for (Note n : c.getNotes()) {
            Note t = new Note(n.getPitch(), length);
            t.accept(this);
        }
        advanceTime = true;
        int time = getTime(c.getNotes().get(0));
        timeElapsed += time;
        return null;
    }

    @Override
    public Void visit(Music m) {
        keyAccidentals = m.getKeySignature().getDefaultAccidentals();
        initAccidentals();
        defaultLength = m.getDefaultLength();
        for (Voice v : m.getVoices().values()) {
            v.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Note n) {
        Pitch play = n.getPitch();
        if (currentAccidentals.containsKey(play)) {
            play = currentAccidentals.get(play).getModifiedPitch();
        }
        int time = getTime(n);
        sp.addNote(play.toMidiNote(), timeElapsed, time);
        if (advanceTime) {
            timeElapsed += time;
        }
        return null;
    }

    @Override
    public Void visit(Repeat r) {
        for (Bar b : r.getApparentElments()) {
            b.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Voice v) {
        timeElapsed = 0;
        for (MusicSequence ms : v.getElements()) {
            ms.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Rest rest) {
        timeElapsed += getTime(rest);
        return null;
    }

    @Override
    public Void visit(Tuplet tuplet) {
        scale = tuplet.getScale();
        for (MusicalElement me : tuplet.getMusicalElements()) {
            me.accept(this);
        }
        scale = new MusicalLength(1, 1);
        return null;
    }

}

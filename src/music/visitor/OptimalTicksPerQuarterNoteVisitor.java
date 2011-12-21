package music.visitor;

import music.ast.Accidental;
import music.ast.Bar;
import music.ast.Chord;
import music.ast.Music;
import music.ast.MusicSequence;
import music.ast.MusicalElement;
import music.ast.Note;
import music.ast.Repeat;
import music.ast.Rest;
import music.ast.Tuplet;
import music.ast.Voice;
import music.utils.ExtraMath;

/**
 * Compute the number of ticks that are needed to represent a Quarter Note.
 * 
 * This Visitor will look at all of the notes in the piece and find the least
 * common multiple of the denominators, so that the minimum number of ticks per
 * quarter note can be determined.
 * 
 */
public class OptimalTicksPerQuarterNoteVisitor implements MusicVisitor<Integer> {

    @Override
    public Integer visit(Accidental a) {
        return 1;
    }

    @Override
    public Integer visit(Tuplet tuplet) {
        return tuplet.getLength().getDenom();
    }

    @Override
    public Integer visit(Bar b) {
        int lcm = 1;
        for (MusicalElement me : b.getElements()) {
            lcm = ExtraMath.lcm(lcm, me.accept(this));
        }
        return lcm;
    }

    @Override
    public Integer visit(Chord c) {
        return c.getNotes().get(0).accept(this);
    }

    @Override
    public Integer visit(Music m) {
        int lcm = 1;
        for (Voice v : m.getVoices().values()) {
            lcm = ExtraMath.lcm(lcm, v.accept(this));
        }
        return ExtraMath.lcm(lcm, m.getDefaultLength().getDenom());
    }

    @Override
    public Integer visit(Note n) {
        return n.getLength().getDenom();
    }

    @Override
    public Integer visit(Repeat r) {
        int lcm = 1;
        for (Bar b : r.getApparentElments()) {
            lcm = ExtraMath.lcm(lcm, b.accept(this));
        }
        return lcm;
    }

    @Override
    public Integer visit(Voice v) {
        int lcm = 1;
        for (MusicSequence ms : v.getElements()) {
            lcm = ExtraMath.lcm(lcm, ms.accept(this));
        }
        return lcm;
    }

    @Override
    public Integer visit(Rest rest) {
        return rest.getLength().getDenom();
    }

}

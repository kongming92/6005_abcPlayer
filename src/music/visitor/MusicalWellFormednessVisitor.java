package music.visitor;

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
 * Walk the Music Abstract Syntax Tree, verifying that it is actually musically
 * valid.
 * 
 * Currently, this Visitor makes sure that the number of beats in a Bar
 * (measure) is consistent with the meter of the piece. It also checks to make
 * sure that Chords consist of notes of the same length.
 * 
 * Errors are recorded, and can be accessed through {@link #getErrors()}.
 * 
 */
public class MusicalWellFormednessVisitor implements
        MusicVisitor<MusicalLength> {

    private StringBuilder errors;
    private MusicalLength meter;
    private MusicalLength defaultLength;
    private String currentVoice;
    private int bar;

    public MusicalWellFormednessVisitor() {
        errors = new StringBuilder();
        meter = null;
        defaultLength = null;
        currentVoice = null;
        bar = 0;
    }

    public String getErrors() {
        return errors.toString();
    }

    @Override
    public MusicalLength visit(Accidental a) {
        return a.getLength();
    }

    @Override
    public MusicalLength visit(Tuplet tuplet) {
        return tuplet.getLength();
    }

    @Override
    public MusicalLength visit(Bar b) {
        bar++;
        MusicalLength sum = new MusicalLength(0, 1);
        for (MusicalElement me : b.getElements()) {
            MusicalLength ml = me.accept(this);
            sum = sum.add(ml.multiply(defaultLength.getNum(), defaultLength.getDenom()));
        }
        double expectedRatio = (double) meter.getNum() / meter.getDenom();
        double actualRatio = (double) sum.getNum() / sum.getDenom();
        if (actualRatio != expectedRatio) {
            errors.append(getErrorMessage()
                    + "incorrect number of beats encountered: expected "
                    + meter + " but got " + sum + "\n");
        }
        return null;
    }

    @Override
    public MusicalLength visit(Chord c) {
        MusicalLength expectedLength = c.getNotes().get(0).getLength();
        for (Note n : c.getNotes()) {
            if (!expectedLength.equals(n.accept(this))) {
                errors.append(getErrorMessage()
                        + "Chord with inconsistent note-lengths encountered: expected "
                        + expectedLength + ", but found " + n.getLength()
                        + "\n");
            }
        }
        return c.getLength();
    }

    @Override
    public MusicalLength visit(Music m) {
        meter = m.getMeter();
        defaultLength = m.getDefaultLength();
        for (Voice v : m.getVoices().values()) {
            v.accept(this);
        }
        return null;
    }

    @Override
    public MusicalLength visit(Note n) {
        return n.getLength();
    }

    @Override
    public MusicalLength visit(Repeat r) {
        for (Bar b : r.getApparentElments()) {
            b.accept(this);
        }
        return null;
    }

    @Override
    public MusicalLength visit(Voice v) {
        currentVoice = v.getName();
        bar = 0;
        for (MusicSequence ms : v.getElements()) {
            ms.accept(this);
        }
        return null;
    }

    @Override
    public MusicalLength visit(Rest rest) {
        return rest.getLength();
    }

    private String getErrorMessage() {
        String prefix = "";
        if (currentVoice != null) {
            prefix += "In Voice " + currentVoice + ", ";
        }
        prefix += "Bar " + bar + ", ";
        return prefix;
    }

}

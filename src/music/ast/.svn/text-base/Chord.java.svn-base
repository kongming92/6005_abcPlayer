package music.ast;

import java.util.ArrayList;
import java.util.List;

import music.visitor.MusicVisitor;

/**
 * A Chord is a set of notes that are to be played at the same time. All notes
 * must have the same length.
 * 
 */
public class Chord extends MusicalElement implements MusicalElementContainer {
    private final List<Note> notes;

    /**
     * Construct a new, empty Chord.
     */
    public Chord() {
        notes = new ArrayList<Note>();

    }

    /**
     * Construct a new chord, with the given notes.
     * 
     * @param n
     */
    public Chord(List<Note> n) {
        this();
        notes.addAll(n);
    }

    /**
     * Get the notes that compose tis Chord.
     * 
     * @return Get a reference to the internal List of Notes.
     */
    public List<Note> getNotes() {
        return this.notes;
    }

    /**
     * Add a note to this Chord.
     * 
     * @param n
     *            The note to add.
     */
    public void addNote(Note n) {
        this.notes.add(n);
    }

    /**
     * Get the length of this Chord.
     * 
     * This method assumes that the first note is representative of the entire
     * Chord, and returns its length. If there are no notes in the Chord, a
     * MusicalLength of 0 will be returned.
     * 
     * @return The length of this Chord.
     */
    @Override
    public MusicalLength getLength() {
        return (notes != null) ? notes.get(0).getLength() : new MusicalLength(
                0, 1);
    }

    /**
     * Implement the Visitor pattern.
     */
    @Override
    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }

    @Override
    public void add(MusicalElement me) {
        if (!(me instanceof Note)) {
            throw new IllegalArgumentException(
                    "ERROR: Can only add Notes to Chord");
        }
        notes.add((Note) me);
    }

    @Override
    public MusicalType getMusicalType() {
        return MusicalType.CHORD;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        sb.append("Chord[");
        for (Note note : notes) {
            sb.append(note.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

}

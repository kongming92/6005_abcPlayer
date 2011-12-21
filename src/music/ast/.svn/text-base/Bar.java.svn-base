package music.ast;

import java.util.ArrayList;
import java.util.List;

import music.visitor.MusicVisitor;

/**
 * A Bar (or measure) of Music. A Bar is composed of Notes and Accidentals.
 * 
 */
public class Bar extends MusicSequence implements MusicalElementContainer {
    private final List<MusicalElement> elements;

    /**
     * Construct a new, empty Bar.
     */
    public Bar() {
        this.elements = new ArrayList<MusicalElement>();
    }

    /**
     * Construct a Bar from the given Musical Elements. The given List will not
     * be stored internally, but all elements will be copied to the internal
     * List.
     * 
     * @param music
     *            The MusicalElements to add
     */
    public Bar(List<MusicalElement> music) {
        this();
        this.elements.addAll(music);

    }

    /**
     * Get the list of underlying MusicalElements stored in this Bar.
     * 
     * @return A reference to the internal List of MusicalElement
     */
    public List<MusicalElement> getElements() {
        return this.elements;
    }

    /**
     * Add the specified MusicalElement to the internal List.
     * 
     * @param me
     *            The MusicalElement to add
     */
    public void addElement(MusicalElement me) {
        this.elements.add(me);

    }

    /**
     * Implement the Visitor pattern.
     */
    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }

    @Override
    public void add(MusicalElement me) {
        elements.add(me);
    }

    @Override
    public MusicalType getMusicalType() {
        return MusicalType.BAR;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        sb.append("Bar[");
        for (MusicalElement me : elements) {
            sb.append(me.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}

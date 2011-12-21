package music.ast;

import java.util.ArrayList;
import java.util.List;

import music.visitor.MusicVisitor;

/**
 * A Tuplet is a group of notes that are played with a scaled length.
 * 
 */
public class Tuplet extends MusicalElement implements MusicalElementContainer {

    List<MusicalElement> musicalElements;
    int size;
    int currentNumNotes;

    public Tuplet(int size) {
        this.musicalElements = new ArrayList<MusicalElement>();
        this.size = size;
        this.currentNumNotes = 0;
    }

    public int getSize() {
        return size;
    }

    public int getCurrentNumNotes() {
        return currentNumNotes;
    }

    /**
     * Multiply the scale of this tuplet by the length of the inner notes.
     * 
     * @return Length of this Tuplet
     */
    @Override
    public MusicalLength getLength() {
        MusicalLength length = (musicalElements != null && musicalElements
                .size() != 0) ? musicalElements.get(0).getLength()
                : new MusicalLength(0, 1);

        if (size == 2) {
            return length.multiply(3, 2);
        } else if (size == 3) {
            return length.multiply(2, 3);
        } else if (size == 4) {
            return length.multiply(3, 4);
        } else {
            return new MusicalLength(0, 1);
        }
    }

    /**
     * Get the scale factor for this n-tuplet (used for computing lengths);
     * 
     * @return Scale of this tuplet
     */
    public MusicalLength getScale() {
        MusicalLength[] ml = { null, null, new MusicalLength(3, 2),
                new MusicalLength(2, 3), new MusicalLength(3, 4) };
        if ((size < 2) || (size >= ml.length)) {
            // Uhm. Unsupported tuplet. ZERO!
            return new MusicalLength(0, 1);
        }
        return ml[size];
    }

    public List<MusicalElement> getMusicalElements() {
        return this.musicalElements;
    }

    @Override
    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }

    @Override
    public void add(MusicalElement me) {
        musicalElements.add(me);
        if (me.getMusicalType() == MusicalType.NOTE) {
            currentNumNotes++;
        }
    }

    @Override
    public MusicalType getMusicalType() {
        return MusicalType.TUPLET;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tuplet[");
        for (MusicalElement me : musicalElements) {
            sb.append(me.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

}

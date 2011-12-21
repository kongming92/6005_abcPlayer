package music.ast;

import java.util.ArrayList;
import java.util.List;

import music.visitor.MusicVisitor;

/**
 * Repeat is a MusicSequence that will be played multiple times. It consists of
 * Bars.
 * 
 */
public class Repeat extends MusicSequence {
    private final List<Bar> bars;
    private final List<Bar> firstEnding;
    private final List<Bar> secondEnding;

    public Repeat() {
        this.bars = new ArrayList<Bar>();
        this.firstEnding = new ArrayList<Bar>();
        this.secondEnding = new ArrayList<Bar>();
    }

    public Repeat(List<Bar> e) {
        this();
        this.bars.addAll(e);
    }

    public List<Bar> getElements() {
        return this.bars;
    }

    public List<Bar> getFirstEnding() {
        return this.firstEnding;
    }

    public List<Bar> getSecondEnding() {
        return this.secondEnding;
    }

    /**
     * Linearize this repeat into Apparent Elements.
     * 
     * @return A new list of the notes, repeated correctly.
     */
    public List<Bar> getApparentElments() {
        List<Bar> apparent = new ArrayList<Bar>();
        apparent.addAll(bars);
        apparent.addAll(firstEnding);
        apparent.addAll(bars);
        apparent.addAll(secondEnding);
        return apparent;
    }

    public void addNormalBar(Bar e) {
        this.bars.add(e);
    }

    public void addToFirstEnding(Bar e) {
        this.firstEnding.add(e);
    }

    public void addToSecondEnding(Bar e) {
        this.secondEnding.add(e);
    }

    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }

    @Override
    public MusicalType getMusicalType() {
        return MusicalType.REPEAT;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Repeat[");
        for (Bar bar : bars) {
            sb.append(bar.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        if (firstEnding.size() != 0) {
            sb.append("[1 ");
            for (Bar bar : firstEnding) {
                sb.append(bar.toString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("] 2[ ");
            for (Bar bar : secondEnding) {
                sb.append(bar.toString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }

}

package music.ast;

import music.visitor.MusicVisitor;

/**
 * A Rest is a period of silence in the Music.
 * 
 */
public class Rest extends MusicalElement {

    private final MusicalLength length;

    public Rest(MusicalLength l) {
        this.length = l;
    }

    @Override
    public MusicalLength getLength() {
        return length;
    }

    @Override
    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rest[length=");
        sb.append(length.toString());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public MusicalType getMusicalType() {
        return MusicalType.REST;
    }

}

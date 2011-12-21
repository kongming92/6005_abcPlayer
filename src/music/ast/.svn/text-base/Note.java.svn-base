package music.ast;

import music.visitor.MusicVisitor;

/**
 * A Note is a Pitch that is played for a particular MusicalLength.
 * 
 */
public class Note extends MusicalElement {
    private final sound.Pitch pitch;
    private final MusicalLength length;

    public Note(sound.Pitch p, MusicalLength l) {
        this.pitch = p;
        this.length = l;
    }

    public sound.Pitch getPitch() {
        return this.pitch;
    }

    @Override
    public MusicalLength getLength() {
        return this.length;
    }

    public boolean isRest() {
        return pitch == null;
    }

    @Override
    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        sb.append("Note[pitch=");
        sb.append(pitch);
        sb.append(",length=");
        sb.append(length);
        sb.append("]");
        return sb.toString();
    }

	@Override
	public MusicalType getMusicalType() {
		return MusicalType.NOTE;
	}

}

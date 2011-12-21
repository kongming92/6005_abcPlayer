package music.ast;

import music.visitor.MusicVisitor;

/**
 * An Accidental represents a sharp or a flat that is not part of the key.
 * Accidentals hold for the entire measure, which is why they are represented as
 * MusicalElements rather than being directly applied to the note they preceded
 * only.
 * 
 */
public class Accidental extends MusicalElement {
    private final sound.Pitch pitch;
    private final int value;

    /**
     * Create a new Accidental, which will be applied to the specified pitch,
     * shifting it by the specified value.
     * 
     * @param pitch
     *            The pitch to which the accidental is being applied.
     * @param value
     *            The value by which the pitch is being shifted
     */
    public Accidental(sound.Pitch pitch, int value) {

        this.pitch = pitch;
        this.value = value;
    }

    /**
     * Retrieve the base pitch to which this Accidental is being applied.
     * 
     * @return The base (unmodified) Pitch.
     */
    public sound.Pitch getBasePitch() {
        return this.pitch;
    }

    /**
     * Apply the accidental transpose to the underlying Pitch, and return the
     * result.
     * 
     * @return The new Pitch.
     */
    public sound.Pitch getModifiedPitch() {
        return this.pitch.accidentalTranspose(value);
    }

    /**
     * Get the accidental transpose value of this Accidental.
     * 
     * @return The number of semitones that this accidental will transpose the
     *         fundamental (base) Pitch.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Implement the Visitor pattern.
     */
    @Override
    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }

    /**
     * Accidentals have no length, but must implement this method.
     */
    @Override
    public MusicalLength getLength() {
        return new MusicalLength(0, 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accidental[pitch=");
        sb.append(pitch.toString() + ",value=" + Integer.toString(value));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public MusicalType getMusicalType() {
        return MusicalType.ACCIDENTAL;
    }

}

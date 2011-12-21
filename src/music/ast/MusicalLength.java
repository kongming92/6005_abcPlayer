package music.ast;

import music.utils.ExtraMath;

/**
 * Wrapper class to represent a fraction that is a MusicalLength.
 * 
 */
public class MusicalLength {
    private final int num;
    private final int denom;

    public MusicalLength(int num, int denom) {
        this.num = num;
        this.denom = denom;
    }

    public int getNum() {
        return this.num;
    }

    public int getDenom() {
        return this.denom;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + denom;
        result = prime * result + num;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MusicalLength other = (MusicalLength) obj;
        if (denom != other.denom)
            return false;
        if (num != other.num)
            return false;
        return true;
    }

    /**
     * Convert this MusicalLength to a tick
     * 
     * @param ticksPerQuarterNote
     *            Reference ticks per Quarter Note
     * @return Ticks for this length
     */
    public int getTicks(int ticksPerQuarterNote) {
        return ticksPerQuarterNote * num / denom;
    }

    public MusicalLength multiply(int num, int denom) {
        return new MusicalLength(this.num * num, this.denom * denom);
    }

    public MusicalLength add(MusicalLength other) {
        int newNumerator = this.num * other.denom + other.num * this.denom;
        int newDenominator = this.denom * other.denom;
        int gcf = ExtraMath.gcd(newNumerator, newDenominator);
        return new MusicalLength(newNumerator / gcf, newDenominator / gcf);
    }

    public String toString() {
        int gcf = ExtraMath.gcd(num, denom);
        return Integer.toString(num / gcf) + "/"
                + Integer.toString(denom / gcf);
    }

}

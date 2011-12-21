package music.ast;

import music.visitor.MusicVisitor;

/**
 * Abstract base class for Musical Elements, e.g., Note and Accidental.
 * 
 */
public abstract class MusicalElement {

    public abstract <R> R accept(MusicVisitor<R> v);

    public abstract MusicalLength getLength();
    
    public abstract String toString();
    
    public abstract MusicalType getMusicalType();

}

package music.ast;

import music.visitor.MusicVisitor;

/**
 * Abstract base class for MusicSequence, e.g., Bar or Repeat.
 * 
 */
public abstract class MusicSequence {
	private boolean isOwned = false;
    public abstract <R> R accept(MusicVisitor<R> v);

    public abstract MusicalType getMusicalType();
    
    @Override
    public abstract String toString();
    
    public void added() {
    	isOwned = true;
    }
    
    public boolean isOwned() {
    	return isOwned;
    }
}

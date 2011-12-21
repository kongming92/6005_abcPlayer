package music.ast;

/**
 * Interface representing that a particular Musical construct consists of other
 * Musical Elements.
 * 
 */
public interface MusicalElementContainer {

    public void add(MusicalElement me);

    public MusicalType getMusicalType();

}

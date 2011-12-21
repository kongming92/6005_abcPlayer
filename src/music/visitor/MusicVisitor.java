package music.visitor;

import music.ast.Accidental;
import music.ast.Bar;
import music.ast.Chord;
import music.ast.Music;
import music.ast.Note;
import music.ast.Repeat;
import music.ast.Rest;
import music.ast.Tuplet;
import music.ast.Voice;

/**
 * Define the interface that all Visitors will implement, to traverse the
 * Abstract Syntax Tree representing an ABC music file.
 * 
 * Note that there are no visit() method for MusicSequence and MusicElement,
 * because these are abstract classes, and the Visitor should get the fully
 * derived type.
 */
public interface MusicVisitor<R> {
    public R visit(Accidental a);

    public R visit(Tuplet tuplet);

    public R visit(Bar b);

    public R visit(Chord c);

    public R visit(Music m);

    public R visit(Note n);

    public R visit(Repeat r);

    public R visit(Voice v);

    public R visit(Rest rest);

}

package player;

import org.junit.Test;

/**
 * MainTest class Tests the player end to end Test by listening. Hopefully you
 * know music :D
 */
public class MainTest {

    @Test
    public void playsample1() {
        Main.play("sample_abc/sample1.abc");
    }
    @Test
    public void playsample2() {
        Main.play("sample_abc/sample2.abc");
    }
    @Test
    public void playtuplerest() {
        Main.play("sample_abc/tuplerest.abc");
    }
    @Test
    public void playsample3() {
        Main.play("sample_abc/sample3.abc");
    }

    @Test
    public void playScale() {
        Main.play("sample_abc/scale.abc");
    }

    @Test
    public void playRowRowRowYourBoat() {
        Main.play("sample_abc/piece1.abc");
    }

    @Test
    public void playMario() {
        Main.play("sample_abc/piece2.abc");
    }

    @Test
    public void playLittleNightMusic() {
        Main.play("sample_abc/little_night_music.abc");
    }

    @Test
    public void playPrelude() {
        Main.play("sample_abc/prelude.abc");
    }

    @Test
    public void playPaddy() {
        Main.play("sample_abc/paddy.abc");
    }

    @Test
    public void playFurElise() {
        Main.play("sample_abc/fur_elise.abc");
    }

    @Test
    public void playInvention() {
        Main.play("sample_abc/invention.abc");
    }

    @Test
    public void playComments() {
        Main.play("sample_abc/comments.abc");
    }
//Tests from here are expected to "fail gracefully"
    @Test
    public void playBadL() {
        Main.play("sample_abc/bad_L.abc");
    }

    @Test
    public void playLotsOfChord() {
        Main.play("sample_abc/lots_of_chord.abc");
    }

    @Test
    public void barlines() {
        Main.play("sample_abc/barlines.abc");
    }

    @Test
    public void crazymusic() {
        Main.play("sample_abc/crazymusic.abc");
    }

    @Test
    public void flat_sharp() {
        Main.play("sample_abc/flat_sharp.abc");
    }

    @Test
    public void nestrepeats() {
        Main.play("sample_abc/nest_repeats.abc");
    }

    @Test
    public void no_note_v() {
        Main.play("sample_abc/no_note_v.abc");
    }

    @Test
    public void octave_rest() {
        Main.play("sample_abc/octave_rest.abc");
    }

    @Test
    public void octaveupdown() {
        Main.play("sample_abc/octaveupdown.abc");
    }

    @Test
    public void super_high_octave() {
        Main.play("sample_abc/super_high_octave.abc");
    }

    @Test
    public void super_sharp() {
        Main.play("sample_abc/super_sharp.abc");
    }

    @Test
    public void super_tuplet() {
        Main.play("sample_abc/super_tuplet.abc");
    }

}

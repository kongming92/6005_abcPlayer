package music.ast;

import java.util.HashMap;
import java.util.Map;

import music.visitor.MusicVisitor;

/**
 * The actual Music from the ABC file. Music consists of at least once voice.
 * 
 */
public class Music {

    private Map<String, Voice> voices;
    private int tempo;
    private MusicalLength meter;
    private MusicalLength defaultLength;
    private KeySignature keySignature;
    private String indexNumber;
    private String title;
    private String composer;

    public Music() {
        this.voices = new HashMap<String, Voice>();

        // default values
        this.tempo = 100;
        this.meter = new MusicalLength(4, 4);
        this.defaultLength = new MusicalLength(1, 8);
        this.keySignature = new KeySignature(); // C Major
        this.indexNumber = "1";
        this.title = "Unknown title";
        this.composer = "Unknown composer";
    }

    public void addVoice(String s, Voice v) {
        voices.put(s, v);
    }

    public boolean contains(Voice v) {
        return voices.containsKey(v);
    }

    public Voice getVoice(String s) {
        return voices.get(s);
    }

    public Map<String, Voice> getVoices() {
        return voices;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void setMeter(MusicalLength meter) {
        this.meter = meter;
    }

    public void setDefaultLength(MusicalLength defaultLength) {
        this.defaultLength = defaultLength;
    }

    public void setKeySignature(KeySignature keySignature) {
        this.keySignature = keySignature;
    }
    
    public void setIndexNumber(String indexNumber) {
    	this.indexNumber = indexNumber;
    }
    
    public void setTitle (String title) {
    	this.title = title;
    }
    
    public void setComposer(String composer) {
    	this.composer = composer;
    }

    public int getTempo() {
        return tempo / defaultLength.getDenom() * 4 * defaultLength.getNum();
    }

    public MusicalLength getMeter() {
        return meter;
    }

    public MusicalLength getDefaultLength() {
        return defaultLength;
    }

    public KeySignature getKeySignature() {
        return keySignature;
    }
    
    public String getIndexNumber() {
    	return indexNumber;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public String getComposer() {
    	return composer;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Music[");
        for (Voice v : voices.values()) {
            sb.append(v.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
    

    /**
     * Implement the Visitor pattern.
     */
    public <R> R accept(MusicVisitor<R> v) {
        return v.visit(this);
    }

}

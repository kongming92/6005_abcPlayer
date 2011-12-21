package music.ast;

import java.util.ArrayList;
import java.util.List;

import music.visitor.MusicVisitor;

/**
 * A Voice is a particular list of MusicSeqence that are to be played linearly.
 * 
 */
public class Voice {
	private final List<MusicSequence> seq;
	private boolean seenBeginRepeat;
	private final String name;

	public Voice(String name) {
		this.seq = new ArrayList<MusicSequence>();
		this.seenBeginRepeat = false;
		this.name = name;
	}

	public Voice(List<MusicSequence> s, String name) {
		this(name);
		this.seq.addAll(s);
	}

	public List<MusicSequence> getElements() {
		return this.seq;
	}

	public <R> R accept(MusicVisitor<R> v) {
		return v.visit(this);
	}

	public void seeBeginRepeat() {
		this.seenBeginRepeat = true;
	}

	public boolean hasBeginRepeat() {
		return seenBeginRepeat;
	}

	public void add(MusicSequence ms) {
		if (ms.isOwned()) {
			// Uh oh. Ignore?
			return;
		}
		ms.added();
		this.seq.add(ms);
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Voice[");
		for (MusicSequence ms : seq) {
			sb.append(ms.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
}

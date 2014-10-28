package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 10/28/14.
 * This is the representation of a Song object and will be used to generate a block.
 */
public class Song {
    List<Track> tracks;
    List<ForLoop> forLoops;
    List<ESEffect> effects;
    int tempoBPM;
    int phraseLength;

    public Song() {
        this(120);
    }

    public Song(int tempoBPM) {
        this(tempoBPM,8);
    }

    public Song(int tempoBPM, int phraseLength) {
        this(tempoBPM,phraseLength,new ArrayList<Track>());
    }

    public Song(int tempoBPM, int phraseLength, List<Track> tracks) {
        this.tempoBPM = tempoBPM;
        this.phraseLength = phraseLength;
        this.tracks = tracks;
        this.forLoops = new ArrayList<ForLoop>();
        this.effects = new ArrayList<ESEffect>();
    }


}

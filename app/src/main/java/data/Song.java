package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 10/28/14.
 * This is the representation of a Song object and will be used to generate a block.
 */
public class Song {
    List<Section> sections;
    List<ForLoop> forLoops;
    List<ESEffect> effects;
    int tempoBPM;
    int phraseLength;
    String description;

    public Song() {
        this(120);
    }

    public Song(int tempoBPM) {
        this(tempoBPM,8,"");
    }

    public Song(int tempoBPM, int phraseLength, String description) {
        this(tempoBPM, phraseLength, new ArrayList<Section>(), description);
    }

    public Song(int tempoBPM, int phraseLength, List<Section> sections, String description) {
        this.tempoBPM = tempoBPM;
        this.phraseLength = phraseLength;
        this.sections = sections;
        this.forLoops = new ArrayList<ForLoop>();
        this.effects = new ArrayList<ESEffect>();
        this.description = description;
    }


}

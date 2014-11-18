package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 11/16/14.
 * This is a holder for a number of sections.
 */
public class Group {
    List<Section> songs;
    String description;

    public Group(String description) {
        this.songs = new ArrayList<Section>();
        this.description = description;
    }

    public Group(List<Section> songs, String description) {
        this.songs = songs;
        this.description = description;
    }

    public void addSection(Section section) {
        this.songs.add(section);
    }

    public boolean removeSection(Section section) {
        return this.songs.remove(section);
    }
}

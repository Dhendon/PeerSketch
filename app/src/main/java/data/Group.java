package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 11/16/14.
 * This is a holder for a number of sections.
 */
public class Group {
    List<Section> sections;
    String description;

    public Group(String description) {
        this.sections = new ArrayList<Section>();
        this.description = description;
    }

    public Group(List<Section> sections, String description) {
        this.sections = sections;
        this.description = description;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public boolean removeSection(Section section) {
        return this.sections.remove(section);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public String getDescription() { return this.description; }
}

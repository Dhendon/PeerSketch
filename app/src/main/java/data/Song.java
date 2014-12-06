package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 10/28/14.
 * This is the representation of a Song object and will be used to generate a block.
 */
public class Song implements Serializable {
    private static final long serialVersionUID = 7L;
    List<Section> sections;
    List<ForLoop> forLoops;
    List<IfStatement> ifStatements;
    List<ESSetEffect> effects;
    List<Group> groups;
    int tempoBPM;
    int phraseLength;
    String description;

    public Song() {
        this(120);
    }

    public Song(int tempoBPM) {
        this(tempoBPM, 8, "");
    }

    public Song(int tempoBPM, int phraseLength, String description) {
        this(tempoBPM, phraseLength, new ArrayList<Section>(), description);
    }

    public Song(int tempoBPM, int phraseLength, List<Section> sections, String description) {
        this.tempoBPM = tempoBPM;
        this.phraseLength = phraseLength;
        this.sections = sections;
        this.forLoops = new ArrayList<ForLoop>();
        this.effects = new ArrayList<ESSetEffect>();
        this.description = description;
    }

    public void addSection(Section section, int location) {
        if (validLocation(location, sections)) {
            section.setSectionNumber(location);
            sections.add(location, section);
        } else {
            section.setSectionNumber(0);
            sections.add(section);
        }
    }

    public void addGroup(Group group, int location) {
        if (validLocation(location, groups)) {
            groups.add(location, group);
        } else {
            groups.add(group);
        }
    }

    public void addIfStatement(IfStatement ifStatement, int location) {
        if (validLocation(location, ifStatements)) {
            ifStatements.add(location, ifStatement);
        } else {
            ifStatements.add(ifStatement);
        }

    }

    public void addForLoop(ForLoop forLoop, int location) {
        if (validLocation(location, forLoops)) {
            forLoops.add(location, forLoop);
        } else {
            forLoops.add(forLoop);
        }

    }

    private boolean validLocation(int location, List<?> list) {
        return location <= list.size() && location >= 0;
    }

    public int getTempoBPM() {
        return tempoBPM;
    }

    public void setTempoBPM(int tempoBPM) {
        this.tempoBPM = tempoBPM;
    }

    public int getPhraseLength() {
        return phraseLength;
    }

    public void setPhraseLength(int phraseLength) {
        this.phraseLength = phraseLength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<ForLoop> getForLoops() {
        return forLoops;
    }

    public void setForLoops(List<ForLoop> forLoops) {
        this.forLoops = forLoops;
    }

    public List<IfStatement> getIfStatements() {
        return ifStatements;
    }

    public void setIfStatements(List<IfStatement> ifStatements) {
        this.ifStatements = ifStatements;
    }

    public List<ESSetEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<ESSetEffect> effects) {
        this.effects = effects;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}

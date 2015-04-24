package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidhendon on 10/28/14.
 * This is the representation of a Song object and will be used to generate a block.
 */
public class Song extends Group implements Serializable {
    private static final long serialVersionUID = 7L;
    private List<Section> sections;
    private List<ForLoop> forLoops;
    private List<IfStatement> ifStatements;
    private List<ESSetEffect> effects;
    private List<Group> groups;
    private HashMap<String, String> variables;
    private int tempoBPM;
    private int phraseLength;
    private String description;
    private OnSongChangeListener listener;

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
        this.groups = new ArrayList<Group>();
        this.forLoops = new ArrayList<ForLoop>();
        this.effects = new ArrayList<ESSetEffect>();
        this.description = description;
        groups = new ArrayList<Group>();
    }

    public void addSection(Section section, int location) {
        if (validLocation(location, sections)) {
            section.setSectionNumber(location);
            sections.add(location, section);
        } else {
            section.setSectionNumber(0);
            sections.add(section);
        }
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public Section removeSection(int location) {
        Section removed = null;
        if (validLocation(location, sections)) {
            removed = sections.remove(location);
            if (listener != null) {
                listener.onSongChange(this);
            }
        }
        return removed;
    }

    public void setListener(OnSongChangeListener listener) {
        this.listener = listener;
    }

    public HashMap<String, String> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, String> variables) {
        this.variables = variables;
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public void addGroup(Group group, int location) {
        if (validLocation(location, groups)) {
            groups.add(location, group);
        } else {
            groups.add(group);
        }
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public void addIfStatement(IfStatement ifStatement, int location) {
        if (validLocation(location, ifStatements)) {
            ifStatements.add(location, ifStatement);
        } else {
            ifStatements.add(ifStatement);
        }
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public void addForLoop(ForLoop forLoop, int location) {
        if (validLocation(location, forLoops)) {
            forLoops.add(location, forLoop);
        } else {
            forLoops.add(forLoop);
        }
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    private boolean validLocation(int location, List<?> list) {
        return location <= list.size() && location >= 0;
    }

    public boolean addVariable(String variable, String value) {
        if (variables.containsKey(variable)) {
            return false;
        } else {
            variables.put(variable, value);
            if (listener != null) {
                listener.onSongChange(this);
            }
            return true;
        }
    }

    public int getTempoBPM() {
        return tempoBPM;
    }

    public void setTempoBPM(int tempoBPM) {
        this.tempoBPM = tempoBPM;
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public int getPhraseLength() {
        return phraseLength;
    }

    public void setPhraseLength(int phraseLength) {
        this.phraseLength = phraseLength;
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public List<ForLoop> getForLoops() {
        return forLoops;
    }

    public void setForLoops(List<ForLoop> forLoops) {
        this.forLoops = forLoops;
        if (listener != null) {
            listener.onSongChange(this);
        }
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
        if (listener != null) {
            listener.onSongChange(this);
        }
    }

    public interface OnSongChangeListener {
        public void onSongChange(Song song);
    }

    public interface OnSongChangeProvider {
        void setOnSongChangeListener(OnSongChangeListener listener);
    }
}

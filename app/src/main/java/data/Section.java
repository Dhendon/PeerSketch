package data;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidhendon on 10/28/14.
 */
public class Section extends Group implements Serializable {
    // TODO: Implement parent instance variable, with a reference to song it's attached to.
    private static final long serialVersionUID = 6L;
    HashMap<String, List<Double>> variables;
    private List<ForLoop> forLoops;
    private List<IfStatement> ifStatements;
    private List<ESSetEffect> effects;
    private List<ESFitMedia> fitMedias;
    private List<ESMakeBeat> makeBeats;
    private List<Pair<Double, Double>> activeMeasures; // TODO: update this when parameters are changed
    private String name;
    private int sectionNumber;
    private int tempoBPM;
    private int phraseLengthMeasures;
    private OnSectionChangeListener listener;

    public Section(String name) {
        this.name = name;
        // TODO Change this later.
        tempoBPM = 120;
        phraseLengthMeasures = 8;
        forLoops = new ArrayList<ForLoop>();
        ifStatements = new ArrayList<IfStatement>();
        effects = new ArrayList<ESSetEffect>();
        fitMedias = new ArrayList<ESFitMedia>();
        makeBeats = new ArrayList<ESMakeBeat>();
        activeMeasures = new ArrayList<Pair<Double, Double>>();
        variables = new HashMap<String, List<Double>>();
    }

    public HashMap<String, List<Double>> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, List<Double>> variables) {
        this.variables = variables;
        if (listener != null) {
            listener.onSectionChange(this);
        }
    }

    public void setListener(OnSectionChangeListener listener) {
        this.listener = listener;
    }

    public boolean addVariable(String variable, List<Double> values) {
        if (variables.containsKey(variable)) {
            return false;
        } else {
            variables.put(variable, values);
            return true;
        }
    }


    // TODO: Update active measures according to newly added functions

    public void add(ForLoop forLoop, int location) {
        if (validLocation(location, forLoops)) {
            forLoops.add(location, forLoop);
        } else {
            forLoops.add(forLoop);
        }
        if (listener != null) {
            listener.onSectionChange(this);
        }
    }

    /**
     * Adds an item to the section
     *
     * @param ifStatement newly user created if statement
     * @param location    this determines the order in which to show the object on the screen
     */
    public void add(IfStatement ifStatement, int location) {
        if (validLocation(location, ifStatements)) {
            ifStatements.add(location, ifStatement);
        } else {
            ifStatements.add(ifStatement);
        }
        if (listener != null) {
            listener.onSectionChange(this);
        }
    }

    public void add(ESSetEffect effect, int location) {
        if (validLocation(location, effects)) {
            effects.add(location, effect);
        } else {
            effects.add(effect);
        }
        if (listener != null) {
            listener.onSectionChange(this);
        }
    }

    public void add(ESFitMedia fitMedia, int location) {
        if (validLocation(location, fitMedias)) {
            fitMedias.add(location, fitMedia);
        } else {
            fitMedias.add(fitMedia);
        }
        if (listener != null) {
            listener.onSectionChange(this);
        }
    }

    public void add(ESMakeBeat makeBeat, int location) {
        if (validLocation(location, makeBeats)) {
            makeBeats.add(location, makeBeat);
        } else {
            makeBeats.add(makeBeat);
        }
        if (listener != null) {
            listener.onSectionChange(this);
        }
    }

    private boolean validLocation(int location, List<?> list) {
        return location <= list.size() && location >= 0;
    }

    // TODO: Implement this method
    public boolean isValid() {
        return true;
    }

    public List<ForLoop> getForLoops() {
        return forLoops;
    }

    public List<IfStatement> getIfStatements() {
        return ifStatements;
    }

    public List<ESSetEffect> getEffects() {
        return effects;
    }

    public List<ESFitMedia> getFitMedias() {
        return fitMedias;
    }

    public List<ESMakeBeat> getMakeBeats() {
        return makeBeats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTempoBPM() {
        return tempoBPM;
    }

    public void setTempoBPM(int tempoBPM) {
        this.tempoBPM = tempoBPM;
    }

    public int getPhraseLengthMeasures() {
        return phraseLengthMeasures;
    }

    public void setPhraseLengthMeasures(int phraseLengthMeasures) {
        this.phraseLengthMeasures = phraseLengthMeasures;
    }

    @Override
    public String toString() {
        return "Section{" +
                "name='" + name + '\'' +
                ", fitMedias=" + fitMedias +
                '}';
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public void clearAll() {
        forLoops = new ArrayList<ForLoop>();
        ifStatements = new ArrayList<IfStatement>();
        effects = new ArrayList<ESSetEffect>();
        fitMedias = new ArrayList<ESFitMedia>();
        makeBeats = new ArrayList<ESMakeBeat>();
        activeMeasures = new ArrayList<Pair<Double, Double>>();
        if (listener != null) {
            listener.onSectionChange(this);
        }
    }

    public List<Pair<Double, Double>> calcActiveMeasures() {
        ArrayList<Pair<Double, Double>> activeMeasures = new ArrayList<Pair<Double, Double>>();
        for (ESFitMedia fitMedia : fitMedias) {
            double startLocation = fitMedia.getStartLocation();
            double endLocation = fitMedia.getEndLocation();
            Pair<Double, Double> measureRange = new Pair<Double, Double>(startLocation, endLocation);
            activeMeasures.add(measureRange);
        }
        // TODO: incorporate makebeat and for loops
        return activeMeasures;
    }

    public interface OnSectionChangeListener {
        public void onSectionChange(Section section);
    }

    public interface OnSectionChangeProvider {
        void setOnSectionChangeListener(OnSectionChangeListener listener);}
}

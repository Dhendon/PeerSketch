package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 11/16/14.
 * This is a holder for a number of sections.
 */
public class Group implements Serializable, GroupObject {
    private static final long serialVersionUID = 4L;
    List<ESFitMedia> fitMedias;
    List<ESMakeBeat> makeBeats;
    List<ESSetEffect> setEffects;
    List<Group> subgroups;
    List<GroupObject> orderedObjects;
    private String name;
    private ESSetEffect groupSetEffect;

    public Group(String name, ESSetEffect groupSetEffect, List<ESFitMedia> fitMedias,
                 List<ESMakeBeat> makeBeats, List<ESSetEffect> setEffects, List<Group> subgroups,
                 List<GroupObject> orderedObjects) {
        this.name = name;
        this.groupSetEffect = groupSetEffect;
        this.fitMedias = fitMedias;
        this.makeBeats = makeBeats;
        this.setEffects = setEffects;
        this.subgroups = subgroups;
        this.orderedObjects = orderedObjects;
    }

    public Group(String name) {
        this(name, null, new ArrayList<ESFitMedia>(), new ArrayList<ESMakeBeat>(),
                new ArrayList<ESSetEffect>(), new ArrayList<Group>(), new ArrayList<GroupObject>());
    }

    public Group() {
        this("");
    }

    public List<GroupObject> getOrderedObjects() {
        return orderedObjects;
    }

    public void setOrderedObjects(List<GroupObject> orderedObjects) {
        this.orderedObjects = orderedObjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ESFitMedia> getFitMedias() {
        return fitMedias;
    }

    public void setFitMedias(List<ESFitMedia> fitMedias) {
        this.fitMedias = fitMedias;
    }

    public List<ESMakeBeat> getMakeBeats() {
        return makeBeats;
    }

    public void setMakeBeats(List<ESMakeBeat> makeBeats) {
        this.makeBeats = makeBeats;
    }

    public List<ESSetEffect> getSetEffects() {
        return setEffects;
    }

    public void setSetEffects(List<ESSetEffect> setEffects) {
        this.setEffects = setEffects;
    }

    public List<Group> getSubgroups() {
        return subgroups;
    }

    public void setSubgroups(List<Group> subgroups) {
        this.subgroups = subgroups;
    }

    public void add(int location, ESFitMedia fitMedia) {
        fitMedias.add(location, fitMedia);
    }

    public void add(ESFitMedia fitMedia) {
        fitMedias.add(fitMedia);
    }

    public void add(int location, ESMakeBeat makeBeat) {
        makeBeats.add(location, makeBeat);
    }

    public void add(ESMakeBeat makeBeat) {
        makeBeats.add(makeBeat);
    }

    public void add(int location, GroupObject object) {
        orderedObjects.add(location, object);
    }

    public void add(Group group) {
        subgroups.add(group);
    }

    public void add(int location, Group group) {
        subgroups.add(location, group);
    }

    public void addObject(GroupObject object) {
        orderedObjects.add(object);
    }

    /**
     * This ensures that the user can only add one effect at a time to a Group.
     *
     * @return Returns the ESSetEffect that this is replaces or returns null if none is set.
     */
    public ESSetEffect setGroupEffect(ESSetEffect setEffect) {
        ESSetEffect replaced = groupSetEffect;
        groupSetEffect = setEffect;
        return replaced;
    }
}

package gatech.adam.peersketch.views;

import java.util.ArrayList;

public class ExpandableList_Group {
    private String name;
    private ArrayList<ExpandableList_Child> items;

    public ExpandableList_Group(String name, ArrayList<ExpandableList_Child> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ExpandableList_Child> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExpandableList_Child> items) {
        this.items = items;
    }

    public boolean hasItems() {
        if (items.size() > 0) { return true; }
        else { return false; }
    };
}

package gatech.adam.peersketch.views;

public class ExpandableList_Child {
    private String name;
    private String tag;

    public ExpandableList_Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

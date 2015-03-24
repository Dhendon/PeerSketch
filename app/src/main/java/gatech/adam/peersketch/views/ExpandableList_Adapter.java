package gatech.adam.peersketch.views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gatech.adam.peersketch.R;

public class ExpandableList_Adapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ExpandableList_Group> groups;
    private String type;

    public ExpandableList_Adapter(Context context, ArrayList<ExpandableList_Group> groups, String type) {
        this.context = context;
        this.groups = groups;
        this.type = type;
    }

    public void addItem(ExpandableList_Child item, ExpandableList_Group group) {
        if (!groups.contains(group)) {
            groups.add(group);
            int index = groups.indexOf(group);
            ArrayList<ExpandableList_Child> ch = groups.get(index).getItems();
            ch.add(item);
            groups.get(index).setItems(ch);
        }
    }

    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ExpandableList_Child> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        ExpandableList_Child child = (ExpandableList_Child) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.list_item_textview);
        tv.setText(child.getName().toString());
        tv.setTag(child.getTag());

        if (type.equals("pallet")) {
            Drawable roundedCorners = context.getResources().getDrawable(R.drawable.shape_rounded_corners);
            roundedCorners.setColorFilter(R.color.black, PorterDuff.Mode.DARKEN);

            tv.setBackgroundDrawable(roundedCorners);
            tv.setHeight(96);
        }

        return view;
    }

    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        ArrayList<ExpandableList_Child> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        ExpandableList_Group group = (ExpandableList_Group) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.list_group, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.list_group_textview);
        tv.setText(group.getName());

        if (type.equals("pallet")) {
            tv.setBackgroundResource(R.drawable.shape_rounded_corners);
            tv.setHeight(128);
        }

        return view;
    }

    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }


}

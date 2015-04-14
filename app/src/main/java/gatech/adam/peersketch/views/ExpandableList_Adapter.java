package gatech.adam.peersketch.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.Group;
import data.Section;
import data.Song;
import gatech.adam.peersketch.Activity_Main;
import gatech.adam.peersketch.R;

public class ExpandableList_Adapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<ExpandableList_Group> groups;
    private Activity_Main.ExpandableListAdapterMode mMode;

    public ExpandableList_Adapter(Context context, ArrayList<ExpandableList_Group> groups, Activity_Main.ExpandableListAdapterMode mode) {
        this.mContext = context;
        this.groups = groups;
        this.mMode = mode;
    }

    public ExpandableList_Adapter (Context context, Song song, Activity_Main.ExpandableListAdapterMode mode) {
        this.mContext = context;
        this.groups = parseSongToGroup(song);
        this.mMode = mode;
    }



    public ArrayList<ExpandableList_Group> parseSongToGroup(Song song) {
        // Get groups and sections
        List<Group> mGroups = song.getGroups();
        List<Section> mSections = song.getSections();

        // Adding non-grouped sections
        ArrayList<ExpandableList_Group> groups = new ArrayList<>();
        for (int i = 0; i < mSections.size(); i++) {
            groups.add(new ExpandableList_Group(mSections.get(i).getName(), new ArrayList<ExpandableList_Child>()));
        }

        // Adding groups
        for (int i = 0; i < mGroups.size(); i++) {
            // Create and iterate through list of child elements for every group element
            ArrayList<ExpandableList_Child> childList = new ArrayList<ExpandableList_Child>();
            int groupSize = mGroups.get(i).getSubgroups().size(); // Number of sections in group

            for (int j = 0; j < groupSize; j++) {
                String childSampleName = mGroups.get(i).getSubgroups().get(j).getName();
                childList.add(new ExpandableList_Child(childSampleName));
            }

            // Adding children to group, and adding group to groups list
            groups.add(new ExpandableList_Group( mGroups.get(i).getName(), childList));
        }

        return groups;
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

    public void updateBySong(Song song) {
        this.groups = parseSongToGroup(song);
        notifyDataSetChanged();
    }

    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ExpandableList_Child> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public ArrayList<ExpandableList_Group> getGroups() {
        return groups;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        ExpandableList_Child child = (ExpandableList_Child) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.list_item_textview);
        tv.setText(child.getName().toString());
        tv.setTag(child.getTag());

        // If this is pallet drawer, apply rounded rect
        if (mMode == Activity_Main.ExpandableListAdapterMode.PALLET_DRAWER) {
            Drawable roundedCorners = mContext.getResources().getDrawable(R.drawable.shape_rounded_corners);
            roundedCorners.setColorFilter(R.color.black, PorterDuff.Mode.DARKEN);

            tv.setBackgroundDrawable(roundedCorners);
            tv.setHeight(96);
        }
        else {
            tv.setHeight(128);
            tv.setTextSize(20);
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

        ExpandableList_Group group = (ExpandableList_Group) getGroup(groupPosition); // Get handle to group
        boolean hasChildren = group.hasItems(); // Check if group has children

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.list_group, null);
        }

        TextView tv = (TextView) view.findViewById(R.id.list_group_textview);
        tv.setText(group.getName());

        ImageView imageView = (ImageView) view.findViewById(R.id.expandableIcon);

        // Pallet
        if (mMode == Activity_Main.ExpandableListAdapterMode.PALLET_DRAWER) {
            tv.setBackgroundResource(R.drawable.shape_rounded_corners);
            tv.setHeight(128);
            imageView.setVisibility(View.GONE);
        }
        // Groups
        else if (mMode == Activity_Main.ExpandableListAdapterMode.SONG_EDITOR && hasChildren) {
            imageView.setVisibility(View.VISIBLE);
            tv.setHeight(128);
        }
        // Tracks, Sections
        else if (mMode == Activity_Main.ExpandableListAdapterMode.SONG_EDITOR && !hasChildren) {
            imageView.setVisibility(View.GONE);
        }
        // Else...
        else {
            imageView.setVisibility(View.GONE);
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

    private void rotate(float degree, ImageView imgview) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        imgview.startAnimation(rotateAnim);
    }
}

// Song
// Select tracks from list view, then open track fragment

package gatech.adam.peersketch;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.List;

import data.Group;
import data.Section;
import data.Song;
import gatech.adam.peersketch.views.ExpandableList_Adapter;
import gatech.adam.peersketch.views.ExpandableList_Child;
import gatech.adam.peersketch.views.ExpandableList_Group;

public class Fragment_Song extends Fragment {

    private static final String SONG_POSITION = "songPosition"; // Position in song library
    private Song mSong;
    private List<Group> mGroups;
    private List<Section> mSections;
    private ExpandableListView mTrackList;
    private FrameLayout mSongMap;
    private Activity_Main mActivity;

    // Returns new instance of song fragment
    public static Fragment_Song newInstance(int position) {
        Fragment_Song fragment = new Fragment_Song();
        Bundle args = new Bundle();
        args.putInt(SONG_POSITION, position);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get pointer to main activity
        mActivity = (Activity_Main) getActivity();

        // Getting selected song
        if (getArguments() != null) {
            int songPosition = getArguments().getInt(SONG_POSITION);
            mSong = mActivity.mSongLibrary.getSong(songPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate rootView to activity_main/container and get pointer
        View rootView = inflater.inflate(R.layout.fragment_song, container, false);

        // Get pointer to expandable track list and song mini-map
        mTrackList = (ExpandableListView) rootView.findViewById(R.id.listView_tracks);
        mSongMap = (FrameLayout) rootView.findViewById(R.id.frameLayout_songMap);

        // Get groups and sections
        mGroups = mSong.getGroups();
        mSections = mSong.getSections();

        // Adding non-grouped sections
        ArrayList<ExpandableList_Group> groups = new ArrayList<>();
        for (int i = 0; i < mSections.size(); i++) {
            groups.add(new ExpandableList_Group(mSections.get(i).getSample(), new ArrayList<ExpandableList_Child>()));
        }

        // Adding groups
        for (int i = 0; i < mGroups.size(); i++) {
            // Create and iterate through list of child elements for every group element
            ArrayList<ExpandableList_Child> childList = new ArrayList<ExpandableList_Child>();
            int groupSize = mGroups.get(i).getSections().size(); // Number of sections in group

            for (int j = 0; j < groupSize; j++) {
                String childSampleName = mGroups.get(i).getSections().get(j).getSample();
                childList.add(new ExpandableList_Child(childSampleName));
            }

            // Adding children to group, and adding group to groups list
            groups.add(new ExpandableList_Group( mGroups.get(i).getDescription(), childList));
        }

        // Initiating track list adapter
        ExpandableList_Adapter trackList_adapter = new ExpandableList_Adapter(
                getActivity(),
                groups,
                "song"
        );


//        // Pad's children
//        ArrayList<ExpandableList_Child> children1 = new ArrayList<>();
//        children1.add(new ExpandableList_Child("Track 4"));
//        children1.add(new ExpandableList_Child("Track 5"));
//        children1.add(new ExpandableList_Child("Track 6"));
//        children1.add(new ExpandableList_Child("Track 7"));
//
//        // Bass's children
//        ArrayList<ExpandableList_Child> children2 = new ArrayList<ExpandableList_Child>();
//        children2.add(new ExpandableList_Child("Track 8"));
//        children2.add(new ExpandableList_Child("Track 9"));
//
//        // Rhythm's children
//        ArrayList<ExpandableList_Child> children3 = new ArrayList<ExpandableList_Child>();
//        children3.add(new ExpandableList_Child("Track 10"));
//        children3.add(new ExpandableList_Child("Track 11"));
//
//        // Populating groups...
//        ArrayList<ExpandableList_Group> groups = new ArrayList<>();
//        groups.add(new ExpandableList_Group("Pad", children1));
//        groups.add(new ExpandableList_Group("Bass", children2));
//        groups.add(new ExpandableList_Group("Rhythm", children3));



        // Getting pointer to expandable list view in root, fragment_song.xml, and setting adapter
        mTrackList.setAdapter(trackList_adapter);

        // Setting drag listeners
        mSongMap.setOnDragListener(new DragEventListener_Fragment_Song(false)); // Can't receive drops, sends view back to parent
        mTrackList.setOnDragListener(new DragEventListener_Fragment_Song(true)); // Can receive drops, appends view to current view

        return rootView;
    }

    protected class DragEventListener_Fragment_Song
        implements View.OnDragListener {

        private boolean mCanReceive; // Stores whether this listener accepts drop events

        public DragEventListener_Fragment_Song(boolean canReceive) {
            mCanReceive = canReceive;
        }

        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction(); // Getting current drag action

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Closing main activity drawers
                    mActivity.closeDrawers();
                    break;
                case DragEvent.ACTION_DROP:
                    // Getting handle to view and view parent
                    //View view = (View) event.getLocalState();
                    //ViewGroup owner = (ViewGroup) view.getParent();

                    // If can receive drops, remove from parent and append to current view
                    if (mCanReceive) {
                        // owner.removeView(view);
                        // TODO: append to current view
                    }
                    // If this can't receive drops, return to parent view
                    else {
                        mActivity.openPalletDrawer();
                    }
                    break;
            }

            return true;
        }
    }
}

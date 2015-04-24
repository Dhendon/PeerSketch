// Song
// Select tracks from list view, then open track fragment

package gatech.adam.peersketch;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import audio.ESAudio;
import data.Group;
import data.Section;
import data.Song;
import gatech.adam.peersketch.views.ExpandableList_Adapter;

public class Fragment_SongEdit extends Fragment {

    private Fragment_Fab mFab;
    private Song mSong;
    private List<Group> mGroups;
    private List<Section> mSections;
    private ExpandableListView mTrackList;
    private Surface_Minimap mSongMap;
    private Activity_Main mActivity;
    private Fragment_Fab mFabFragment;
    private FloatingActionButton mPlay;
    private ExpandableList_Adapter mTrackListAdapter;

    // Returns new instance of song fragment
    public static Fragment_SongEdit newInstance(Song song) {
        Fragment_SongEdit fragment = new Fragment_SongEdit();
        fragment.mSong = song;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get handle to main activity
        mActivity = (Activity_Main) getActivity();

        // Getting groups and mSections
        mGroups = mSong.getGroups();
        mSections = mSong.getSections();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate rootView to activity_main/container and get pointer
        final View rootView = inflater.inflate(R.layout.fragment_song_edit, container, false);

        // Getting song minimap
        mSongMap = (Surface_Minimap) rootView.findViewById(R.id.surfaceView_songMap);
        mSongMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity.getApplicationContext(),
                        "Minimap item selected", Toast.LENGTH_SHORT).show();

            }
        });

        mSongMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("Activity_Main", "Test");
                mTrackList.deferNotifyDataSetChanged();
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    List<Pair<Rect, Integer >> rectanglePairs = mSongMap.getRectanglePairs();
                    for (Pair<Rect, Integer> pair : rectanglePairs) {

                        Rect rect = pair.first;
                        Integer sectionNumber = pair.second;

                        if(rect.contains(x, y)) {
                            Log.v("Activity_Main", sectionNumber.toString());
                        }
                    }
                }

                return false;
            }
        });

        // Instantiating floating action button fragment
        mFabFragment = Fragment_Fab.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fabContainer, mFabFragment)
                .commit();

        // Initiating track list adapter
        mTrackListAdapter = new ExpandableList_Adapter(
                mActivity,
                mSong,
                Activity_Main.ExpandableListAdapterMode.SONG_EDITOR
        );

        // Getting track list and setting adapter
        mTrackList = (ExpandableListView) rootView.findViewById(R.id.listView_tracks);
        mTrackList.setAdapter(mTrackListAdapter);
        mTrackList.setOnGroupClickListener(new GroupClickListener());
        mTrackList.setOnChildClickListener(new ChildClickListener());
        mTrackList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mSections.remove(position);
                mTrackListAdapter.notifyDataSetChanged();
                return false;
            }
        });

        // Setting drag listeners
        mSongMap.setOnDragListener(new DragEventListener_Fragment_Song(false)); // Can't receive drops, sends view back to parent
        mTrackList.setOnDragListener(new DragEventListener_Fragment_Song(false)); // Can't receive drops, appends view to current view

        // Setting play button listener
        mPlay = (FloatingActionButton) rootView.findViewById(R.id.play);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Section section : mSong.getSections()) {
                    ESAudio.play(section, mActivity);
                }
            }
        });

        return rootView;
    }

    public Section getSection(int groupPosition) {
        return mSections.get(groupPosition);
    }

    public Section getSection(int groupPosition, int childPosition) {
        Group group = mGroups.get(groupPosition);
        return (Section) group.getSubgroups().get(childPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
            mTrackList.deferNotifyDataSetChanged();
        }

    private class DragEventListener_Fragment_Song
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

    private class GroupClickListener
            implements ExpandableListView.OnGroupClickListener {

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            // Getting section
            if (groupPosition < mSections.size()) {
                Section section = getSection(groupPosition);

                // Updating main activity container to section fragment
                mActivity.setCurrentSection(section);
                mActivity.setMode(Activity_Main.Mode.SECTION_EDITOR);
                return true;
            }
            else {
                return false;
            }
        }
    }

    private class ChildClickListener
            implements ExpandableListView.OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            // Getting section
            groupPosition -= mSections.size();
            Section section = getSection(groupPosition, childPosition);

            // Updating main activity container to section fragment
            mActivity.setCurrentSection(section);
            mActivity.setMode(Activity_Main.Mode.SECTION_EDITOR);
            return false;
        }
    }

}

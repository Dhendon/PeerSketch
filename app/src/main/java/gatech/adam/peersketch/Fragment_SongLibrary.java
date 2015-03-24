// Project list
// Select song from list view, then open song fragment

package gatech.adam.peersketch;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.Song;

public class Fragment_SongLibrary extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayAdapter<String> projectList_adapter;
    private Activity_Main mActivity;

    // Returns new instance of projects list
    public static Fragment_SongLibrary newInstance() {
        return new Fragment_SongLibrary();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate rootView to activity_main/container and get pointer
        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);

        // Get pointer to main activity
        mActivity = (Activity_Main) getActivity();

        // Setting data for adapter from SongLibrary, then converting to ArrayList object
        int songLibrarySize = mActivity.mSongLibrary.getSize();
        List<String> songtTitle_list = new ArrayList<String>();

        // Adding song titles to list
        for (int i = 0 ; i < songLibrarySize; i++) {
            Song song = mActivity.mSongLibrary.getSong(i);
            songtTitle_list.add(song.getName());
        }

        // Creating array adapter
        projectList_adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item,
                R.id.list_item_textview,
                songtTitle_list
        );

        // Get pointer to list view and set adapter
        ListView listView = (ListView) rootView.findViewById(R.id.listView_projects);
        listView.setAdapter(projectList_adapter);

        // Set project list listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment to = Fragment_Song.newInstance(position);

                // Replacing main activity fragment, making pallet drawer visible
                Activity_Main main = (Activity_Main) getActivity();
                main.updateContainer(to, true);
            }
        });

        return rootView;
    }
}

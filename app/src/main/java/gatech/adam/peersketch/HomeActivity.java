package gatech.adam.peersketch;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.Section;
import data.Song;
import data.Util;


public class HomeActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, SectionEditorFragment.OnFragmentInteractionListener {

    private static String sectionName;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Button mButtonCreateSection;
    private ListView mListViewSongItems;
    private Song currentSong;
    private Context context;
    private ArrayAdapter<String> sampleAdapter;
    //static ArrayAdapter<String> sampleList;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, SectionEditorFragment.newInstance("Section1"))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
        mButtonCreateSection = (Button) findViewById(R.id.buttonNewSection_dumbUI);
        mListViewSongItems = (ListView) findViewById(R.id.listViewSections_dumbUI);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mButtonCreateSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionName = "";
                promptCreatedSectionName();
                // TODO: Return this to normal once it's working.
                if (sectionName.equals("")) { //user inputted name
                    sectionName = "New Section";
                }
                // Add section to this song

                // Go to Section Editor
                //Intent createSectionIntent = new Intent(context, SectionEditorFragment.class);
                //createSectionIntent.putExtra(Util.SECTION_NAME, sectionName);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container,
                                SectionEditorFragment.newInstance(sectionName))
                        .commit();
                //    }
            }
        });
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptCreatedSectionName() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateSectionDialogFragment();
        newFragment.show(getFragmentManager(), "createSection");
    }

    private void refreshUI() {
        // reads in list of all items on page and adds them to list.
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        mTitle = "New Section";
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container,
                        SectionEditorFragment.newInstance(sectionName))
                .commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_song_editor, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class SectionEditorFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        // private static final String ARG_NAME = "name";
        //private static final String ARG_PARAM2 = "param2";

        public static String TAG = "section-editor";
        // TODO change this to use callbacks from the DialogFragment
        private static ESFitMedia recentFitMedia;
        // TODO: Rename and change types of parameters
        private Section currentSection;
        private ListView mListView;
        private Button mFitMediaButton;
        private Button mPlayButton;
        // TODO: make these do something
        private int songBPM = Util.DEFAULT_TEMPO_BPM;
        private int songPhraseLength = Util.DEFAULT_PHRASE_LENGTH;


        //private OnFragmentInteractionListener mListener;

        public SectionEditorFragment() {
            // Required empty public constructor
        }

        // TODO: Rename and change types and number of parameters
        public static SectionEditorFragment newInstance(String name) {
            SectionEditorFragment fragment = new SectionEditorFragment();
            Bundle args = new Bundle();
            Section added = new Section(name);
            ESFitMedia dummyFitMedia = new ESFitMedia("PlaceHolder FitMedia", 0, 1, 2.25);
            added.add(dummyFitMedia, 0);
            args.putSerializable(Util.BundleKeys.SECTION, new Section(name));
            fragment.setArguments(args);
            return fragment;
        }

        public static SectionEditorFragment newInstance(Section section) {
            SectionEditorFragment fragment = new SectionEditorFragment();
            Bundle args = new Bundle();
            args.putSerializable(Util.BundleKeys.SECTION, section);
            fragment.setArguments(args);
            // TODO: add isValid check
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                currentSection = (Section) getArguments().getSerializable(Util.BundleKeys.SECTION);
            } else {
                currentSection = new Section("Section Name Goes Here");
            }
            // Sets up UI
            getActivity().setTitle(currentSection.getName());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_section_editor, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mListView = (ListView) getActivity().findViewById(R.id.listViewSectionItems_dumbUI);
            List<ESFitMedia> fitMedias = currentSection.getFitMedias();
            String[] sectionItems = new String[fitMedias.size()];
            // Crappy parsing of strings
            for (int i = 0; i < fitMedias.size(); i++) {
                sectionItems[i] = fitMedias.get(i).toString();
            }
            if (getActivity().getApplicationContext() == null) {
                Log.i(TAG, "null context - onActivityCreated");
            }
            ArrayAdapter<String> sectionItemsAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    android.R.layout.simple_spinner_item, sectionItems);
            mListView.setAdapter(sectionItemsAdapter);
            mFitMediaButton = (Button) getActivity().findViewById(R.id.buttonFitMedia_dumbUI);
            mPlayButton = (Button) getActivity().findViewById(R.id.buttonPlaySection_dumbUI);
            mFitMediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptCreateFitMedia();
                    currentSection.add(recentFitMedia, 0);
                }
            });
            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ESAudio.play(currentSection.getFitMedias().get(0),
                            getActivity().getApplicationContext(), songBPM, songPhraseLength);
                }
            });
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO: trigger editing instead of just playing here.
                    ESFitMedia selectedFitMedia = currentSection.getFitMedias().get(position);
                    ESAudio.play(selectedFitMedia, getActivity().getApplicationContext(),
                            songBPM, songPhraseLength);
                }
            });
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            /*try {
                mListener = (OnFragmentInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnFragmentInteractionListener");
            }*/
            //mListView = (ListView)getActivity().findViewById(R.id.listViewSectionItems_dumbUI);
        /*List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        String[] sectionItems = new String[fitMedias.size()];
        // Crappy parsing of strings
        for (int i = 0; i<fitMedias.size(); i++) {
            sectionItems[i] = fitMedias.get(i).toString();
        }*/
            if (getActivity().getApplicationContext() == null) {
                Log.i(TAG, "null context - onActivityCreated");
            }
        /*ArrayAdapter<String> sectionItemsAdapter = new ArrayAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, sectionItems);
        mListView.setAdapter(sectionItemsAdapter);*/
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }

        private void promptCreateFitMedia() {
            // TODO: write toStrings for all the methods
            DialogFragment newFragment = new CreateFitMediaDialogFragment();
            newFragment.show(getFragmentManager(), "createFitMedia");
            refreshUI();
        }

        public void refreshUI() {
            mListView = (ListView) getActivity().findViewById(R.id.listViewSectionItems_dumbUI);
            List<ESFitMedia> fitMedias = currentSection.getFitMedias();
            String[] sectionItems = new String[fitMedias.size()];
            // Crappy parsing of strings
            for (int i = 0; i < fitMedias.size(); i++) {
                sectionItems[i] = fitMedias.get(i).toString();
            }
            if (getActivity().getApplicationContext() == null) {
                Log.i(TAG, "null context - refresh UI");
            }
            ArrayAdapter<String> sectionItemsAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    android.R.layout.simple_spinner_item, sectionItems);
            mListView.setAdapter(sectionItemsAdapter);
        }

        public static class CreateFitMediaDialogFragment extends DialogFragment {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View prompt = inflater.inflate(R.layout.fragment_fitmedia_dialog, null);
                builder.setTitle("Create New FitMedia")
                        .setPositiveButton("Make it so!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Spinner samplesSpinner = (Spinner) prompt.findViewById(
                                        R.id.spinnerSamples);
                                EditText startEditText = (EditText) prompt.findViewById(
                                        R.id.editTextStartLocation);
                                EditText endEditText = (EditText) prompt.findViewById(
                                        R.id.editTextEndLocation);
                                String val = getActivity().getApplicationContext().toString();
                                Log.i(SectionEditorFragment.TAG, "FitMediaDialog-context=" + val);
                                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                        android.R.layout.simple_spinner_item, Util.DEFAULT_SAMPLES);
                                samplesSpinner.setAdapter(adapter);
                                String rawStartLocation = startEditText.getText().toString();
                                String rawEndLocation = endEditText.getText().toString();
                                double start = Double.parseDouble(rawStartLocation);
                                double end = Double.parseDouble(rawEndLocation);

                                String sampleName = samplesSpinner.getSelectedItem().toString();
                                recentFitMedia = new ESFitMedia(sampleName, 0, start, end);
                            }
                        })
                        .setNegativeButton("Forget it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .setView(prompt);

                // Create the AlertDialog object and return it
                return builder.create();
            }
        }
    }

    public static class CreateSectionDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            EditText inputFake = new EditText(getActivity().getApplicationContext());
            inputFake.setTextColor(Color.BLACK);
            final EditText input = inputFake;
            builder.setMessage("What's the name of the section?")
                    .setPositiveButton("Create!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sectionName = input.getText().toString();
                            // TODO: fix this getting cleared.
                        }
                    })
                    .setNegativeButton("Nevermind.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .setView(input);
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}

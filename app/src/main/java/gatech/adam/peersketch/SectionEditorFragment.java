package gatech.adam.peersketch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import data.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SectionEditorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SectionEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionEditorFragment extends Fragment {
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
    private OnFragmentInteractionListener mListener;

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        ArrayAdapter<String> sectionItemsAdapter = new ArrayAdapter(getActivity().getApplicationContext(),
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
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        mListener = null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public static class CreateFitMediaDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View prompt = inflater.inflate(R.layout.fragment_fitmedia_dialog, null);
            builder.setMessage("Create New FitMedia")
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

package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import data.Section;
import data.Song;
import data.Util;

/**
 * Created by hendon on 4/15/15.
 */
public class CreateSectionDialogFragment extends DialogFragment {
    private static Song currentSong;
    public final String TAG = "create-section-dialog";
    private SectionDialogListener mListener;

    public static CreateSectionDialogFragment newInstance(Song currentSong) {
        CreateSectionDialogFragment fragment = new CreateSectionDialogFragment();

        Bundle songBundle = new Bundle();
        songBundle.putSerializable(Util.BundleKeys.SONG, currentSong);
        fragment.setArguments(songBundle);

        return fragment;
    }

    private static boolean sectionNameTaken(String name) {
        for (Section section : currentSong.getSections()) {
            if (section.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SectionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SectionDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentSong = (Song) getArguments().getSerializable(Util.BundleKeys.SONG);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText inputFake = new EditText(getActivity().getApplicationContext());
        inputFake.setTextColor(Color.BLACK);
        final EditText input = inputFake;
        builder.setMessage("What's the name of the section?")
                .setPositiveButton("Create!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String sectionName = input.getText().toString();
                        if (!sectionNameTaken(sectionName)) {
                            int trackNumber = currentSong.getSections().size() - 1;
                            Section newSection = new Section(sectionName);
                            currentSong.addSection(newSection, trackNumber);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Sorry! Section name taken, please try again with a new name",
                                    Toast.LENGTH_LONG).show();
                            Log.i(TAG, "Unable to create new section with name: "
                                    + sectionName + " - Duplicate Name");
                        }
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

    public interface SectionDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String sectionName);
    }
}
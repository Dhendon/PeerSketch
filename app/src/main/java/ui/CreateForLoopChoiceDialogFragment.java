package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import gatech.adam.peersketch.R;
import gatech.adam.peersketch.SectionEditorActivity;

/**
 * Created by hendon on 4/14/15.
 */
public class CreateForLoopChoiceDialogFragment extends DialogFragment {
    public final String TAG = "create-forloop-choice-dialog";
    private ForLoopChoiceDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ForLoopChoiceDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ForLoopChoiceDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Null Root View is acceptable here because AlertDialog replaces it anyway.
        final View prompt = inflater.inflate(R.layout.fragment_forloop_choice_dialog, null);
        final RadioButton fitMediaRadioButton = (RadioButton) prompt.findViewById(R.id.radioButtonFitMedia);
        final RadioButton makeBeatRadioButton = (RadioButton) prompt.findViewById(R.id.radioButtonMakeBeat);
        final RadioButton forLoopRadioButton = (RadioButton) prompt.findViewById(R.id.radioButtonForLoop);
        fitMediaRadioButton.setChecked(true);
        builder.setTitle("Add Function to ForLoop")
                .setPositiveButton("Presto!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int choiceID = SectionEditorActivity.RadioButtonIndices.UNASSIGNED;
                        if (fitMediaRadioButton.isChecked()) {
                            choiceID = SectionEditorActivity.RadioButtonIndices.FITMEDIA;
                        } else if (makeBeatRadioButton.isChecked()) {
                            choiceID = SectionEditorActivity.RadioButtonIndices.MAKEBEAT;
                        } else if (forLoopRadioButton.isChecked()) {
                            choiceID = SectionEditorActivity.RadioButtonIndices.FORLOOP;
                        }
                        mListener.onDialogPositiveClick(CreateForLoopChoiceDialogFragment.this,
                                choiceID);
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

    public interface ForLoopChoiceDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int choiceID);
    }
}



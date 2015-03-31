package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import data.ForLoop;
import gatech.adam.peersketch.R;

public class CreateForLoopDialogFragment extends DialogFragment {
    public final String TAG = "create-forloop-dialog";
    private ForLoopDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ForLoopDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ForLoopDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Null Root View is acceptable here because AlertDialog replaces it anyway.
        final View prompt = inflater.inflate(R.layout.fragment_forloop_dialog, null);
        // Setup UI elements.
        final EditText startEditText = (EditText) prompt.findViewById(
                R.id.editTextStartLocation);
        final EditText endEditText = (EditText) prompt.findViewById(
                R.id.editTextEndLocation);
        final EditText stepSizeEditText = (EditText) prompt.findViewById(R.id.editTextStepSize);
        final RadioButton incrementRadioButton = (RadioButton)
                prompt.findViewById((R.id.radioButtonIncrement));
        final EditText variableEditText = (EditText) prompt.findViewById(R.id.editTextVariable);
        builder.setTitle("Create New FitMedia")
                .setPositiveButton("Make it so!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String rawStartLocation = startEditText.getText().toString();
                        String rawEndLocation = endEditText.getText().toString();
                        String rawStepSize = stepSizeEditText.getText().toString();
                        String variable = variableEditText.getText().toString();
                        boolean isIncrementing = incrementRadioButton.isChecked();
                        if (!rawStartLocation.equals("") && !rawEndLocation.equals("")
                                && !rawStepSize.equals("") && !variable.equals("")) {
                            int start = Integer.parseInt(rawStartLocation);
                            int end = Integer.parseInt(rawEndLocation);
                            String directionString = isIncrementing ? "+" : "-";
                            // TODO(hendon): Turn this into ToString for ForLoop.
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Made for Loop! --> for " + variable + " in range("
                                            + rawStartLocation + "," + rawEndLocation + ","
                                            + directionString + rawStepSize + ")",
                                    Toast.LENGTH_SHORT).show();
                            int stepSize = Integer.parseInt(rawStepSize);
                            // TODO: Deal with the case where they make stepSize negative
                            ForLoop value = new ForLoop(start, stepSize, end, variable);
                            mListener.onDialogPositiveClick(CreateForLoopDialogFragment.this, value);

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Invalid parameters, please try again!",
                                    Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Unable to create for loop");
                        }
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

    public interface ForLoopDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, ForLoop value);
    }
}

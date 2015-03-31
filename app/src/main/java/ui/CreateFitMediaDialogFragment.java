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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import data.ESFitMedia;
import data.Util;
import gatech.adam.peersketch.R;

public class CreateFitMediaDialogFragment extends DialogFragment {
    public final String TAG = "create-fitmedia-dialog";
    private FitMediaDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (FitMediaDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement FitMediaDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Null Root View is acceptable here because AlertDialog replaces it anyway.
        final View prompt = inflater.inflate(R.layout.fragment_fitmedia_dialog, null);
        // Setup UI elements
        final Spinner samplesSpinner = (Spinner) prompt.findViewById(
                R.id.spinnerSamples);
        //ArrayAdapter<CharSequence> adapter =
        //        ArrayAdapter.createFromResource(getActivity().getBaseContext(),
        //                R.array.default_samples, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(Util.DEFAULT_SAMPLES);
        samplesSpinner.setAdapter(adapter);
        // TODO add in the ability to add custom samples -- in final UI.
        final EditText startEditText = (EditText) prompt.findViewById(
                R.id.editTextStartLocation);
        final EditText endEditText = (EditText) prompt.findViewById(
                R.id.editTextEndLocation);

        builder.setTitle("Create New FitMedia")
                .setPositiveButton("Make it so!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String rawStartLocation = startEditText.getText().toString();
                        String rawEndLocation = endEditText.getText().toString();
                        String sampleName = samplesSpinner.getSelectedItem().toString();
                        if (!rawStartLocation.equals("") && !rawEndLocation.equals("")
                                && !sampleName.equals("")) {
                            double start = Double.parseDouble(rawStartLocation);
                            double end = Double.parseDouble(rawEndLocation);
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Made FitMedia! (" + rawStartLocation + ", " + rawEndLocation + ")",
                                    Toast.LENGTH_SHORT).show();
                            ESFitMedia value = new ESFitMedia(sampleName, -1, start, end);
                            mListener.onDialogPositiveClick(CreateFitMediaDialogFragment.this, value);

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Invalid parameters", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Unable to create FitMedia");
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

    public interface FitMediaDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, ESFitMedia value);
    }
}

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

import data.ESSetEffect;
import data.Util;
import gatech.adam.peersketch.R;

public class CreateSetEffectDialogFragment extends DialogFragment {
    public final String TAG = "create-seteffect-dialog";
    private SetEffectDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SetEffectDialogListener) activity;
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
        final View prompt = inflater.inflate(R.layout.fragment_seteffect_dialog, null);
        // Setup UI elements
        final Spinner effectsSpinner = (Spinner) prompt.findViewById(
                R.id.spinnerEffects);
        //ArrayAdapter<CharSequence> adapter =
        //        ArrayAdapter.createFromResource(getActivity().getBaseContext(),
        //                R.array.default_samples, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(Util.EFFECT_NAMES);
        effectsSpinner.setAdapter(adapter);
        // TODO add in the ability to add custom samples -- in final UI.
        final EditText startEditText = (EditText) prompt.findViewById(
                R.id.editTextStartLocation);
        final EditText endEditText = (EditText) prompt.findViewById(
                R.id.editTextEndLocation);
        final EditText amountEditText = (EditText) prompt.findViewById(R.id.editTextAmount);

        builder.setTitle("Add new SetEffect")
                .setPositiveButton("Presto!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String rawStartLocation = startEditText.getText().toString();
                        String rawEndLocation = endEditText.getText().toString();
                        String rawAmount = amountEditText.getText().toString();
                        String effectName = effectsSpinner.getSelectedItem().toString();
                        int effectID = effectsSpinner.getSelectedItemPosition();
                        if (!rawStartLocation.equals("") && !rawEndLocation.equals("")
                                && !effectName.equals("")) {
                            double start = Double.parseDouble(rawStartLocation);
                            double end = Double.parseDouble(rawEndLocation);
                            double amount = Double.parseDouble(rawAmount);
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Made SetEffect! (" + rawStartLocation + ", " + rawEndLocation + ") " + rawAmount + " " + effectName,
                                    Toast.LENGTH_SHORT).show();
                            ESSetEffect value = new ESSetEffect(effectID, start, end, amount);
                            //ESFitMedia value = new ESFitMedia(effectName, -1, start, end);
                            mListener.onDialogPositiveClick(CreateSetEffectDialogFragment.this, value);

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

    public interface SetEffectDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, ESSetEffect value);
    }
}

package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import data.ESFitMedia;
import data.Util;
import gatech.adam.peersketch.R;

/**
 * Created by hendon on 4/13/15.
 */
public class CreateForLoopFitmediaDialogFragment extends DialogFragment {
    public final String TAG = "create-fitmedia-dialog";
    private ForLoopFitMediaDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ForLoopFitMediaDialogListener) activity;
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
        final LinearLayout startVariableLayout = (LinearLayout) prompt.findViewById(R.id.layoutStartVariable);
        final LinearLayout endVariableLayout = (LinearLayout) prompt.findViewById(R.id.layoutEndVariable);


        // Variable stuff
        final EditText startVariableEditText = (EditText) prompt.findViewById(R.id.editTextStartVariable);
        final Spinner startVariableOperandSpinner = (Spinner) prompt.findViewById(R.id.spinnerStartOperand);
        final EditText startVariableAmountEditText = (EditText) prompt.findViewById(R.id.editTextStartAmount);
        final EditText endVariableEditText = (EditText) prompt.findViewById(R.id.editTextEndVariable);
        final Spinner endVariableOperandSpinner = (Spinner) prompt.findViewById(R.id.spinnerEndOperand);
        final EditText endVariableAmountEditText = (EditText) prompt.findViewById(R.id.editTextEndAmount);
        ArrayAdapter<String> operandAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item);
        operandAdapter.addAll(Util.OPERANDS);
        startVariableOperandSpinner.setAdapter(operandAdapter);
        endVariableOperandSpinner.setAdapter(operandAdapter);

        startVariableLayout.setVisibility(View.VISIBLE);
        endVariableLayout.setVisibility(View.VISIBLE);
        startEditText.setVisibility(View.GONE);
        endEditText.setVisibility(View.GONE);
        Toast.makeText(getActivity().getApplicationContext(),
                "Please enter variable name and (optional) amount to modify it!",
                Toast.LENGTH_SHORT).show();
        builder.setTitle("Create New FitMedia")
                .setPositiveButton("Make it so!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String sampleName = samplesSpinner.getSelectedItem().toString();
                        // Deal with variables
                        String startVariable = startVariableEditText.getText().toString();
                        String endVariable = endVariableEditText.getText().toString();
                        // TODO: Restrict length of the operand to just one character.
                        char startOperand = Util.OPERANDS[startVariableOperandSpinner.getSelectedItemPosition()].charAt(0);
                        char endOperand = Util.OPERANDS[endVariableOperandSpinner.getSelectedItemPosition()].charAt(0);
                        double startAmount = 0;
                        String startAmountRaw = startVariableAmountEditText.getText().toString();
                        if (startOperand != 'n' && !startAmountRaw.equals("")) {
                            startAmount = Double.parseDouble(startAmountRaw);
                        }
                        double endAmount = 0;
                        String endAmountRaw = endVariableAmountEditText.getText().toString();
                        if (endOperand != 'n' && !endAmountRaw.equals("")) {
                            endAmount = Double.parseDouble(endAmountRaw);
                        }
                        // TODO: Ignore values if there is no operand
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Attempting to create FitMedia!",
                                Toast.LENGTH_SHORT).show();
                        ESFitMedia value = new ESFitMedia(sampleName, -1, startVariable,
                                startOperand, startAmount, endVariable, endOperand, endAmount);
                        mListener.onDialogPositiveClick(CreateForLoopFitmediaDialogFragment.this, value);
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

    public interface ForLoopFitMediaDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, ESFitMedia value);
    }
}



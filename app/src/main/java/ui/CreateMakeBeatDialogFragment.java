package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import data.ESMakeBeat;
import data.Section;
import data.Util;
import gatech.adam.peersketch.R;

/**
 * Created by hendon on 2/12/15.
 */
public class CreateMakeBeatDialogFragment extends DialogFragment {

    public final String TAG = "create-makebeat-dialog";
    private MakeBeatDialogListener mListener;

    public static CreateMakeBeatDialogFragment newInstance(Section currentSection) {
        CreateMakeBeatDialogFragment fragment = new CreateMakeBeatDialogFragment();

        Bundle variablesBundle = new Bundle();
        variablesBundle.putSerializable(Util.BundleKeys.SECTION, currentSection);
        fragment.setArguments(variablesBundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MakeBeatDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement MakeBeatDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Null Root View is acceptable here because AlertDialog replaces it anyway.
        final View prompt = inflater.inflate(R.layout.fragment_makebeat_dialog, null);
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
        final TextView beatPatternTextView = (TextView) prompt.findViewById(
                R.id.textViewUserBeatPattern);
        final Button zeroButton = (Button) prompt.findViewById(R.id.buttonZero);
        final Button plusSignButton = (Button) prompt.findViewById(R.id.buttonPlusSign);
        final Button minusSignButton = (Button) prompt.findViewById(R.id.buttonMinusSign);
        final Button deleteButton = (Button) prompt.findViewById(R.id.buttonDelete);
        // Disable button before it can be used.
        plusSignButton.setEnabled(false);
        plusSignButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        zeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String original = beatPatternTextView.getText().toString();
                beatPatternTextView.setText(original + "0");
                if (!plusSignButton.isEnabled()) {
                    plusSignButton.setEnabled(true);
                    plusSignButton.getBackground().setColorFilter(null);
                }
            }
        });
        plusSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String original = beatPatternTextView.getText().toString();
                beatPatternTextView.setText(original + "+");
            }
        });
        minusSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String original = beatPatternTextView.getText().toString();
                beatPatternTextView.setText(original + "-");
                if (plusSignButton.isEnabled()) {
                    plusSignButton.setEnabled(false);
                    plusSignButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String original = beatPatternTextView.getText().toString();
                if (original.length() > 1) {
                    char removed = original.charAt(original.length()-1);
                    char prev = original.charAt(original.length() - 2);
                    // Case where you delete something that enabled plusSignButton
                    if (removed == '0' && prev == '-') {
                        plusSignButton.setEnabled(false);
                        plusSignButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    } else if (removed == '-' && prev != '-') {
                        plusSignButton.setEnabled(true);
                        plusSignButton.getBackground().setColorFilter(null);
                    }
                }
                if (original.length() > 0) {
                    original = original.substring(0, original.length()-1);
                }
                beatPatternTextView.setText(original);
            }
        });
        builder.setTitle("Create New MakeBeat")
                .setPositiveButton("Make it happen, Captain!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String rawStartLocation = startEditText.getText().toString();
                        String sampleName = samplesSpinner.getSelectedItem().toString();
                        String beatPattern = beatPatternTextView.getText().toString();
                        if (!rawStartLocation.equals("") && !beatPattern.equals("")
                                && !sampleName.equals("")) {
                            double start = Double.parseDouble(rawStartLocation);
                            //double end = Double.parseDouble(rawEndLocation);
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Made MakeBeat! (" + rawStartLocation + ", "
                                            + beatPattern + ")",
                                    Toast.LENGTH_SHORT).show();
                            ESMakeBeat value = new ESMakeBeat(sampleName, -1, start, beatPattern);
                            mListener.onDialogPositiveClick(CreateMakeBeatDialogFragment.this,
                                    value);

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Invalid parameters", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Unable to create MakeBeat");
                        }
                    }
                })
                .setNegativeButton("Not today...", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setView(prompt);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface MakeBeatDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, ESMakeBeat value);
    }

}

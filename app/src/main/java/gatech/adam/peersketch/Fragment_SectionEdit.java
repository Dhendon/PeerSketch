package gatech.adam.peersketch;

import android.app.DialogFragment;
import android.content.ClipData;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import data.ESFitMedia;
import data.Section;
import ui.CreateMakeBeatDialogFragment;

public class Fragment_SectionEdit extends Fragment {

    private static final String SECTION_POSITION = "section";
    private int mSectionPosition;
    private Section mSection;
    private LinearLayout mBlockContainer;
    private FloatingActionButton mFab;
    private Activity_Main mActivity;
    private SurfaceView mSongMap;

    public static Fragment_SectionEdit newInstance(Section section) {
        Fragment_SectionEdit fragment = new Fragment_SectionEdit();
        fragment.mSection = section;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (Activity_Main) getActivity();
        if (getArguments() != null) {
            mSectionPosition = getArguments().getInt(SECTION_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate rootView to activity_main/container and get pointer
        View rootView = inflater.inflate(R.layout.fragment_section_edit, container, false);

        // Setting title
        TextView tv = (TextView) rootView.findViewById(R.id.textView_sectionTitle);
        tv.setText(mSection.getName());

        // Getting handle to block container
        mBlockContainer = (LinearLayout) rootView.findViewById(R.id.linearLayou_functionBlocks);

        // Getting handle to minimap
        mSongMap = (SurfaceView) rootView.findViewById(R.id.surfaceView_songMap);

        // Creating fit media blocks
        List<ESFitMedia> fitMedias = mSection.getFitMedias();
        for (int i = 0; i < fitMedias.size(); i++) {
            createFitMediaBlock(fitMedias.get(i));
        }

        // Setting drag listener
        mSongMap.setOnDragListener(new DragEventListener_Fragment_Section(false)); // Can't receive drops, sends view back to parent

        return rootView;
    }

    private void promptMakeBeatDialogAndWrite() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateMakeBeatDialogFragment();
        newFragment.show(getFragmentManager(), "createMakeBeat");
    }

    public void createFitMediaBlock(ESFitMedia fitMedia) {
        // Creating block
        TextView block =  new TextView(mActivity);
        block.setText(fitMedia.toString());
        block.setBackgroundResource(R.drawable.shape_rounded_corners_fitmedia);

        // Styling block
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(64, 12, 24, 12);
        block.setLayoutParams(params);
        block.setTextSize(18);
        block.setElevation(5);
        block.setPadding(24, 24, 24, 24);

        // Adding block to view
        mBlockContainer.addView(block, 0);
    }

    private class DragEventListener_Fragment_Section
            implements View.OnDragListener {

        private boolean mCanReceive; // Stores whether this listener accepts drop events

        public DragEventListener_Fragment_Section(boolean canReceive) {
            mCanReceive = canReceive;
        }

        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction(); // Getting current drag action

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Closing main activity drawers
                    mActivity.closeDrawers();
                    break;
                case DragEvent.ACTION_DROP:
                    // Getting handle to view and view parent
                    //View view = (View) event.getLocalState();
                    //ViewGroup owner = (ViewGroup) view.getParent();

                    // If can receive drops, remove from parent and append to current view
                    if (mCanReceive) {
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        String text = item.coerceToText(mActivity).toString();
                        // TODO:

                    }
                    // If this can't receive drops, return to parent view
                    else {
                        mActivity.openPalletDrawer();
                    }
                    break;
            }

            return true;
        }
    }

}

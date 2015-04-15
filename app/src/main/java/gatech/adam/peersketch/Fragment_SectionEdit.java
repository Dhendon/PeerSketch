package gatech.adam.peersketch;

import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.ESMakeBeat;
import data.ForLoop;
import data.Section;

public class Fragment_SectionEdit extends Fragment {

    private static final String SECTION_POSITION = "section";
    public LinearLayout mBlockContainer;
    private int mSectionPosition;
    private Section currentSection;
    private FloatingActionButton mFab;
    private Activity_Main mActivity;
    private SurfaceView mSongMap;
    private Fragment_Fab mFabFragment;

    public static Fragment_SectionEdit newInstance(Section section) {
        Fragment_SectionEdit fragment = new Fragment_SectionEdit();
        fragment.currentSection = section;
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
        final View rootView = inflater.inflate(R.layout.fragment_section_edit, container, false);

        // Setting title
        TextView tv = (TextView) rootView.findViewById(R.id.textView_sectionTitle);
        tv.setText(currentSection.getName());

        // Getting handle to block container
        mBlockContainer = (LinearLayout) rootView.findViewById(R.id.linearLayou_functionBlocks);

        // Getting handle to minimap
        mSongMap = (SurfaceView) rootView.findViewById(R.id.surfaceView_songMap);

        mSongMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mFabFragment = Fragment_Fab.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fabContainer, mFabFragment)
                .commit();

        // Creating fit media blocks
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        List<ESMakeBeat> makeBeats = currentSection.getMakeBeats();
        List<ForLoop> forLoops = currentSection.getForLoops();
        for (ESFitMedia fitMedia : fitMedias) {
            createFitMediaBlock(fitMedia);
        }
        for (ESMakeBeat makeBeat : makeBeats) {
            createMakeBeatBlock(makeBeat);
        }
        for (ForLoop forLoop : forLoops) {
            createForLoopBlock(forLoop);
        }

        // Setting drag listener
        mSongMap.setOnDragListener(new DragEventListener_Fragment_Section(false)); // Can't receive drops, sends view back to parent

        return rootView;
    }

    public void createFitMediaBlock(final ESFitMedia fitMedia) {
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

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ESAudio.play(fitMedia, mActivity.getApplicationContext(),
                        mActivity.getSong().getTempoBPM(), mActivity.getSong().getPhraseLength());
            }
        });

        block.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentSection.getFitMedias().remove(fitMedia);
                mBlockContainer.postInvalidate();
                return true;
            }
        });

        // Adding block to view
        mBlockContainer.addView(block, 0);

    }

    public void createMakeBeatBlock(final ESMakeBeat makeBeat) {
        // Creating block
        TextView block =  new TextView(mActivity);
        block.setText(makeBeat.toString());
        block.setBackgroundResource(R.drawable.shape_rounded_corners_makebeat);

        // Styling block
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(64, 12, 24, 12);
        block.setLayoutParams(params);
        block.setTextSize(18);
        block.setElevation(5);
        block.setPadding(24, 24, 24, 24);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ESAudio.play(makeBeat,mActivity.getApplicationContext(),
                        mActivity.getSong().getTempoBPM(), mActivity.getSong().getPhraseLength());
            }
        });

        block.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentSection.getMakeBeats().remove(makeBeat);
                mBlockContainer.invalidate();
                return true;
            }
        });
        // Adding block to view
        mBlockContainer.addView(block, 0);
    }

    public void createForLoopBlock(final ForLoop forLoop) {
        // Creating block
        TextView block =  new TextView(mActivity);
        block.setText(forLoop.toString());
        block.setBackgroundResource(R.drawable.shape_rounded_corners_forloop);

        // Styling block
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(64, 12, 24, 12);
        block.setLayoutParams(params);
        block.setTextSize(18);
        block.setElevation(5);
        block.setPadding(24, 24, 24, 24);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.promptForLoopChoiceDialog();
            }
        });

        block.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentSection.getForLoops().remove(forLoop);
                mBlockContainer.invalidate();
                return true;
            }
        });
        // Adding block to view
        mBlockContainer.addView(block, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlockContainer.invalidate();
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

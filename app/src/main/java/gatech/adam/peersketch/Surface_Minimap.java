package gatech.adam.peersketch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import data.Section;

import static android.graphics.Color.*;
import static android.view.MotionEvent.ACTION_UP;


public class Surface_Minimap extends SurfaceView implements SurfaceHolder.Callback {

    private Paint backgroundPaint;
    private Paint rectPaint;
    private Rect  newRect;

    // Fixed values
    // TODO: Match these with UI design
    public int padding = 20;
    public int rectMargin = 5;

    public int frameWidth;
    public int frameHeight;

    // Values computed in methods below
    public Double mMeasureWidth;
    public int numSections;

    public int rectHeight;
    public int topCoord;
    public int bottomCoord;

    private Double x1 = 0.0;
    private Double x2 = 0.0;
    private int y1 = 0;
    private int y2 = 0;

    List<Section> mSections;
    List<Pair<Rect, Integer>> mRectanglePairs = new ArrayList<>();
    public int sectionNum;
    public List<Pair<Double,Double>> mMeasures;
    public int[] colors = {
            getResources().getColor(R.color.orange),
            getResources().getColor(R.color.blueDark),
            getResources().getColor(R.color.cyan),
            getResources().getColor(R.color.yellow)
    };

    // Constructor
    public Surface_Minimap(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);

        frameWidth = context.getResources().getDisplayMetrics().widthPixels;
        frameHeight = 635;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(context.getResources().getColor(R.color.brown));   // Sets background color

        // Getting sections of current song
        Activity_Main mActivity = (Activity_Main) getContext();
        mSections = mActivity.getSong().getSections();

        // Adding sections' active measures to mMeasures
        mMeasures = new ArrayList<Pair<Double, Double>>();
        for (Section section : mSections) {
            mMeasures.addAll(section.calcActiveMeasures());
        }

        // Calculating measure width
        mMeasureWidth = frameWidth / calcMaxLength();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Canvas c = getHolder().lockCanvas();
        draw(c);

        getHolder().unlockCanvasAndPost(c);
    }

    public int calcSectionNum() {

        for (Section section : mSections) {
            sectionNum = section.getSectionNumber();
        }
        return sectionNum;
    }

    public int calcNumSections() {

        numSections =  mSections.size();
        return numSections;
    }

    public int makeRectHeight() {

        rectHeight = ( ( frameHeight - (2 * padding) ) / calcNumSections() ) - rectMargin;
        return rectHeight;
    }

    public int[] makeTopCoord(int sectionNm ) {
        int topCoord =  rectMargin + ( sectionNm * rectMargin ) + ( sectionNm * makeRectHeight());
        int bottomCoord = topCoord + makeRectHeight();
        int[] yCoords = {topCoord, bottomCoord};

        return yCoords;
    }

    public int makeBottomCoord() {

        bottomCoord = topCoord + makeRectHeight();
        return bottomCoord;
    }

    public Double makeMeasureWidth() {

        mMeasureWidth = frameWidth / (calcMaxLength() - 1);
        return mMeasureWidth;
    }

    public Double calcMaxLength() {
       Double maxLength = 0.0;

        for (Pair<Double, Double> measure : mMeasures) {
           Double endValue = measure.second;
           if (endValue > maxLength) {
               maxLength = endValue;
           }
       }

        return maxLength - 1;
    }

    public List<Pair<Rect, Integer>> getRectanglePairs() {
        return mRectanglePairs;
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawColor(backgroundPaint.getColor());

        int counter = 0;

        if(mSections != null) {
            for (Section section : mSections) {
                List<Pair<Double, Double>> measures = section.calcActiveMeasures();

                int color = colors[counter%4];

                rectPaint = new Paint();
                rectPaint.setColor(color);

                for (Pair<Double, Double> measure : measures) {
                    x1 = measure.first;
                    x2 = measure.second;

                    x1 = (x1 - 1) * mMeasureWidth;
                    x2 = (x2 - 1) * mMeasureWidth;

                    y1 = makeTopCoord(section.getSectionNumber())[0];
                    y2 = makeTopCoord(section.getSectionNumber())[1];

                    int l = (int) x1.doubleValue();
                    int r = (int) x2.doubleValue();

                    // draw rectangle
                    newRect = new Rect(l, y1, r, y2);
                    canvas.drawRect(newRect, rectPaint);

                    // Adding rect to rectangle list
                    mRectanglePairs.add(new Pair<Rect, Integer>(newRect, new Integer(section.getSectionNumber())));
                }

                // Updating counter
                counter++;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}



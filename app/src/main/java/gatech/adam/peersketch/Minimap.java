package gatech.adam.peersketch;

/**
 * Created by mmadaio on 1/15/15.
 */


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.Section;
import data.Song;
import data.Util;

import static android.graphics.Color.*;


public class Minimap extends SurfaceView implements SurfaceHolder.Callback {

    private Paint backgroundPaint;
    private Paint rectPaint;
    private Paint textPaint;
    private Rect  newRect;

    public List<Pair<Double, Double>> measures;

    // TODO: Get start and endMeasure values for each Section
    // TODO: Compute maxLength from list of measures
    // TODO: Get numSections
    public int startMeasure;
    public int endMeasure;
    public int maxLength = 9;
    public int numSections;
    public int priorRects; // Counter that tracks how many previous rects are drawn before the current one;
    // TODO: Find a better way to compute priorRects


    // Fixed values
    public int padding = 10;
    public int rectMargin = 5;

    // TODO: get these two the parent_width and parent_height from the XML layout file
    public int frameWidth = 705;
    public int frameHeight = 330;


    // Values computed in methods below
    public int measureWidth;
    public int rectStart;
    public int rectEnd;

    public int rectHeight;
    public int topCoord;
    public int bottomCoord;

    private int x1;
    private int x2;
    private int y1;
    private int y2;

    List<Section> sections;
    public int sectionNum;
    public List<Pair<Double,Double>> activeMeasures;



    public Minimap (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(BLACK);   // Sets background color
    }


    public void surfaceCreated(SurfaceHolder holder) {
        Canvas c = getHolder().lockCanvas();
        draw(c);

        getHolder().unlockCanvasAndPost(c);

    }




    @Override



    public int calcSectionNum() {

        for (Section section : sections) {
            sectionNum = section.getSectionNumber();
        }
        return sectionNum;
    }

    public int calcNumSections() {

        numSections =  sections.size();
        return numSections;
    }

    public int makeRectHeight() {

        rectHeight = ( ( frameHeight - (2 * padding) ) / calcNumSections() ) - rectMargin;
        return rectHeight;
    }

    public int makeTopCoord() {
        topCoord =  rectMargin + ( calcSectionNum() * rectMargin ) + ( calcSectionNum() * makeRectHeight());
        return topCoord;
    }

    public int makeBottomCoord() {

        bottomCoord = topCoord + makeRectHeight();
        return bottomCoord;
    }

    public int makeMeasureWidth() {

        measureWidth = frameWidth / (maxLength - 1);
        return measureWidth;
    }


// TODO: Find max right value in activeMeasures List Pairs

 /*

    public int calcMaxLength(List<Pair<Double, Double>> activeMeasures) {

        maxLength = Collections.max(activeMeasures);  //How do I calculate this?
        return maxLength;
    } */





    public void draw(Canvas canvas) {

        canvas.drawColor(backgroundPaint.getColor());


        for (Section section : sections) {
            List<Pair<Double,Double>> measures = section.calcActiveMeasures();

            // different color for each section?
            rectPaint = new Paint();
            rectPaint.setColor(CYAN);

// TODO: Split measure Pairs into each Double value, and convert to int

            for (Pair<Double, Double> measure : measures) {
                int x1 = measure.getLeft();
                int x2 = measure.getRight();

                x1 = (x1 - 1) * makeMeasureWidth();
                x2 = (x2 - 1) * makeMeasureWidth();

                y1 = makeTopCoord();
                y2 = makeBottomCoord();

                // draw rectangle
                newRect = new Rect(x1, y1, x2, y2);
                canvas.drawRect(newRect, rectPaint);


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

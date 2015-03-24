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
    private Rect  newRect;


    // Fixed values
    // TODO: Match these with UI design
    public int padding = 10;
    public int rectMargin = 5;

    // TODO: get these two from the parent_width and parent_height from the XML layout file
    public int frameWidth = 705;
    public int frameHeight = 330;


    // Values computed in methods below
    public Double measureWidth;
    public Double maxLength;
    public int numSections;

    public int rectHeight;
    public int topCoord;
    public int bottomCoord;

    private Double x1 = 0.0;
    private Double x2 = 0.0;
    private int y1 = 0;
    private int y2 = 0;

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

    public Double makeMeasureWidth() {

        measureWidth = frameWidth / (calcMaxLength() - 1);
        return measureWidth;
    }


// TODO: Find max right value in activeMeasures List Pairs



    public Double calcMaxLength() {

       for (Pair<Double, Double> measure : activeMeasures) {
           Double endValue = measure.second;
           if (endValue > maxLength) {
               maxLength = endValue;
           }
       }

        return maxLength;
    }




    @Override
    public void draw(Canvas canvas) {

        canvas.drawColor(backgroundPaint.getColor());

        rectPaint = new Paint();
        rectPaint.setColor(CYAN);

//        int l = 1;
//        int r = 500;
//        int y1 = 1;
//        int y2 = 80;

//        newRect = new Rect(l, y1, r, y2);
//        canvas.drawRect(newRect, rectPaint);


        for (Section section : sections) {
            List<Pair<Double,Double>> measures = section.calcActiveMeasures();

            // TODO make different color for each section?
            rectPaint = new Paint();
            rectPaint.setColor(CYAN);

            for (Pair<Double, Double> measure : measures) {
                x1 = measure.first;
                x2 = measure.second;


                x1 = (x1 - 1) * makeMeasureWidth();
                x2 = (x2 - 1) * makeMeasureWidth();

                y1 = makeTopCoord();
                y2 = makeBottomCoord();

                int l = (int) x1.doubleValue();
                int r = (int) x2.doubleValue();

                // draw rectangle
                newRect = new Rect(l, y1, r, y2);
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

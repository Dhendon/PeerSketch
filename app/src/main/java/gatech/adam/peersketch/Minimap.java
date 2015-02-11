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
    public int startMeasure = 1;
    public int endMeasure = 9;
    public int maxLength = 9;
    public int numSections = 5;
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

    // For testing purposes only:
    private int l;
    private int t;
    private int r;
    private int b;





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
    public void draw(Canvas canvas) {

        canvas.drawColor(backgroundPaint.getColor());



        // Each rect is created here:
        // TODO: Create a function to iterate across the rects and compute start/end/top/bottom values for each, then drawRect for each new Rect in that array, for numSections of them

        l = makeRectStart();
        t = makeTopCoord();
        r = makeRectEnd();
        b = makeBottomCoord();

        rectPaint = new Paint();
        newRect = new Rect(l, t, r, b);

        rectPaint.setColor(CYAN);
        canvas.drawRect(newRect, rectPaint);


        // Fixed rect for testing purposes
        rectPaint.setColor(GREEN);
        canvas.drawRect(305, 175, 600, 255, rectPaint );

        // Text for debugging
        textPaint = new Paint();
        textPaint.setColor(WHITE);
        textPaint.setTextSize(46);

        canvas.drawText( String.valueOf(makeMeasureWidth() ), 100, 200, textPaint);
        canvas.drawText( String.valueOf(makeRectEnd() ), 80, 300, textPaint);

    }




    public int makeMeasureWidth() {

        measureWidth = frameWidth / (maxLength - 1);

        return measureWidth;

    }



    public int makeRectStart() {

        rectStart = (startMeasure - 1) * makeMeasureWidth();

        return rectStart;
    }



    public int makeRectEnd() {

        rectEnd = (endMeasure - 1) * measureWidth;

        return rectEnd;
    }



    public int makeRectHeight() {

        rectHeight = ((frameHeight - 2 * padding) / numSections) - rectMargin;

        return rectHeight;
    }


    public int makeTopCoord() {

        priorRects = 0; // Just for testing - there will need to be a better way to increment this or a better method for doing it.
        topCoord =  rectMargin + ( priorRects * rectMargin ) + ( priorRects * makeRectHeight());

        return topCoord;
    }




    public int makeBottomCoord() {

        bottomCoord = topCoord + makeRectHeight();

        return bottomCoord;
    }





     /*
    public int getMeasures() {


        List measures = Section.calcActiveMeasures();

    }


    private void songInfo() {
        // Get the number of sections (numSections)
        // Get the array of sections
        // Compute the maxVal of measures to find maxLength
    }

    */


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }


}

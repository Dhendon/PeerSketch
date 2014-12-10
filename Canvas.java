
// In main View:

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


    private final Paint paint = new Paint();
    
    Paint.setColor(0); // I don't think this is right. maybe paint.setColor(0)?

    public int cHeight = 300px;  // Fixed value or 1/3 the max_height of the screen?
	public int cWidth = maxWidth; 


	public void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        canvas.drawBitmap(cHeight, cWidth, paint);
	    }









// Background View (or CanvasView?)

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;



public class BackgroundView extends View {

    private ArrayList<Object> Rects = new ArrayList<Object>();   // Pretty sure Object is the wrong thing to call this, but not sure what the right thing is


    public BackgroundView(Context context) {    
        setFocusable(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {

    	private final Paint rPaint = new Paint();   // Need to iterate across Sections to find their color value and set rPaint = to that

        for (int i = 0; i < Rects.length; i++) {

            // Draw one rect
            canvas.drawRect(Rect[i], Rect[i].rPaint);
        }
    }








public static final Creator<Rect> Rects() {   //I have that ArrayList Rects above, but not sure if this should be an ArrayList instead...

// Constructor for Rect class

	public int	bottom;
	public int	left;	
	public int	right;	
	public int	top;


	for i=0; i<Sections.length; i++ {
		getRectangleBoundsFromSection(Section section); // This should return a 4 int array, which are the 4 values for sectionBottom, Left, Right, and Top, to be assigned below
	}

	this.bottom = sectionBottom;   // the parameters of each instance of the Rect class. Setting each instance's values to the specific sectionBottom as defined below
	this.left = sectionLeft;
	this.right = sectionRight;
	this.top = sectionTop;


	for i=0; i<numSections; i++ {   // for each of the sections, creates a new Rect in the ArrayList Rects with the parameters specified in getRectangleBoundsFromSection
		ArrayList Rects[i] = new Rect(bottom, left, right, top) // Do these need to be this.bottom, etc?
	}


}     



public int[] getRectangleBoundsFromSection(Section section) {

	public int sectionBottom; // Should I declare these here? Or label them as public int down below when I actually use them?
	public int sectionLeft; 
	public int sectionRight;
	public int sectionTop;

  	public int sectionHeight = cHeight / numSections;  // The height of each bar is determined by the total height dividied by the number of Sections. Can I access numSections from here?

  	
  	for i=0; i<Sections.length; i++ {

		public int maxVal = max(activeMeasures<<>>) - 1;    // Can I run a maximum function (whatever it is in Java), on a List?

		/* 
			activeMeasures.getEndValue()
			int max = 0; for all sections, if endVal > max, max = endVal
		*/

		public int measureWidth = maxWidth / maxVal;    // Pixel width of each measure


		// Horizontal bounds are a function of the startLocation and measure width. Vertical bounds are a function of sectionHeight and sectionNumber. Can I access Section parameters from here?

		sectionBottom = (Section.sectionNumber + 1) * sectionHeight; // Gets bottom bound
		sectionLeft = 1 + ((Section.startLocation - 1) * measureWidth);     // Gets left bound 
		sectionRight = measureWidth * (Section.endLocation - 1);   // Gets right bound
		sectionTop = 1 + (Section.sectionNumber * sectionHeight);  // Gets top bound

	}
  
}


// In main View:

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


    private final Paint paint = new Paint();
    
    Paint.setColor(0); // I don't think this is right. maybe paint.setColor(0)?

    public int cHeight = 300px;  // Fixed value or 1/3 the max_height of the screen?
	public int cWidth = maxWidth; 


	public void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        canvas.drawBitmap(cHeight, cWidth, paint);
	    }









// Background View (or CanvasView?)

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;



public class BackgroundView extends View {

    private ArrayList<Object> Rects = new ArrayList<Object>();   // Pretty sure Object is the wrong thing to call this, but not sure what the right thing is


    public BackgroundView(Context context) {    
        setFocusable(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {

    	private final Paint rPaint = new Paint();   // Need to iterate across Sections to find their color value and set rPaint = to that

        for (int i = 0; i < Rects.length; i++) {

            // Draw one rect
            canvas.drawRect(Rect[i], Rect[i].rPaint);
        }
    }








public static final Creator<Rect> Rects() {   //I have that ArrayList Rects above, but not sure if this should be an ArrayList instead...

// Constructor for Rect class

	public int	bottom;
	public int	left;	
	public int	right;	
	public int	top;


	for i=0; i<Sections.length; i++ {
		getRectangleBoundsFromSection(Section section); // This should return a 4 int array, which are the 4 values for sectionBottom, Left, Right, and Top, to be assigned below
	}

	this.bottom = sectionBottom;   // the parameters of each instance of the Rect class. Setting each instance's values to the specific sectionBottom as defined below
	this.left = sectionLeft;
	this.right = sectionRight;
	this.top = sectionTop;


	for i=0; i<numSections; i++ {   // for each of the sections, creates a new Rect in the ArrayList Rects with the parameters specified in getRectangleBoundsFromSection
		ArrayList Rects[i] = new Rect(bottom, left, right, top) // Do these need to be this.bottom, etc?
	}


}     



public int[] getRectangleBoundsFromSection(Section section) {

	public int sectionBottom; // Should I declare these here? Or label them as public int down below when I actually use them?
	public int sectionLeft; 
	public int sectionRight;
	public int sectionTop;

  	public int sectionHeight = cHeight / numSections;  // The height of each bar is determined by the total height dividied by the number of Sections. Can I access numSections from here?

  	
  	for i=0; i<Sections.length; i++ {

		public int maxVal = max(activeMeasures<<>>) - 1;    // Can I run a maximum function (whatever it is in Java), on a List?

		/* 
			activeMeasures.getEndValue()
			int max = 0; for all sections, if endVal > max, max = endVal
		*/

		public int measureWidth = maxWidth / maxVal;    // Pixel width of each measure


		// Horizontal bounds are a function of the startLocation and measure width. Vertical bounds are a function of sectionHeight and sectionNumber. Can I access Section parameters from here?

		sectionBottom = (Section.sectionNumber + 1) * sectionHeight; // Gets bottom bound
		sectionLeft = 1 + ((Section.startLocation - 1) * measureWidth);     // Gets left bound 
		sectionRight = measureWidth * (Section.endLocation - 1);   // Gets right bound
		sectionTop = 1 + (Section.sectionNumber * sectionHeight);  // Gets top bound

	}
  
}


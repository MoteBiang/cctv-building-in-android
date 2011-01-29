package com.pdager.cctv;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class CCTVApp extends Activity {
	
	private GLSurfaceView surface;
	private CCTVRenderer renderer;
	private float mPreviousX;
	private float mPreviousY;
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;
	
	//private GestureDetector gestureDetector;
	private static boolean fullscreen;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
        
        if (fullscreen) {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);  
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        
       // gestureDetector = new GestureDetector(this, new GlAppGestureListener(this));
        
        surface = new GLSurfaceView(this);
        renderer = new CCTVRenderer(this);
        surface.setRenderer(renderer);
        setContentView(surface);
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		surface.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		surface.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_L:
			renderer.toggleLighting();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	   public boolean onTouchEvent(MotionEvent event)
	    {
	    	float x = event.getX();
	    	float y = event.getY();
	    	switch (event.getAction())
	    	{
	    	case MotionEvent.ACTION_MOVE:
	    		float dx = x- mPreviousX;
	    		float dy = y - mPreviousY;
	    		renderer.xrot += dx*TOUCH_SCALE_FACTOR;
	    		renderer.yrot += dy*TOUCH_SCALE_FACTOR;
	    	}
	    	mPreviousX = x;
	    	mPreviousY = y;
	    	return true;
	    }
}
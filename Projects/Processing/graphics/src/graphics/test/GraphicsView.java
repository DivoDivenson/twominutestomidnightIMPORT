package graphics.test;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GraphicsView extends GLSurfaceView {
	private static final String LOG_TAG = GraphicsView.class.getSimpleName();
	private GraphicsRenderer renderer;
	
	public GraphicsView(Context context){
		super(context);
		renderer = new GraphicsRenderer();
		setRenderer(renderer);
	}
	
	private float _x = 0;
	private float _y = 0;
	 
	// code snipped
	 
	public boolean onTouchEvent(final MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        _x = event.getX();
	        _y = event.getY();
	    }
	    if (event.getAction() == MotionEvent.ACTION_MOVE) {
	        final float xdiff = (_x - event.getX());
	        final float ydiff = (_y - event.getY());
	        queueEvent(new Runnable() {
	            public void run() {
	                renderer.setXAngle(renderer.getXAngle() + ydiff);
	                renderer.setYAngle(renderer.getYAngle() + xdiff);
	            }
	        });
	        _x = event.getX();
	        _y = event.getY();
	    }
	    return true;
	}
}

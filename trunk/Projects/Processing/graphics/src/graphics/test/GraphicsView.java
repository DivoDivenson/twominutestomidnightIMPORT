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
	
    public boolean onTouchEvent(final MotionEvent event) {
    	 queueEvent(new Runnable() {
    	        public void run() {
    	            //renderer.setColor(event.getX() / getWidth(), event.getY() / getHeight(), 1.0f);
    	            renderer.setAngle(event.getX() / 100, event.getY() / 100);
    	        }
    	    });
    	    return true;
    }


}

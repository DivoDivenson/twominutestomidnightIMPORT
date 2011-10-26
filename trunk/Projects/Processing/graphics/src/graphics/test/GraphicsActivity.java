package graphics.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GraphicsActivity extends Activity {
	private static final String LOG_TAG = GraphicsActivity.class.getSimpleName();
    private GraphicsView graphicsView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphicsView = new GraphicsView(this);
        setContentView(graphicsView);
       
    }
}
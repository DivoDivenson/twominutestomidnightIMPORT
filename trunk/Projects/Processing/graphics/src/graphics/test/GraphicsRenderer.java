package graphics.test;
//http://www.droidnova.com/android-3d-game-tutorial-part-ii,328.html

import java.nio.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
 
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
 
public class GraphicsRenderer implements GLSurfaceView.Renderer {
    private static final String LOG_TAG = GraphicsRenderer.class.getSimpleName();
 
    private float _red = 0.9f;
    private float _green = 0.2f;
    private float _blue = 0.2f;
 
 
    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
    }
 
    @Override
    public void onDrawFrame(GL10 gl) {
        // define the color we want to be displayed as the "clipping wall"
        gl.glClearColor(_red, _green, _blue, 1.0f);
     
        // clear the color buffer to show the ClearColor we called above...
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glRotatef(xAngle, 0f, 1f, 0f);
        gl.glRotatef(yAngle, 1f, 0f, 0f);
   

     
        // set the color of our element
        gl.glColor4f(0.5f, 0f, 0f, 0.5f);
     
        // define the vertices we want to draw
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
     
        // finally draw the vertices
        gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    }
    
    public void setColor(float r, float g, float b) {
        _red = r;
        _green = g;
        _blue = b;
    }
    
 // new object variables we need
 // a raw buffer to hold indices
 private ShortBuffer _indexBuffer;
  
 // a raw buffer to hold the vertices
 private FloatBuffer _vertexBuffer;
  
 private short[] _indicesArray = {0, 1, 2};
 private int _nrOfVertices = 3;
 
 private float xAngle;
 private float yAngle;
 
 public void setAngle(float xAngle, float yAngle) {
     this.xAngle = xAngle;
     this.yAngle = yAngle;
 }
  
 // code snipped
  
 private void initTriangle() {
     // float has 4 bytes
     ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);
     vbb.order(ByteOrder.nativeOrder());
     _vertexBuffer = vbb.asFloatBuffer();
  
     // short has 2 bytes
     ByteBuffer ibb = ByteBuffer.allocateDirect(_nrOfVertices * 2);
     ibb.order(ByteOrder.nativeOrder());
     _indexBuffer = ibb.asShortBuffer();
  
     float[] coords = {
         -0.5f, -0.5f, 0f, // (x1, y1, z1)
         0.5f, -0.5f, 0f, // (x2, y2, z2)
         0f, 0.5f, 0f // (x3, y3, z3)
     };
  
     _vertexBuffer.put(coords);
     _indexBuffer.put(_indicesArray);
  
     _vertexBuffer.position(0);
     _indexBuffer.position(0);
 }
 
 @Override
 public void onSurfaceCreated(GL10 gl, EGLConfig config) {
     // preparation
     gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
     initTriangle();
 }
    

}

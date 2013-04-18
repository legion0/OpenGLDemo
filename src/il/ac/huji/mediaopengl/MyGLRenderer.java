package il.ac.huji.mediaopengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	private Square square;
	private int width;
	private int height;

    public static final String vertexShaderCode =
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = vPosition;" +
        "}";

    public static final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}";

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color to gray
		GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		// Initialize the square and its shaders
		square = new Square();
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		// Draw triangle
		square.draw(unused);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		this.width = width;
		this.height = height;
		GLES20.glViewport(0, 0, width, height);
	}

	public static int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	public void setGravity(float x, float y, float z) {
		square.setTarget(new float[] { x / this.width * 2 - 1, (y / this.height * 2 - 1) * -1, z });
	}

	public void cancelGravity() {
		square.setTarget(null);
	}
}

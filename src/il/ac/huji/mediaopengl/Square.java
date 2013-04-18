package il.ac.huji.mediaopengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

class Square {

	private float squareCoords[] = { // in counterclockwise order:
	0.1f, 0.1f, 0.0f, // top right
			-0.1f, 0.1f, 0.0f, // top left
			-0.1f, -0.1f, 0.0f, // bottom left
			0.1f, -0.1f, 0.0f // bottom right
	};
	// number of coordinates per vertex in this array
	private static final int COORDS_PER_VERTEX = 3;
	private static final int COORD_SIZE = Float.SIZE / Byte.SIZE;
	// bytes per vertex
	private static final int vertexStride = COORDS_PER_VERTEX * COORD_SIZE;
	private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;

	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

	private final FloatBuffer vertexBuffer;
	private final int mProgram;

	static int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, MyGLRenderer.vertexShaderCode);
	static int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, MyGLRenderer.fragmentShaderCode);

	public Square() {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * COORD_SIZE);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(squareCoords);
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);

		// prepare shaders and OpenGL program
		mProgram = loadProgram();
	}

	public void draw(GL10 unused) {

		// set the shape coords
		vertexBuffer.position(0);
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);

		// get handle to vertex shader's vPosition member
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// Enable a handle to the square vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		// get handle to fragment shader's vColor member
		int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// Set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);

		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
	
	public static int loadProgram() {
		// create empty OpenGL Program
		int mProgram = GLES20.glCreateProgram();
		// add the vertex shader to program
		GLES20.glAttachShader(mProgram, vertexShader);
		// add the fragment shader to program
		GLES20.glAttachShader(mProgram, fragmentShader);
		// create OpenGL program executables
		GLES20.glLinkProgram(mProgram);
		return mProgram;
	}
}

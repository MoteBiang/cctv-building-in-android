package com.pdager.cctv;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class CCTVRenderer implements Renderer {

	boolean key = true;
	float xrot = 0.0f;
	float yrot = 0.0f;
	float xspeed, yspeed;
	float z = -5.0f;
	private boolean lighting = true;
	private LPlane[] root;
	
	 // JinMeng@2011/01/27 ADD START
	float[] verterxTest = new float[]{  // 原始坐标/10
			0.0f,		13.50f,  0.0f, // A1
			14.3f,  13.4f,   0f,  // A2
			14.2f,  0f,      0f,   // A3
			9.0f,   0.1f,    0f, // A4
			9.1f,   9.3f,    0f, //A5
			0f,     9.4f,    0f, // A6
			//底座顶面
			0.5f,   13.0f,   3.94f,  // B1
			3.74f,  13.0f,   3.94f, // B2
			13.90f, 12.96f,  3.94f, // B3
			13.85f, 3.80f,   3.94f, //B4
			13.83f, 0.44f,   3.94f, //B5
			9.05f,  0.46f,   3.94f,  //B6
			9.07f,  3.83f,   3.94f, //B7
			9.1f,   8.9f,    3.94f,  //B8
			3.72f,  8.95f,   3.94f, //B9
			0.47f,  8.96f,   3.94f, //B10
			//顶层底面坐标
			5.29f,  7.46f,   16.23f, // C1
			5.28f,  5.08f,   16.23f,  //C2
			9.08f,  5.06f,   16.23f, //C3
			9.06f,  1.74f,   16.23f, //C4
			2.01f,  1.76f,   16.23f, //C5
			2.03f,  7.48f,   16.23f, //C6	
		   // 顶层上面坐标
			5.91f,  10.81f,  21.66f, //D1
			5.89f,  5.34f,   20.95f, //D2
			12.10f, 5.33f,   19.03f, //D3
			12.01f, 2.0f,    19.03f, //D4
			2.71f,  2.32f,   21.66f, //D5
			3.00f,  10.88f,  23.53f, //D6
	};
	
	private static  byte[][] indexCoords = new byte[][] {
		new byte[] {  	//0 底座底面   逆时针以下是 triangle_fans 的顺序 TODO 加纹理时拆成俩矩形
				// A5, A4, A3, A2, A1, A6
				     4,   3,    2,   1,   0,  5,
		},
		new byte[]{  	// 1 底座顶面      
			  //B8, B7, B4, B3, B2, B9
				13, 12,   9,   8,   7,  14,
		},
		new byte[]{ 		// 2 顶层底面  
			//C2, C1, C6, C5, C4, C3                                  
				17, 16, 21, 20, 19, 18, 
		},
		new byte[]{ //  3 顶层顶面  
			// D2, D1, D6, D5, D4, D3
			    23, 22, 27, 26, 25, 24,	
		},
		new byte[]{ //4  前正面    可拆成 D3, D4, B5, B4 和 B3, B5, A3, A2
			// B4, D3, D4, A3, A2, B3
			    9,   24, 25, 2,   1,     8,	
		},
		new byte[]{ // 5 前背面
			// B7, B8, A5, A4, C4, C3
				12, 13,  4,   3,   19, 18,
		},
		new byte[]{ // 6 后正面
				//C1, C2, D2, D1, B2, B9
				   16, 17, 23,  22, 7, 14,
		},
		new byte[]{ // 7 后背面
				//C6, C5, D5, D6, A1, A6,
				   21, 20, 26,  27,   0,  5,
		},
		new byte[]{ // 8 左外面
				//D4, D5, C5, C4, A4, A3
				   25,  26, 20, 19, 3,   2, 
		},
		new byte[]{ // 9 左内面
				//C3, C2, D2, D3, B4, B7
				   18, 17, 23, 24, 9, 12,
		},
		new byte[]{ // 10 右外面
				//B2, B3, A2, A1, D6, D1,
				  7,    8,   1,    0,  27,  22,
		},
		new byte[]{ // 11 右内面	
				//B9, C1, C6, A6, A5, B8,
				  14, 16,   21,  5,  4,  13,
		},
	};		
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		// Translates 4 units into the screen.
		gl.glTranslatef(0, 0, -4);
		// Draw our scene.
		for(int i=0; i<root.length; i++)
		root[i].draw(gl);


	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				1000.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

	}
	
	public void toggleLighting() {
		lighting = !lighting;
	}

}

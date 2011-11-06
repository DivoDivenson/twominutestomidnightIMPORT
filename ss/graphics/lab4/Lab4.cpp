//#include <windows.h>	// for timeGetTime()
//#include <mmsystem.h>	// ditto
#include <iostream>		// I/O
#include <GL/glut.h>	// GLUT
#include <sys/time.h>
//#include <gl/glu.h>		// for gluPerspective & gluLookAt
#include "model3DS.h" // 3DS model support

model3DS *teddyModel;

void setupScene();
void updateScene();
void renderScene();
void exitScene();
void keypress(unsigned char key, int x, int y);
void setViewport(int width, int height);
typedef unsigned int DWORD;

int         tetrahedronAngle=0;
bool		wireframe=false;
int         windowId;
GLuint      textureId;
DWORD		lastTickCount;
GLfloat white_light[] = {1.0, 1.0, 1.0, 1.0};
GLfloat left_light_position[] = {1,0,-1, 1.0}; 
GLfloat right_light_position[] = {-1,0,-1, 1.0};


void renderScene(){
        
    // Clear framebuffer & depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    
    // Reset Modelview matrix      	
    glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	



	// Set view position & direction
	// (Camera at (0,0,5) looking down the negative Z-axis)
	gluLookAt(0,0,5,  0,0,-1,  0,1,0);


    // Draw textured tetrahedron
	glEnable(GL_TEXTURE_2D);
	glColor3f(1.0,1.0,1.0);
    glBindTexture(GL_TEXTURE_2D,textureId);

    glPushMatrix();

	glTranslatef(0.0,0.0,-1000.0);
	glRotatef(-1*tetrahedronAngle/4.f,0,1,0);
    glRotatef(-1*tetrahedronAngle/3.f,1,0,0);
    glRotatef(-1*tetrahedronAngle/2.f,0,0,1);	

	//DrawTetrahedron();
	teddyModel->draw();

    glDisable(GL_TEXTURE_2D);
	glPopMatrix();
   
 
    // Swap double buffer for flicker-free animation
    glutSwapBuffers();
        
}

void updateScene(){
	
	// Wait until at least 16ms passed since start of last frame
	// Effectively caps framerate at ~60fps
	while(timeGetTime()-lastTickCount<16);
	lastTickCount=timeGetTime();
    
    // Increment angle for next frame
    tetrahedronAngle+=2;

	// Do any other updates here
	
	// Draw the next frame
    glutPostRedisplay();

}

void keypress(unsigned char key, int x, int y){
	
	// Test if user pressed ESCAPE (ascii 27)
	// If so, exit the program
    if(key==27){
		exitScene();
	}

	// Other possible keypresses go here
	//if(key == 'a'){...}
}

void setupScene(){

	std::cout<<"Initializing scene..."<<std::endl;
    
	//Set up Lighting Stuff
	glLightfv(GL_LIGHT0, GL_POSITION, left_light_position);
	glLightfv(GL_LIGHT0, GL_SPECULAR, white_light);
	glLightfv(GL_LIGHT1, GL_POSITION, right_light_position);
	glLightfv(GL_LIGHT1, GL_SPECULAR, white_light);
	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHT1);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, white_light);
	glLightfv(GL_LIGHT1, GL_DIFFUSE, white_light);
	glShadeModel(GL_SMOOTH);

	glEnable(GL_DEPTH_TEST);

	//Load the teddy model
	teddyModel = new model3DS("teddy\\teddy.3ds", 2);
      
}

void exitScene(){

    std::cout<<"Exiting scene..."<<std::endl;

    // Close window
    glutDestroyWindow(windowId);

    // Free any allocated memory

    // Exit program
    exit(0);
}

void setViewport(int width, int height) {

    // Work out window ratio, avoid divide-by-zero
    if(height==0)height=1;
	float ratio = float(width)/float(height);

	// Reset projection matrix
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	
	// Fill screen with viewport
	glViewport(0, 0, width, height);

	// Set a 45 degree perspective
	gluPerspective(45, ratio, .1, 2000);

}

int main(int argc, char *argv[]){
        
    // Initialise OpenGL
    glutInit(&argc, argv); 

    // Set window position, size & create window
    glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
    glutInitWindowPosition(50,50);
    glutInitWindowSize(640,480);
	windowId = glutCreateWindow("Lab 4: Loading a textured model");
    
    // Set GLUT callback functions
    glutReshapeFunc(setViewport);
    glutDisplayFunc(renderScene);
    glutIdleFunc(updateScene);
    glutKeyboardFunc(keypress);

    // Setup OpenGL state & scene resources (models, textures etc)
    setupScene();

    // Show window & start update loop
    glutMainLoop();    

    return 0;
    
}

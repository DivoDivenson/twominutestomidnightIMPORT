//#include <windows.h>	// for timeGetTime()
//#include <mmsystem.h>	// ditto
#include <iostream>		// I/O
#include <GL/glut.h>	// GLUT
#include <sys/time.h>
//#include <gl/glu.h>		// for gluPerspective & gluLookAt
#include "model3DS.h" // 3DS model support

#define PI 3.141592654f.0

model3DS *teddyModel;
model3DS * table;
model3DS * carpark;

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

int cameraZ;
int camX, camY;

float xpos = 0, ypos = 0, zpos = 0, xrot = 0, yrot = 0, angle=0.0;
float lastx, lasty;



GLuint      textureId;
DWORD		lastTickCount;
GLfloat white_light[] = {1.0, 1.0, 1.0, 1.0};
GLfloat left_light_position[] = {1,0,-1, 1.0}; 
GLfloat right_light_position[] = {-1,0,-1, 1.0};

void camera (void) {
    glRotatef(xrot,1.0,0.0,0.0);  
    glRotatef(yrot,0.0,1.0,0.0);  
    glTranslated(-xpos,-ypos,-zpos); 
}


void renderScene(){
        
    // Clear framebuffer & depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    
    // Reset Modelview matrix      	
    glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	



	// Set view position & direction
	// (Camera at (0,0,5) looking down the negative Z-axis)
	//gluLookAt(0,0,cameraZ,  camX,camY,-1,  0,1,0);
	//gluLookAt(xpos,ypos,zpos,  xrot + 10,yrot + 10,-1,  0,1,0);
	camera();


    // Draw textured tetrahedron
	glEnable(GL_TEXTURE_2D);
	glColor3f(1.0,1.0,1.0);
    glBindTexture(GL_TEXTURE_2D,textureId);

    glPushMatrix();

	glTranslatef(0.0,0.0,-600.0);
	glRotatef(-1*tetrahedronAngle/4.f,0,1,0);
    //glRotatef(-1*tetrahedronAngle/3.f,1,0,0);
    //glRotatef(-1*tetrahedronAngle/2.f,0,0,1);	

	//DrawTetrahedron();
	//teddyModel->draw();
	//table->draw();
	carpark->draw();


    glDisable(GL_TEXTURE_2D);
	glPopMatrix();
   
 
    // Swap double buffer for flicker-free animation
    glutSwapBuffers();
        
}

void updateScene(){
	
	// Wait until at least 16ms passed since start of last frame
	// Effectively caps framerate at ~60fps
	//while(timeGetTime()-lastTickCount<16);
	//lastTickCount=timeGetTime();
    
    // Increment angle for next frame
    //tetrahedronAngle+=2;

	// Do any other updates here
	
	// Draw the next frame
    glutPostRedisplay();

}

void keypress(unsigned char key, int x, int y){
	
	// Test if user pressed ESCAPE (ascii 27)
	// If so, exit the program
	if(tetrahedronAngle >= 360){
		//tetrahedronAngle = 0;
	}

	printf("%d %d\n", cameraZ, key);
    if(key==27){
		exitScene();
	//Forward
	}

	if(key=='w'){
	float xrotrad, yrotrad;
    yrotrad = (yrot / 180 * 3.141592654f);
    xrotrad = (xrot / 180 * 3.141592654f); 
    xpos += float(sin(yrotrad));
    zpos -= float(cos(yrotrad))+10;
    ypos -= float(sin(xrotrad));
	//Backward
	}

	if(key =='s'){
	float xrotrad, yrotrad;
    yrotrad = (yrot / 180 * 3.141592654f);
    xrotrad = (xrot / 180 * 3.141592654f); 
    xpos -= float(sin(yrotrad));
    zpos += float(cos(yrotrad))+10;
    ypos += float(sin(xrotrad));
	}

	if(key=='d')
    {
    float yrotrad;
	yrotrad = (yrot / 180 * 3.141592654f);
	xpos += float(cos(yrotrad)) * 0.2 + 10;
	zpos += float(sin(yrotrad)) * 0.2;
    }

    if(key=='a')
    {
   	float yrotrad;
	yrotrad = (yrot / 180 * 3.141592654f);
	xpos -= float(cos(yrotrad)) * 0.2 + 10;
	zpos -= float(sin(yrotrad)) * 0.2;
    }
	

	// Other possible keypresses go here
	//if(key == 'a'){...}
}

void mouseMove(int x, int y){
	int diffx=x-lastx; //check the difference between the current x and the last x position
	int diffy=y-lasty; //check the difference between the current y and the last y position
	lastx=x; //set lastx to the current x position
	lasty=y; //set lasty to the current y position
	xrot += (float) diffy; //set the xrot to xrot with the addition of the difference in the y position
	yrot += (float) diffx;// set the xrot to yrot with the addition of the difference in the x position
}

void setupScene(){

	std::cout<<"Initializing scene..."<<std::endl;
   
	cameraZ = -1000;
	camX =0;
	camY =0;
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
	teddyModel = new model3DS("./teddy/teddy.3ds", 2);
	table = new model3DS("./table/table.3ds", 50);
	carpark = new model3DS("./carpark/carparkfinal.3ds", 30);
      
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
    glutPassiveMotionFunc(mouseMove);

    // Setup OpenGL state & scene resources (models, textures etc)
    setupScene();

    // Show window & start update loop
    glutMainLoop();    

    return 0;
    
}

//#include <windows.h>	// for timeGetTime()
//#include <mmsystem.h>	// ditto
#include <iostream>		// I/O
#include <GL/glut.h>	// GLUT
#include <sys/time.h>
//#include <gl/glu.h>		// for gluPerspective & gluLookAt
#include "model3DS.h" // 3DS model support
#include "shot.h"

#define PI M_PI.0

#define SPEED 1

model3DS *teddyModel;
model3DS * table;
model3DS * carpark; //My model

model3DS * car; //From the web

shot * test_shot;

void setupScene();
void updateScene();
void renderScene();
void exitScene();
void keypress(unsigned char key, int x, int y);
void setViewport(int width, int height);
typedef unsigned int DWORD;

int         tetrahedronAngle=0;

int rotationAngle=0; //For moving object

bool		wireframe=false;
int         windowId;

int cameraZ;
int camX, camY;
//
float xpos = 0, ypos = 0, zpos = 0, xrot = 0, yrot = 0, angle=0.0;
float lastx, lasty;

float cRadius = 10.0f;



GLuint      textureId;

GLfloat white_light[] = {1.0, 1.0, 1.0, .5};
GLfloat left_light_position[] = {-200,20.0,-1, 1.0}; 
GLfloat right_light_position[] = {-1,0,-1, 1.0};

GLfloat emerald_ambient[] =
{0.0215, 0.1745, 0.0215}, emerald_diffuse[] =
{0.07568, 0.61424, 0.07568}, emerald_specular[] =
{0.633, 0.727811, 0.633}, emerald_shininess = 76.8;



void camera (void) {
    glRotatef(xrot,1.0,0.0,0.0);  
    glRotatef(yrot,0.0,1.0,0.0);
    glTranslated(-xpos,-ypos,-zpos); 
 
}

void drawObjects(int move = 0, int scale = 1.0f){
//Stick this here for the moment, lazy

	GLUquadric* nQ;
    nQ=gluNewQuadric();
    GLUquadric * nQ2;
    nQ2 = gluNewQuadric();

	glutSolidCube(1.0f * scale);
    
    //glTranslatef(0.0f, -1.0f, 0.0f);
    //rotate everything above the base
    if(move){
        glRotatef(-1*rotationAngle/4.f,0,1,0);
        glRotatef(-1*rotationAngle/2.f,0,0,1);
        glRotatef(-1*rotationAngle/3.f,1,0,0);
    }

//  glPushMatrix();
    //draw first joint
    glutSolidSphere(0.3f * scale, 20, 20);  

    //cylinder, draw upper arm
    glPushMatrix();
    glRotatef(90.0f, -1.0f, 0.0f, 0.0f);
    gluCylinder(nQ, 0.15 * scale, 0.15 * scale, 1.5f * scale, 20, 5);
    //glRotatef(90.0f, 1.0f, 0.0f, 0.0f); //Same as pop
    glPopMatrix(); //Undo the rotate
    
    glTranslatef(0.0f, 1.7f * scale, 0.0f);
    //Rotate the lower arm
    if(move){
        glRotatef(-1*rotationAngle/4.f,0,1,0);
        glRotatef(-1*rotationAngle/3.f,1,0,0);
        glRotatef(-1*rotationAngle/2.f,0,0,1);
    }
    //second join
    glutSolidSphere(0.3f * scale, 20, 20);
    //Horizontal cylinder for lower arm
    gluCylinder(nQ2, 0.15 * scale, 0.15 * scale, 1.5f * scale, 20, 5);
	glTranslatef(0.0f, 0.0f, 1.5f * scale);
	glutSolidSphere(0.3f * scale, 20, 20);
}

void enable(){

	glEnable (GL_DEPTH_TEST); //enable the depth testing
    glEnable (GL_LIGHTING); //enable the lighting
    glEnable (GL_LIGHT0); //enable LIGHT0, our Diffuse Light
    glShadeModel (GL_SMOOTH); //set the shader to smooth shader
	glEnable(GL_TEXTURE_2D);
	//glColor3f(1.0,1.0,1.0);
    glBindTexture(GL_TEXTURE_2D,textureId);
    

}


void renderScene(){
        
    // Clear framebuffer & depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glEnable(GL_LIGHTING);
    
    // Reset Modelview matrix      	
    glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	
	enable();



	// Set view position & direction
	// (Camera at (0,0,5) looking down the negative Z-axis)
	//gluLookAt(0,0,cameraZ,  camX,camY,-1,  0,1,0);
	//gluLookAt(xpos,ypos,zpos,  xrot + 10,yrot + 10,-1,  0,1,0);
	//camera();

	//Camera for the object to follow
	glDisable(GL_TEXTURE_2D);
	glEnable (GL_COLOR_MATERIAL);

	glTranslatef(0.0f, -2.0f, -cRadius);
	glRotatef(xrot, 1.0f, 0.0f, 0.0f);

	glColor3f(1.0f, 1.0f, 1.0f);
	car->draw();
    //Third person object goes here
	//glutSolidCube(2);

	glRotatef(yrot,0.0,1.0,0.0);  //rotate our camera on the y-axis

    glTranslatef(-xpos,2.0f,-zpos); //translate the screen to the position of our camera

    glDisable(GL_COLOR_MATERIAL);
    glEnable(GL_TEXTURE_2D);



 
    //glutSolidCube(2);
    //glTranslatef(0.0,0.0,-600.0);
    carpark->draw();
    test_shot->draw();




    /*glPushMatrix();

	glTranslatef(0.0,0.0,-600.0);
	//glRotatef(-1*tetrahedronAngle/4.f,0,1,0);
    //glRotatef(-1*tetrahedronAngle/3.f,1,0,0);
    //glRotatef(-1*tetrahedronAngle/2.f,0,0,1);	

	//DrawTetrahedron();
	//teddyModel->draw();
	//table->draw();
	//carpark->draw();
	
	glPushMatrix();
	
	glPopMatrix();
	glEnable(GL_TEXTURE_2D);

	
	glTranslatef(-1000.0, -40.0, 250);
	glRotatef(90.0, 0.0, 1.0, 0.0);

	car->draw();


    glDisable(GL_TEXTURE_2D);
	glPopMatrix();*/
   
 
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
    rotationAngle+=2;

	// Do any other updates here
	
	// Draw the next frame
    glutPostRedisplay();

}


void mouse_func(int button, int state, int x, int y){
    if(button == GLUT_LEFT_BUTTON){
        test_shot = new shot(xpos, -2.0f, zpos, yrot - 90);
    }    
}

void keypress(unsigned char key, int x, int y){
	
	   if (key=='q')
    {
    xrot += 1;
    if (xrot >360) xrot -= 360;
    }

    if (key=='z')
    {
    xrot -= 1;
    if (xrot < -360) xrot += 360;
    }

    if (key=='w')
    {
    float xrotrad, yrotrad;
    yrotrad = (yrot / 180 * M_PI);
    xrotrad = (xrot / 180 * M_PI); 
    xpos += float(sin(yrotrad) * SPEED);
    zpos -= float(cos(yrotrad) * SPEED);
    ypos -= float(sin(xrotrad) * SPEED);
    }

    if (key=='s')
    {
    float xrotrad, yrotrad;
    yrotrad = (yrot / 180 * M_PI);
    xrotrad = (xrot / 180 * M_PI); 
    xpos -= float(sin(yrotrad) * SPEED);
    zpos += float(cos(yrotrad) * SPEED) ;
    ypos += float(sin(xrotrad) * SPEED);
    }

    if (key=='d')
    {
    	float yrotrad;
		yrotrad = (yrot / 180 * M_PI);
		xpos += float(cos(yrotrad)) * SPEED;
		zpos += float(sin(yrotrad)) * SPEED;
    }

    if (key=='a')
    {
   		float yrotrad;
		yrotrad = (yrot / 180 * M_PI);
		xpos -= float(cos(yrotrad)) * SPEED;
		zpos -= float(sin(yrotrad)) * SPEED;
    }
    if (key==27)
    {
    exit(0);
    }

}

void mouseMove(int x, int y){
    
	int diffx=x-lastx; //check the difference between the current x and the last x position
	int diffy=y-lasty; //check the difference between the current y and the last y position
	lastx=x; //set lastx to the current x position
	lasty=y; //set lasty to the current y position
	xrot += (float) diffy; //set the xrot to xrot with the addition of the difference in the y position
	yrot += (float) diffx;// set the xrot to yrot with the addition of the difference in the x position
    if(yrot > 360.0f){
        yrot -= 360.0f;
    }
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
//	glEnable(GL_LIGHT1);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, white_light);
	glLightfv(GL_LIGHT1, GL_DIFFUSE, white_light);
	glShadeModel(GL_SMOOTH);

	glEnable(GL_DEPTH_TEST);

	//Load the teddy model
	teddyModel = new model3DS("./teddy/teddy.3ds", 1);
	table = new model3DS("./table/table.3ds", 50);
	carpark = new model3DS("./carpark/carparkfinal.3ds", 1);
	car = new model3DS("./car/car.3ds",0.05f);
    test_shot = new shot(0, 0, 0, 45);
      
      
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
    glutInitWindowSize(1280,1000);
	windowId = glutCreateWindow("Lab 4: Loading a textured model");
    
    // Set GLUT callback functions
    glutReshapeFunc(setViewport);
    glutDisplayFunc(renderScene);
    glutIdleFunc(updateScene);
    glutKeyboardFunc(keypress);
    glutPassiveMotionFunc(mouseMove);
    glutMouseFunc(mouse_func);

    // Setup OpenGL state & scene resources (models, textures etc)
    setupScene();

    // Show window & start update loop
    glutMainLoop();    

    return 0;
    
}

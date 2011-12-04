#include <cstdlib>
#include <ctime>
#include <iostream>
#include <math.h>
#include <set>
#include <sstream>
#include <stdlib.h>
#include <vector>
#include <GL/glut.h>	// GLUT
#include <sys/time.h>

#include "model3DS.h"
#include "hit_box.h"

#define SPEED 1

using namespace std;
const int NUM_GUYS = 100;

const float TERRIAN_WIDTH = 50.0f;

const float TIME_BETWEEN_HANDLE_COLLISIONS = 0.01f;

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





model3DS * car;

vector<hit_box*> _objects;

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



void init(){

	std::cout<<"Initializing scene..."<<std::endl;
	cameraZ = -1000;
	camX =0;
	camY =0;
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	//Set up Lighting Stuff
	glLightfv(GL_LIGHT0, GL_POSITION, left_light_position);
	glLightfv(GL_LIGHT0, GL_SPECULAR, white_light);
	glEnable(GL_LIGHT0);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, white_light);
	glEnable(GL_NORMALIZE);
	glEnable(GL_COLOR_MATERIAL);
	glShadeModel(GL_SMOOTH);

	car = new model3DS("./car/car.3ds", 0.05f);


	
}

vector<hit_box*> makeObjects(int number){
	vector<hit_box*> objects;
	for(int i = 0; i < number; i++){
		objects.push_back(new hit_box(1.0f));
	}
	return objects;
}

void setViewport(int width, int height){
	
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
//need to trim this
void enable(){

	glEnable (GL_DEPTH_TEST); //enable the depth testing
    glEnable (GL_LIGHTING); //enable the lighting
    glEnable (GL_LIGHT0); //enable LIGHT0, our Diffuse Light
    glShadeModel (GL_SMOOTH); //set the shader to smooth shader
	glEnable(GL_TEXTURE_2D);
	//glColor3f(1.0,1.0,1.0);
    glBindTexture(GL_TEXTURE_2D,textureId);
    

}

void updateScene(){
	for (unsigned int i = 0; i < _objects.size(); i++){
		_objects[i]->advance(0.01f);
	}
	glutPostRedisplay();
}


void renderScene(){
	       
    // Clear framebuffer & depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	enable();

	glTranslatef(0.0f, -2.0f, -cRadius);
	glRotatef(xrot, 1.0f, 0.0f, 0.0f);
	glutSolidCube(1);


	glRotatef(yrot,0.0,1.0,0.0);  //rotate our camera on the y-axis

    glTranslatef(-xpos,0.0f,-zpos); //translate the screen to the position of our camera


	for (unsigned int i = 0; i < _objects.size(); i++){
		_objects[i]->draw();
	}

	glutSwapBuffers();
	
}

int main(int argc, char ** argv){
	srand((unsigned int)time(0));

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(1280, 1000);

	glutCreateWindow("A Game");
	init();

	_objects = makeObjects(100);

	glutDisplayFunc(renderScene);
	glutReshapeFunc(setViewport);
	glutKeyboardFunc(keypress);
    glutPassiveMotionFunc(mouseMove);
    glutIdleFunc(updateScene);
    //glutMouseFunc(mouse_func);


	glutMainLoop();
	return 0; //Just for kicks
}
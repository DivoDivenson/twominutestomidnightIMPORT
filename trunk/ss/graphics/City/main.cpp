#include <iostream>
#include <stdlib.h>

#include <GL/glut.h>

using namespace std;

void handleKeypress(unsigned char key, int x, int y){
	switch(key){
		case 27:
			exit(0);
	}
}

void handleResize(int w, int h){
	glViewport(0, 0, w, h); /*Set the viewport. 0, 0 specifies the lower left corner. 
								width and height
								Tells opengl how to convert from coordinates to pixel values*/
	glMatrixMode(GL_PROJECTION); /*Specify which matrix is the current matrix. Model, Projection
									texture or color 
									Switch to setting the camera perspective*/

	glLoadIdentity();		//Replace current matrix with identity matrix. Reset the camera

	gluPerspective(45, 		//The camera angle
					(double) w / (double)h, //Width to height ratio
					1.0, 	//The near z clipping coordinate
					200.0); //The far z clipping coordinate
}

//Expects colours + quads to be set
//Actualy draws a brick (Right rectangular prisim)
void drawCube(float x, float y, float z, float w, float l, float h){
	glPushMatrix();
	glTranslatef(x, y, z);

	glBegin(GL_QUADS);

	//base, check the normal later
	glNormal3f(0.0f, -1.0f, 0.0f);
	glVertex3f(0.0f, 0.0f, 0.0f);
    glVertex3f(w, 0.0f, 0.0f);
    glVertex3f(w, 0.0f, l);
    glVertex3f(0.0f, 0.0f, l);


    //front
    glNormal3f(0.0f, 0.0f, -1.0f);
	glVertex3f(0.0f, 0.0f, 0.0f);
    glVertex3f(w, 0.0f, 0.0f);
    glVertex3f(w, h, 0.0f);
    glVertex3f(0.0f, h, 0.0f);

    //back
    glNormal3f(0.0f, 0.0f, 1.0f);
	glVertex3f(0.0f, 0.0f, l);
    glVertex3f(w, 0.0f, l);
    glVertex3f(w, h, l);
    glVertex3f(0.0f, h, l);

    //top
    glNormal3f(0.0f, 1.0f, 0.0f);
	glVertex3f(0.0f, h, 0.0f);
    glVertex3f(w, h, 0.0f);
    glVertex3f(w, h, l);
    glVertex3f(0.0f, h, l);

    //right
    glNormal3f(1.0f, 0.0f, 0.0f);
    glVertex3f(0.0f, 0.0f, 0.0f);
    glVertex3f(0.0f, 0.0f, l);
    glVertex3f(0.0f, h, l);
    glVertex3f(0.0f, h, 0.0f);

    //left
    glNormal3f(-1.0f, 0.0f, 0.0f);
    glVertex3f(w, 0.0f, 0.0f);
    glVertex3f(w, 0.0f, l);
    glVertex3f(w, h, l);
    glVertex3f(w, h, 0.0f);
    
   


	glEnd();
	glPopMatrix();


}


float _angle = 30.0f;
void drawScene(){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();


	glTranslatef(0.0f, 0.0f, -15.0f);

	glPushMatrix();
	//Add ambient light
	GLfloat ambientColor[] = {0.2f, 0.2f, 0.2f, 1.0f};
	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientColor);
	//Add positioned light
	GLfloat lightColor0[] = {0.5f, 0.5f, 0.5f, 1.0f};
	GLfloat lightPos0[] = {4.0f, 0.0f, 8.0f, 1.0f};//0 for last arg would make it directional
	glLightfv(GL_LIGHT0, GL_DIFFUSE, lightColor0);
	glLightfv(GL_LIGHT0, GL_POSITION, lightPos0);

	glRotatef(_angle, 1.0f, 0.0f, 0.0f);
	glColor3f(1.0f, 1.0f, 1.0f);
   
   	drawCube(0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 5.0f);
   	drawCube(4.0f, 0.0f, 4.0f, 2.0f, 2.0f, 5.0f);


    glPopMatrix(); //Undo the move to the center of the triangle	
	glutSwapBuffers(); //Send the 3D scene to the screen



	
	

}

void update(int value){
	_angle += 2.0f;
	if(_angle > 360){
		_angle -= 360;
	}

	glutPostRedisplay();

	glutTimerFunc(25, update, 0);
}

void initRendering(){
	glEnable(GL_DEPTH_TEST); //Makes 3D drawing work when something is in front of something else
	glEnable(GL_LIGHTING);	
	glEnable(GL_LIGHT0);
	glEnable(GL_NORMALIZE); //Normalize vectors to unit length
	glEnable(GL_COLOR_MATERIAL);
}

int main(int argc, char** argv){
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(1000, 600);

	glutCreateWindow("City");
	initRendering();

	glutDisplayFunc(drawScene);
	glutKeyboardFunc(handleKeypress);
	glutReshapeFunc(handleResize);
 	//glutTimerFunc(25, update, 0);

	glutMainLoop();
		return 0;
}
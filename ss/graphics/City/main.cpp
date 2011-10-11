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

void drawScene(){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glMatrixMode(GL_MODELVIEW);

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

	glutMainLoop();
		return 0;
}
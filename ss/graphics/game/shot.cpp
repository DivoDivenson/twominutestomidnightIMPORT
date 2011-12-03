#include "shot.h"

shot::shot(float xpos, float ypos, float zpos, float init_angle){
	x = xpos;
	y = ypos;
	z = zpos;
	angle = init_angle;
	speed = 1.1f;

}


void shot::draw(){
	glPushMatrix();
	glTranslatef(x, y, z);
	glRotatef(angle, 0.0f, 1.0f, 0.0f);
	x += float(cos(angle*M_PI/180)) * speed;
	z += float(sin(angle*M_PI/180)) * speed;
	glutSolidCube(1);
	glPopMatrix();
}

void shot::update_shot(float xpos, float ypos, float zpos, float init_angle){
	x = xpos;
	y = ypos;
	z = zpos;
	angle = init_angle;
}
#include "hit_box.h"

float randomFloat(){
	return (float)rand() / ((float)RAND_MAX + 1);
}

void hit_box::step(){
	//Update the turning direction information
	timeUntilSwitchDir -= step_time;
	while (timeUntilSwitchDir <= 0) {
		timeUntilSwitchDir += 20 * randomFloat() + 15;
		isTurningLeft = !isTurningLeft;
	}

	float maxX = terrainScale * t_width - radius0;
	float maxZ = terrainScale * t_length - radius0;

	x0 += velocityX() * step_time;
	z0 += velocityZ() * step_time;

	bool hitEdge = false;

	//Stay inside the map
	if (x0 < radius0) {	
		x0 = radius0;
		hitEdge = true;
	}
	else if (x0 > maxX) {
		x0 = maxX;
		hitEdge = true;
	}
			
	if (z0 < radius0) {
		z0 = radius0;
		hitEdge = true;
	}
	else if (z0 > maxZ) {
		z0 = maxZ;
		hitEdge = true;
	}

	if(hitEdge){
		//Speed up when an edge is hit
		if(isTurningLeft){
			angle -= 0.5f * speed * step_time;
		}else{
			angle += 0.5f * speed * step_time;
		}
	}else if(isTurningLeft){
		angle -= 0.05f * speed * step_time;
	}else{
		angle += 0.05f * speed * step_time;
	}

	while (angle > 2 * M_PI){
		angle -= 2 * M_PI;
	}
	while (angle < 0){
		angle += 2 * M_PI;
	}
}



hit_box::hit_box(float terrainScale1){
	terrainScale = terrainScale1;

	timeUntilNextStep = 0;
	radius0 = 0.4f * randomFloat() + 0.25f;

	x0 = randomFloat() * (terrainScale * t_width - radius0) + radius0;
	z0 = randomFloat() * (terrainScale * t_length - radius0) + radius0;

	speed = 1.5f * randomFloat() + 2.0f;
	isTurningLeft = randomFloat() < 0.5f;
	angle = 2 * M_PI * randomFloat();
	timeUntilSwitchDir = randomFloat() * (20 * randomFloat() + 15);

}

//Advance movement by dt. Calls step the right number of times
void hit_box::advance(float dt){

	while (dt > 0){
		if(timeUntilNextStep < dt){
			dt -= timeUntilNextStep;
			step();
			timeUntilNextStep = step_time;
		}else{
			timeUntilNextStep -= dt;
			dt = 0; //bug
		}
	}
}

void hit_box::draw(){
	
	//float scale = radius0 / 2.5f;
	glPushMatrix();
	//y will have to be replaced when a proper map is made
	glTranslatef(x0, 0.0f, z0);
	glRotatef(90 - angle * 180 / M_PI, 0, 1, 0);
	//model goes here
	glutSolidSphere(radius0, 20, 20);
	glPopMatrix();
}

float hit_box::x(){
	return x0;
}

float hit_box::z(){
	return z0;
}

float hit_box::y(){
	//replace
	return 1.0f;
}

float hit_box::velocityX(){
	return speed * cos(angle);
}

float hit_box::velocityZ(){
	return speed * sin(angle);
}

float hit_box::radius(){
	return radius0;
}

float hit_box::walkAngle(){
	return angle;
}

void hit_box::bounceOff(hit_box * otherBox){
	float vx = velocityX();
	float vz = velocityZ();

	//Find distances between the two
	float dx = otherBox->x0 - x0;
	float dz = otherBox->z0 - z0;

	//What do you know, something I leanred in secondary school
	float m = sqrt(dx * dx + dz * dz);
	dx /= m;
	dz /= m;

	float dotProduct = vx * dx + vz * dz;
	vx -= 2 * dotProduct * dx;
	vz -= 2 * dotProduct * dz;

	//This check is somewhat important :)
	if(vx != 0 || vz != 0){
		angle = atan2(vz, vx);
	}
}
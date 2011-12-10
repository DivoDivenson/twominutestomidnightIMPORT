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
		isGoingUp = !isGoingUp;
	}

	float maxX = terrainScale * t_width - radius0;
	float maxZ = terrainScale * t_length - radius0;
	float maxY = terrainScale * t_length - radius0;//Change to height

	x0 += velocityX() * step_time;
	z0 += velocityZ() * step_time;
	y0 += velocityY() * step_time;
	//y0 = 1.0f;

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

	if (y0 < radius0){
		y0 = radius0;
		hitEdge = true;
	}else if(y0 > maxY){
		y0 = maxY;
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

	if(hitEdge){
		//Speed up when an edge is hit
		if(isGoingUp){
			angleX -= 0.5f * speed * step_time;
		}else{
			angleX += 0.5f * speed * step_time;
		}
	}else if(isGoingUp){
		angleX -= 0.05f * speed * step_time;
	}else{
		angleX += 0.05f * speed * step_time;
	}

	while (angle > 2 * M_PI){
		angle -= 2 * M_PI;
	}
	while (angle < 0){
		angle += 2 * M_PI;
	}

	while (angleX > 2 * M_PI){
		angleX -= 2 * M_PI;
	}
	while (angleX < 0){
		angleX += 2 * M_PI;
	}
}



hit_box::hit_box(float terrainScale1){
	terrainScale = terrainScale1;
	dead = false;

	timeUntilNextStep = 0;
	radius0 = 0.4f * randomFloat() + 0.25f;

	x0 = randomFloat() * (terrainScale * t_width - radius0) + radius0;
	z0 = randomFloat() * (terrainScale * t_length - radius0) + radius0;
	y0 = randomFloat() * (terrainScale * t_length - radius0) + radius0;
	//y0 = 1.0f;

	speed = 11.5f * randomFloat() + 2.0f;
	isTurningLeft = randomFloat() < 0.5f;
	isGoingUp = randomFloat() < 0.5f;

	angle = 2 * M_PI * randomFloat();
	angleX = 2 * M_PI * randomFloat();

	timeUntilSwitchDir = randomFloat() * (20 * randomFloat() + 15);

}

void hit_box::kill(){
	dead = true;
	printf("Killed\n");
}

bool hit_box::isDead(){

	return dead;
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
	//printf("Ball %f %f\n", x0, z0);

}

void hit_box::draw(){
	
	//float scale = radius0 / 2.5f;
	;
}

float hit_box::x(){
	return x0;
}

float hit_box::z(){
	return z0;
}

float hit_box::y(){
	//replace
	//return 1.0f;
	return y0;
}

float hit_box::velocityX(){
	return speed * cos(angle);
}

float hit_box::velocityZ(){
	return speed * sin(angle);
}

float hit_box::velocityY(){
	return speed * sin(angleX);//Simplest way to do this, although it does suck baddly
	//return y0;
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
	//printf("BALL\n");
}

void player::bounceOff(hit_box * otherBox){
	float vx = velocityX();
	float vz = velocityZ();

	//Find distances between the two
	float dx = otherBox->x() - x0;
	float dz = otherBox->z() - z0;

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

player::player(float terrainScale, float xpos, float ypos, float zpos, char * _model) : hit_box(terrainScale){

	timeUntilNextStep = 0;
	radius0 = 1.0f;
	x0 = xpos;
	y0 = ypos;
	z0 = zpos;
	dead = false;
	model = new model3DS(_model, 1);
	//speed = 11.5f * randomFloat() + 2.0f;
	isTurningLeft = randomFloat() < 0.5f;
	//angle = 2 * M_PI * randomFloat();
	timeUntilSwitchDir = randomFloat() * (20 * randomFloat() + 15);

}

void player::advance(float dt){
	;//printf("Player advanced\n");
}

void player::update_pos(float xpos, float ypos, float zpos, float angle){
	x0 = xpos;
	z0 = zpos;
	y0 = ypos;
	angle = angle;
}

void player::draw(){
	
	
	//float scale = radius0 / 2.5f;
	glPushMatrix();
	//y will have to be replaced when a proper map is made
	//glTranslatef(x0, 0.0f, z0);
	//glRotatef(90 - angle * 180 / M_PI, 0, 1, 0);
	//model goes here
	//glutSolidCube(radius0);
	glRotatef(180.0f, 0, 1, 0);
	model->draw();
	glPopMatrix();
}

shot::shot(float terrianScale, float xpos, float ypos, float zpos, float angle1, float angle2): hit_box(terrainScale){
	timeUntilNextStep = 0;
	radius0 = 0.3f;
	x0 = xpos;
	y0 = ypos;
	z0 = zpos;

	angle = angle1;
	angleX = angle2;
	dead = false;


	speed = 1.5f;
	//isTurningLeft = randomFloat() < 0.5f;
	//angle = 2 * M_PI * randomFloat();
	//timeUntilSwitchDir = randomFloat() * (20 * randomFloat() + 15);
}

void shot::bounceOff(hit_box * otherBox){
	speed = 0;
}

void shot::draw(){
	if(!dead){ //So this is a terrible way of doing things
		glPushMatrix();
		glTranslatef(x0, y0, z0);
		glutSolidCube(radius0);
		glPopMatrix();
	}
}

void shot::advance(float dt){
	x0 += float(cos(angle*M_PI/180)) * speed;
	z0 += float(sin(angle*M_PI/180)) * speed;
	y0 += float(-sin(angleX*M_PI/180)) * speed;
}

void enemy::draw(){
	if(!dead){
		glPushMatrix();
		//y will have to be replaced when a proper map is made
		glTranslatef(x0, y0, z0);
		glRotatef(90 - angle * 180 / M_PI, 0, 1, 0);
		glRotatef(90 - angleX * 180 / M_PI, 1, 0, 0);
		//glRotatef(90 - angle * 180 / M_PI, 1, 0, 0);

		//model goes here
		glutSolidSphere(radius0, 20, 20);
		glPopMatrix();
	}
}

void enemy::bounceOff(hit_box * otherBox){
	float vx = velocityX();
	float vz = velocityZ();

	//Find distances between the two
	float dx = otherBox->x() - x0;
	float dz = otherBox->z() - z0;

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

	
	//printf("BALL\n");
}

//Advance movement by dt. Calls step the right number of times
void enemy::advance(float dt){

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
	//printf("Ball %f %f\n", x0, z0);

}
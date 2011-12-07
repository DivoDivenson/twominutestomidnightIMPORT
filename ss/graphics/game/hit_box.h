#include <iostream>
#include <stdio.h>
#include <string.h>
#include <math.h>

#include "GL/gl.h"
#include "GL/glu.h"
#include "GL/glut.h"

//#include "model3DS.h" //for later

const int t_width = 50;
const int t_length = 50;

const float step_time = 0.01f;
//A shot moves along the x and z axis at a given angle to the y-axis
class hit_box{
	protected:
		//model3DS * model;
		float terrainScale;
		float x0;
		float z0;
		float radius0; //Approx radius of model
		float speed;
		//The angle at which the guy is currently walking, in radians.  An angle
		//of 0 indicates the positive x direction, while an angle of PI / 2
		//indicates the positive z direction.  The angle always lies between 0
		//and 2 * PI.
		float angle;
		float timeUntilNextStep;
	
		//These two maybe usless
		bool isTurningLeft;
		float timeUntilSwitchDir;

		void step();



	public:

		hit_box(float terrainScale1);
		
		//Advances the state of the guy by the specified amount of time, by
		//calling step() the appropriate number of times and adjusting animTime
		void advance(float dt);
		void draw();
		float x();
		float z();
		float y();
		float velocityX();
		float velocityZ();
		float radius();
		float walkAngle();
		void bounceOff(hit_box * otherBox);
};

class player: public hit_box{
	public:
		player(float a): hit_box(a){ ; }
		void bounceOff(hit_box * otherBox);
		void advance(float dt);// Advance does nothing to the player
		void update_pos(float xpos, float ypos, float zpos, float angle);
		void draw();

};


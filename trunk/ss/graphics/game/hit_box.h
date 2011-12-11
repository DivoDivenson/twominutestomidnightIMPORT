#include <iostream>
#include <stdio.h>
#include <string.h>
#include <math.h>

#include "GL/gl.h"
#include "GL/glu.h"
#include "GL/glut.h"
#include "model3DS.h"

//#include "model3DS.h" //for later

const int t_width = 150;
const int t_length = 150;

const float step_time = 0.01f;



class hit_box{
	protected:
		//model3DS * model;
		float terrainScale;
		float x0;
		float z0;

		float y0;

		float radius0; //Approx radius of model
		float speed;
		//The angle at which the guy is currently walking, in radians.  An angle
		//of 0 indicates the positive x direction, while an angle of PI / 2
		//indicates the positive z direction.  The angle always lies between 0
		//and 2 * PI.
		float angle;
		float angleX;
		float timeUntilNextStep;

		bool dead;

		model3DS * model;

	
		//These two maybe usless
		bool isTurningLeft;
		bool isGoingUp;
		float timeUntilSwitchDir;

		void step();



	public:

		hit_box(float terrainScale1);
		
		//Advances the state of the guy by the specified amount of time, by
		//calling step() the appropriate number of times and adjusting animTime

		
		float x();
		float z();
		float y();
		float velocityX();
		float velocityZ();
		float velocityY();
		float radius();
		float walkAngle();
		bool isDead();
		virtual void bounceOff(hit_box * otherBox);
		virtual void advance(float dt);
		virtual void draw();
		virtual bool kill();
		virtual void explode();
};



class enemy: public hit_box{
	public:
		enemy(float a, model3DS * model);
		void bounceOff(hit_box * otherBox);
		void advance(float dt);// Advance does nothing to the player
		void draw();
		bool kill(); //Hack to get score counting to work
		void explode();

};

class player: public hit_box{
	public:
		//player(float a): hit_box(a){ ; }
		player(float terrainScale, float xpos, float ypos, float zpos, model3DS * model);
		void bounceOff(hit_box * otherBox);
		void advance(float dt);// Advance does nothing to the player
		void update_pos(float xpos, float ypos, float zpos, float angle);
		void draw();
		bool kill();
		void explode();

};

class shot: public hit_box{
	public:
		shot(float terrainScale, float xpos, float ypos, float zpos, float angle1, float angle2);
		void bounceOff(hit_box * otherBox);
		void advance(float dt);
		void draw();
		bool kill();
		void explode();
};




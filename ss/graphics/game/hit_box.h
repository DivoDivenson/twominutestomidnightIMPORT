#include <iostream>
#include <stdio.h>
#include <string.h>
#include <math.h>

#include "GL/gl.h"
#include "GL/glu.h"
#include "GL/glut.h"

//A shot moves along the x and z axis at a given angle to the y-axis
class hit_box{
	private:
		Vec3f v; //Velocity
		Vec3f pos; //Position
		float r; //Radius. Hit_sphere for the moment
		

	public:

		shot(float x, float y, float z, float angle);
		//Manually update shot position
		void update_shot(float x, float y, float z, float angle);
		void draw();
};
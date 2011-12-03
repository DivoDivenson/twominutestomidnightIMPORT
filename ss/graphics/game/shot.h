#include <iostream>
#include <stdio.h>
#include <string.h>
#include <math.h>

#include "GL/gl.h"
#include "GL/glu.h"
#include "GL/glut.h"

//A shot moves along the x and z axis at a given angle to the y-axis
class shot{
	private:
		float x, y, z;
		float speed;
		float angle;

	public:

		shot(float x, float y, float z, float angle);
		//Manually update shot position
		void update_shot(float x, float y, float z, float angle);
		void draw();
};
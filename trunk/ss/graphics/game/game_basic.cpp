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

using namespace std;

const int NUM_GUYS = 100;

const float TERRIAN_WIDTH = 50.0f;

const float TIME_BETWEEN_HANDLE_COLLISIONS = 0.01f;

float randomFloat(){
	return (float)rand() / ((float)RAND_MAX + 1);
}
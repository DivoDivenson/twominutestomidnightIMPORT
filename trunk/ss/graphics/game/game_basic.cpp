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

#include "model3DS.h"
#include "hit_box.h"

#define SPEED 1

using namespace std;
const int NUM_HIT_BOXS = 40;

const float TERRIAN_WIDTH = 30.0f; //Defines cube region of the game

int cameraZ;
int camX, camY;
//
float xpos = 0, ypos = 0, zpos = 0, xrot = 0, yrot = 0, angle=0.0;
float lastx, lasty;

float cRadius = 10.0f;

GLuint      textureId;

GLfloat white_light[] = {1.0, 1.0, 1.0, .5};
GLfloat left_light_position[] = {-200,20.0,-1, 1.0}; 
GLfloat right_light_position[] = {-1,0,-1, 1.0};

GLfloat emerald_ambient[] =
{0.0215, 0.1745, 0.0215}, emerald_diffuse[] =
{0.07568, 0.61424, 0.07568}, emerald_specular[] =
{0.633, 0.727811, 0.633}, emerald_shininess = 76.8;

const float TIME_BETWEEN_HANDLE_COLLISIONS = 0.01f;




model3DS * car;

vector<hit_box*> _objects;
vector<hit_box*> _shots;
vector<hit_box*> dying; //Draw objects "dying" animation, then delete it
hit_box * _player;

int shoot;



//Collision code from movie tutorials rock

struct box_pair {
	hit_box* hit_box1;
	hit_box* hit_box2;
	//set<hit_box*>::iterator one;
	//set<hit_box*>::iterator two;
	//int one;
	//int two;
};
//The amount of time until we next check for and handle all collisions
float _timeUntilHandleCollisions = 0;
int _numCollisions; //The total number of collisions that have occurred

const int MAX_QUADTREE_DEPTH = 6;
const int MIN_HIT_BOXS_PER_QUADTREE = 2;
const int MAX_HIT_BOXS_PER_QUADTREE = 5;

template <class DstType, class SrcType>
bool IsType(const SrcType* src)
{
  return dynamic_cast<const DstType*>(src) != 0;
}

//Our data structure for making collision detection faster
class Quadtree {
	private:
		float minX;
		float minZ;
		float maxX;
		float maxZ;
		float centerX; //(minX + maxX) / 2
		float centerZ; //(minZ + maxZ) / 2
		
		/* The children of this, if this has any.  children[0][*] are the
		 * children with x coordinates ranging from minX to centerX.
		 * children[1][*] are the children with x coordinates ranging from
		 * centerX to maxX.  Similarly for the other dimension of the children
		 * array.
		 */
		Quadtree *children[2][2];
		//Whether this has children
		bool hasChildren;
		//The hit_boxs in this, if this doesn't have any children
		set<hit_box*> hit_boxs;
		//The depth of this in the tree
		int depth;
		//The number of hit_boxs in this, including those stored in its children
		int numhit_boxs;
		
		//Adds a hit_box to or removes one from the children of this
		void filehit_box(hit_box* hit_box, float x, float z, bool addhit_box) {
			//Figure out in which child(ren) the hit_box belongs
			for(int xi = 0; xi < 2; xi++) {
				if (xi == 0) {
					if (x - hit_box->radius() > centerX) {
						continue;
					}
				}
				else if (x + hit_box->radius() < centerX) {
					continue;
				}
				
				for(int zi = 0; zi < 2; zi++) {
					if (zi == 0) {
						if (z - hit_box->radius() > centerZ) {
							continue;
						}
					}
					else if (z + hit_box->radius() < centerZ) {
						continue;
					}
					
					//Add or remove the hit_box
					if (addhit_box) {
						children[xi][zi]->add(hit_box);
					}
					else {
						children[xi][zi]->remove(hit_box, x, z);
					}
				}
			}
		}
		
		//Creates children of this, and moves the hit_boxs in this to the children
		void haveChildren() {
			for(int x = 0; x < 2; x++) {
				float minX2;
				float maxX2;
				if (x == 0) {
					minX2 = minX;
					maxX2 = centerX;
				}
				else {
					minX2 = centerX;
					maxX2 = maxX;
				}
				
				for(int z = 0; z < 2; z++) {
					float minZ2;
					float maxZ2;
					if (z == 0) {
						minZ2 = minZ;
						maxZ2 = centerZ;
					}
					else {
						minZ2 = centerZ;
						maxZ2 = maxZ;
					}
					
					children[x][z] =
						new Quadtree(minX2, maxX2, minZ2, maxZ2, depth + 1);
				}
			}
			
			//Remove all hit_boxs from "hit_boxs" and add them to the new children
			for(set<hit_box*>::iterator it = hit_boxs.begin(); it != hit_boxs.end();
					it++) {
				hit_box* hit_box = *it;
				filehit_box(hit_box, hit_box->x(), hit_box->z(), true);
			}
			hit_boxs.clear();
			
			hasChildren = true;
		}
		
		//Adds all hit_boxs in this or one of its descendants to the specified set
		void collecthit_boxs(set<hit_box*> &gs) {
			if (hasChildren) {
				for(int x = 0; x < 2; x++) {
					for(int z = 0; z < 2; z++) {
						children[x][z]->collecthit_boxs(gs);
					}
				}
			}
			else {
				for(set<hit_box*>::iterator it = hit_boxs.begin(); it != hit_boxs.end();
						it++) {
					hit_box* hit_box = *it;
					gs.insert(hit_box);
				}
			}
		}
		
		//Destroys the children of this, and moves all hit_boxs in its descendants
		//to the "hit_boxs" set
		void destroyChildren() {
			//Move all hit_boxs in descendants of this to the "hit_boxs" set
			collecthit_boxs(hit_boxs);
			
			for(int x = 0; x < 2; x++) {
				for(int z = 0; z < 2; z++) {
					delete children[x][z];
				}
			}
			
			hasChildren = false;
		}
		
		//Removes the specified hit_box at the indicated position
		void remove(hit_box* hit_box, float x, float z) {
			numhit_boxs--;
			
			if (hasChildren && numhit_boxs < MIN_HIT_BOXS_PER_QUADTREE) {
				destroyChildren();
			}
			
			if (hasChildren) {
				filehit_box(hit_box, x, z, false);
			}
			else {
				hit_boxs.erase(hit_box);
			}
		}
	public:
		//Constructs a new Quadtree.  d is the depth, which starts at 1.
		Quadtree(float minX1, float minZ1, float maxX1, float maxZ1, int d) {
			minX = minX1;
			minZ = minZ1;
			maxX = maxX1;
			maxZ = maxZ1;
			centerX = (minX + maxX) / 2;
			centerZ = (minZ + maxZ) / 2;
			
			depth = d;
			numhit_boxs = 0;
			hasChildren = false;
		}
		
		~Quadtree() {
			if (hasChildren) {
				destroyChildren();
			}
		}
		
		//Adds a hit_box to this
		void add(hit_box* hit_box) {
			numhit_boxs++;
			if (!hasChildren && depth < MAX_QUADTREE_DEPTH &&
				numhit_boxs > MAX_HIT_BOXS_PER_QUADTREE) {
				haveChildren();
			}
			
			if (hasChildren) {
				filehit_box(hit_box, hit_box->x(), hit_box->z(), true);
			}
			else {
				hit_boxs.insert(hit_box);
			}
		}
		
		//Removes a hit_box from this
		void remove(hit_box* hit_box) {
			remove(hit_box, hit_box->x(), hit_box->z());
		}
		
		//Changes the position of a hit_box in this from the specified position to
		//its current position
		void hit_boxMoved(hit_box* hit_box, float x, float z) {
			remove(hit_box, x, z);
			add(hit_box);
		}
		
		//Adds potential collisions to the specified set
		void potentialCollisions(vector<box_pair> &collisions) {
			if (hasChildren) {
				for(int x = 0; x < 2; x++) {
					for(int z = 0; z < 2; z++) {
						children[x][z]->potentialCollisions(collisions);
					}
				}
			}
			else {
				//Add all pairs (hit_box1, hit_box2) from hit_boxs
				for(set<hit_box*>::iterator it = hit_boxs.begin(); it != hit_boxs.end();
						it++) {
					hit_box* hit_box1 = *it;
					for(set<hit_box*>::iterator it2 = hit_boxs.begin();
							it2 != hit_boxs.end(); it2++) {
						hit_box* hit_box2 = *it2;
						//This test makes sure that we only add each pair once
						if (hit_box1 < hit_box2) {
							box_pair gp;
							gp.hit_box1 = hit_box1;
							gp.hit_box2 = hit_box2;
							collisions.push_back(gp);
						}
					}
				}
			}
		}
};

void potentialCollisions(vector<box_pair> &cs, Quadtree* quadtree) {
	quadtree->potentialCollisions(cs);
}

//Returns whether hit_box1 and hit_box2 are currently colliding
bool testCollision(hit_box* hit_box1, hit_box* hit_box2) {
	float dx = hit_box1->x() - hit_box2->x();
	float dz = hit_box1->z() - hit_box2->z();
	float dy = hit_box1->y() - hit_box2->y();
	float r = hit_box1->radius() + hit_box2->radius();
	//Not sure If i can just extend the maths like this....
	if (dx * dx + dz * dz + dy * dy < r * r) {
		float vx = hit_box1->velocityX() - hit_box2->velocityX();
		float vz = hit_box1->velocityZ() - hit_box2->velocityZ();
		float vy = hit_box1->velocityY() - hit_box2->velocityY();
		return vx * dx + vz * dz + vy * dy < 0;
	}
	else {
		return false;
	}
}

//Should really have a seperate data structure to contain the shots
void handleCollisions(vector<hit_box*> &hit_boxs,
					  Quadtree* quadtree,
					  int &numCollisions) {
	vector<box_pair> gps;
	potentialCollisions(gps, quadtree);
	for(unsigned int i = 0; i < gps.size(); i++) {
		box_pair gp = gps[i];
		
		hit_box* g1 = gp.hit_box1;
		hit_box* g2 = gp.hit_box2;
		if (testCollision(g1, g2)) {
			g1->bounceOff(g2);
			g2->bounceOff(g1);
			numCollisions++;
			//if either object is a shot, get rid of them both
			//The following code sucks. HARD
			shot * v1 = dynamic_cast<shot*>(g1);
			shot * v2 = dynamic_cast<shot*>(g2);
			if(v1 != 0 || v2 != 0){
				dying.push_back(g1);
				dying.push_back(g2);
				g1->kill();
				g2->kill();
				quadtree->remove(g1);
				quadtree->remove(g2);
			}
		}
	}
}

//This is pretty horrible but is the only quick way I could think off. Also it doesn't work.
//Can't delete from vector you'r iterating over. Makes sense
void clean(vector<hit_box*> &hit_boxs){
	for(unsigned int i = 0; i < hit_boxs.size(); i++){
		if(hit_boxs[i]->isDead()){
			fprintf(stderr,"%d is dead\n", i);
			hit_boxs.erase(hit_boxs.begin()+1);
		}else{
			fprintf(stderr,"%d is not dead\n", i);

		}
	}
}

void clean_shots(vector<hit_box*> &hit_boxs){
	int border = 20;
	for(unsigned int i = 0; i < hit_boxs.size(); i++){
		//This is gonna be a long if statement
		if(hit_boxs[i]->x() < -border || hit_boxs[i]->y() < -border || hit_boxs[i]->z() < -border ||
			hit_boxs[i]->x() > TERRIAN_WIDTH + border || 
			hit_boxs[i]->y() > TERRIAN_WIDTH + border || hit_boxs[i]->z() > TERRIAN_WIDTH + border){
			hit_boxs.erase(hit_boxs.begin()+1);

		}else{

		}
	}
}

//Check if the player collides with anything. Linear for the moment, get working with 
//quad tree later
void playerCollide(hit_box * _player, vector<hit_box*> &hit_boxs){
	for(unsigned int i = 0; i < hit_boxs.size(); i++){
		if(testCollision(_player, hit_boxs[i])){
			_player->bounceOff(hit_boxs[i]);
			//hit_boxs[i]->bounceOff(_player);
			hit_boxs.erase(hit_boxs.begin() + i);
		}
	}
}

//Moves the hit_boxs over the given interval of time, without handling collisions
//Quad tree ignoers Y direction for the moment
void movehit_boxs(vector<hit_box*> &hit_boxs, Quadtree* quadtree, float dt) {
	for(unsigned int i = 0; i < hit_boxs.size(); i++) {
		hit_box* hit_box = hit_boxs[i];
		float oldX = hit_box->x();
		float oldZ = hit_box->z();
		hit_box->advance(dt);
		quadtree->hit_boxMoved(hit_box, oldX, oldZ);
	}
}

//Advances the state of the hit_boxs over the indicated interval of time
void advance(vector<hit_box*> &hit_boxs,
			 Quadtree* quadtree,
			 float t,
			 float &timeUntilHandleCollisions,
			 int &numCollisions) {
	while (t > 0) {
		if (timeUntilHandleCollisions <= t) {
			movehit_boxs(hit_boxs, quadtree, timeUntilHandleCollisions);
			handleCollisions(hit_boxs, quadtree, numCollisions);
			t -= timeUntilHandleCollisions;
			timeUntilHandleCollisions = TIME_BETWEEN_HANDLE_COLLISIONS;
		}
		else {
			movehit_boxs(hit_boxs, quadtree, t);
			timeUntilHandleCollisions -= t;
			t = 0;
		}
	}
}

Quadtree* _quadtree;

void keypress(unsigned char key, int x, int y){
	
	   if (key=='q')
    {
    xrot += 1;
    if (xrot >360) xrot -= 360;
    }

    if (key=='z')
    {
    xrot -= 1;
    if (xrot < -360) xrot += 360;
    }

    if (key=='w')
    {
    float xrotrad, yrotrad;
    yrotrad = (yrot / 180 * M_PI);
    xrotrad = (xrot / 180 * M_PI); 
    xpos += float(sin(yrotrad) * SPEED);
    zpos -= float(cos(yrotrad) * SPEED);
    ypos -= float(sin(xrotrad) * SPEED);
    }

    if (key=='s')
    {
    float xrotrad, yrotrad;
    yrotrad = (yrot / 180 * M_PI);
    xrotrad = (xrot / 180 * M_PI); 
    xpos -= float(sin(yrotrad) * SPEED);
    zpos += float(cos(yrotrad) * SPEED) ;
    ypos += float(sin(xrotrad) * SPEED);
    }

    if (key=='d')
    {
    	float yrotrad;
		yrotrad = (yrot / 180 * M_PI);
		xpos += float(cos(yrotrad)) * SPEED;
		zpos += float(sin(yrotrad)) * SPEED;
    }

    if (key=='a')
    {
   		float yrotrad;
		yrotrad = (yrot / 180 * M_PI);
		xpos -= float(cos(yrotrad)) * SPEED;
		zpos -= float(sin(yrotrad)) * SPEED;
    }
    if (key==27)
    {
    exit(0);
    }

}

void mouse_func(int button, int state, int x, int y){
	if(button == GLUT_LEFT_BUTTON && state == GLUT_DOWN){
		shoot++;
		//_quadtree->add(_objects.at(_objects.end()));
	}else if(button == GLUT_LEFT_BUTTON && state == GLUT_UP){
		shoot = 0;
	}else if(button == 4){ //Zooming
		cRadius += 0.5f;
	}else if(button == 3){
		cRadius -= 0.5f;
	}
}



void mouseMove(int x, int y){
	int diffx=x-lastx; //check the difference between the current x and the last x position
	int diffy=y-lasty; //check the difference between the current y and the last y position
	lastx=x; //set lastx to the current x position
	lasty=y; //set lasty to the current y position
	xrot += (float) diffy; //set the xrot to xrot with the addition of the difference in the y position
	yrot += (float) diffx;// set the xrot to yrot with the addition of the difference in the x position
    if(yrot > 360.0f){
        yrot -= 360.0f;
    }
}





void init(){

	std::cout<<"Initializing scene..."<<std::endl;
	cameraZ = -1000;
	camX =0;
	camY =0;
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	//Set up Lighting Stuff
	glLightfv(GL_LIGHT0, GL_POSITION, left_light_position);
	glLightfv(GL_LIGHT0, GL_SPECULAR, white_light);
	glEnable(GL_LIGHT0);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, white_light);
	glEnable(GL_NORMALIZE);
	glEnable(GL_COLOR_MATERIAL);
	glShadeModel(GL_SMOOTH);

	car = new model3DS("./car/car.3ds", 0.05f);


	
}

vector<hit_box*> makeObjects(int number){
	vector<hit_box*> objects;
	for(int i = 0; i < number; i++){
		objects.push_back(new enemy(1.0f));
	}
	return objects;
}

void setViewport(int width, int height){
	
    // Work out window ratio, avoid divide-by-zero
    if(height==0)height=1;
	float ratio = float(width)/float(height);

	// Reset projection matrix
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	
	// Fill screen with viewport
	glViewport(0, 0, width, height);

	// Set a 45 degree perspective
	gluPerspective(45, ratio, .1, 2000);

}
//need to trim this
void enable(){

	glEnable (GL_DEPTH_TEST); //enable the depth testing
    glEnable (GL_LIGHTING); //enable the lighting
    glEnable (GL_LIGHT0); //enable LIGHT0, our Diffuse Light
    glShadeModel (GL_SMOOTH); //set the shader to smooth shader
	glEnable(GL_TEXTURE_2D);
	//glColor3f(1.0,1.0,1.0);
    glBindTexture(GL_TEXTURE_2D,textureId);
    

}


void updateScene(int value){
	/*for (unsigned int i = 0; i < _objects.size(); i++){
		_objects[i]->advance(0.01f);
	}*/

	if(shoot == 3){
		//_objects.push_back(new shot(1.0f, xpos, ypos, zpos, yrot -90, xrot));
		_shots.push_back(new shot(1.0f, xpos, ypos, zpos, yrot -90, xrot));
		//shoot =0;

	}else if(shoot != 0){
		shoot++;
	}
	advance(_objects, _quadtree, 0.025f, _timeUntilHandleCollisions, _numCollisions);
	advance(_shots, _quadtree, 0.00000001f, _timeUntilHandleCollisions, _numCollisions);
	clean_shots(_shots);
	playerCollide(_player, _objects);
	glutPostRedisplay();
	glutTimerFunc(25, updateScene, 0);

}

float oldX, oldZ;
void renderScene(){
	       
    // Clear framebuffer & depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	enable();

	glTranslatef(0.0f, -2.0f, -cRadius);
	glRotatef(xrot, 1.0f, 0.0f, 0.0f);
	//glutSolidCube(1);
	_player->draw(); //Using the draw function to update the position. Whatever man
	oldX = _player->x();
	oldZ = _player->z();
	dynamic_cast<player*>(_player)->update_pos(xpos, ypos, zpos, yrot);
	//Presume the player moves every scene
	_quadtree->hit_boxMoved(_player, oldX, oldZ);



	glRotatef(yrot,0.0,1.0,0.0);  //rotate our camera on the y-axis

    glTranslatef(-xpos,-ypos,-zpos); //translate the screen to the position of our camera
   // glTranslatef(-xpos,1.0f,-zpos); //translate the screen to the position of our camera



	for (unsigned int i = 0; i < _objects.size(); i++){
		_objects[i]->draw();
	}

	for(unsigned int i = 0; i < dying.size(); i++){
		;//dying[i]->draw();
		//check if it's done and remove it
	}

	for(unsigned int i =0; i < _shots.size(); i++){
		_shots[i]->draw();
	}

	glutSwapBuffers();
	
}


int main(int argc, char ** argv){
	srand((unsigned int)time(0));

	shoot = false;

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(1280, 1000);

	glutCreateWindow("A Game");
	init();

	_objects = makeObjects(NUM_HIT_BOXS);
	_player = new player(1.0f, 0.0f, 0.0f, 0.0f);
	//_objects.push_back(_player);

	_quadtree = new Quadtree(0.0f, 0.0f, TERRIAN_WIDTH, TERRIAN_WIDTH, 1);
	//_quadtree->add(_player);

	for(unsigned int i =0; i < _objects.size(); i++){
		_quadtree->add(_objects[i]);
	}


	glutDisplayFunc(renderScene);
	glutReshapeFunc(setViewport);
	glutKeyboardFunc(keypress);
	glutMouseFunc(mouse_func);
    glutPassiveMotionFunc(mouseMove);
    glutMotionFunc(mouseMove);
    glutTimerFunc(25, updateScene, 0);
    //glutMouseFunc(mouse_func);


	glutMainLoop();
	return 0; //Just for kicks
}
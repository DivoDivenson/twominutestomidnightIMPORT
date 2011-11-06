#include <iostream>     // I/O
#include <GL/glut.h>     // for gluPerspective & gluLookAt
//#include <GL/freeglut.h>
#include <sys/time.h>
void setupScene();
void updateScene();
void renderScene();
void exitScene();
void keypress(unsigned char key, int x, int y);
void setViewport(int width, int height);

typedef unsigned int DWORD;


int         rotationAngle=0;
bool        wireframe=false;
int         windowId;
int win_height = 640;
int win_width = 480;

DWORD       lastTickCount;


GLfloat white_light[] = {1.0, 1.0, 1.0, 1.0};
GLfloat left_light_position[] = {1,0,-1, 1.0}; 
GLfloat right_light_position[] = {-1,0,-1, 1.0};

GLfloat emerald_ambient[] =
{0.0215, 0.1745, 0.0215}, emerald_diffuse[] =
{0.07568, 0.61424, 0.07568}, emerald_specular[] =
{0.633, 0.727811, 0.633}, emerald_shininess = 76.8;

void drawObjects(int move = 0){
//Stick this here for the moment, lazy
	GLUquadric* nQ;
    nQ=gluNewQuadric();
    GLUquadric * nQ2;
    nQ2 = gluNewQuadric();

	glutSolidCube(1.0f);
    
    //glTranslatef(0.0f, -1.0f, 0.0f);
    //rotate everything above the base
    if(move){
        glRotatef(-1*rotationAngle/4.f,0,1,0);
        glRotatef(-1*rotationAngle/2.f,0,0,1);
        glRotatef(-1*rotationAngle/3.f,1,0,0);
    }

//  glPushMatrix();
    //draw first joint
    glutSolidSphere(0.3f, 20, 20);  

    //cylinder, draw upper arm
    glPushMatrix();
    glRotatef(90.0f, -1.0f, 0.0f, 0.0f);
    gluCylinder(nQ, 0.15, 0.15, 1.5f, 20, 5);
    //glRotatef(90.0f, 1.0f, 0.0f, 0.0f); //Same as pop
    glPopMatrix(); //Undo the rotate
    
    glTranslatef(0.0f, 1.7f, 0.0f);
    //Rotate the lower arm
    if(move){
        glRotatef(-1*rotationAngle/4.f,0,1,0);
        glRotatef(-1*rotationAngle/3.f,1,0,0);
        glRotatef(-1*rotationAngle/2.f,0,0,1);
    }
    //second join
    glutSolidSphere(0.3f, 20, 20);
    //Horizontal cylinder for lower arm
    gluCylinder(nQ2, 0.15, 0.15, 1.5f, 20, 5);
	glTranslatef(0.0f, 0.0f, 1.5f);
	glutSolidSphere(0.3f, 20, 20);
}
 
void renderScene(){
        
    // Clear framebuffer & depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glClearColor(0.2f, 0.2f, 0.9f, 1.0f);

    //Enable lighting
    glEnable(GL_LIGHTING);

    //Set the material properties
    glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, emerald_ambient);
    glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, emerald_diffuse);
    glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, emerald_specular);
    glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, emerald_shininess);   

    //Top left; top view
	glViewport(0, win_height/2, win_width/2, win_height/2);
    // Reset Modelview matrix       
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

	glOrtho(-3.0, 3.0, -3.0, 3.0, 1.0, 50.0);
	gluLookAt(0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0);
	glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

	drawObjects();

    //Top right; right view
    glViewport(win_width/2, win_height/2, win_width/2, win_height/2);
    // Reset Modelview matrix       
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

    glOrtho(-3.0, 3.0, -3.0, 3.0, 1.0, 50.0);
    gluLookAt(5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    drawObjects();

     //Bottom left; front view
    glViewport(0, 0, win_width/2, win_height/2);
    // Reset Modelview matrix       
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

    gluPerspective(70.0, 1.0, 1, 50);
    gluLookAt(0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    drawObjects();

     //Bottom right; rotating
    glViewport(win_width/2, 0, win_width/2, win_height/2);
    // Reset Modelview matrix       
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

    gluPerspective(120.0, 1.0, 1, 50);    
    gluLookAt(0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
    glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);

    drawObjects(1);


   

  
    glPopMatrix(); //restore the state of the modelview matrix
    // Swap double buffer for flicker-free animation
    glutSwapBuffers();
        
}

void updateScene(){
    timeval tim;
    gettimeofday(&tim,NULL);
    // Wait until at least 16ms passed since start of last frame
    // Effectively caps framerate at ~60fps
    double t2 = tim.tv_sec +(tim.tv_usec/1000000.0);
    while(t2-lastTickCount<16);
    lastTickCount=gettimeofday(&tim,NULL);
    
    // Increment angle for next frame
    rotationAngle+=2;

    // Do any other updates here
    
    // Draw the next frame*/
    glutPostRedisplay();

}

void keypress(unsigned char key, int x, int y){
    
    // Test if user pressed ESCAPE (ascii 27)
    // If so, exit the program
    if(key==27){
        exitScene();
    }

    // 'W' key toggles wireframe mode on & off
    if(key == 'w' || key == 'W'){
        wireframe=!wireframe;
        if(wireframe){
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
        else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    // Other possible keypresses go here
    //if(key == 'a'){...}
}

void setupScene(){

    std::cout<<"Initializing scene..."<<std::endl;
    
    //Set up Lighting Stuff
    glLightfv(GL_LIGHT0, GL_POSITION, left_light_position);
    glLightfv(GL_LIGHT0, GL_SPECULAR, white_light);
    glLightfv(GL_LIGHT1, GL_POSITION, right_light_position);
    glLightfv(GL_LIGHT1, GL_SPECULAR, white_light);
    glEnable(GL_LIGHT0);
    glEnable(GL_LIGHT1);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, white_light);
    glLightfv(GL_LIGHT1, GL_DIFFUSE, white_light);
    glShadeModel(GL_SMOOTH);

    glEnable(GL_DEPTH_TEST);

    // Generate GL texture ID & load texture
//    glGenTextures(1, &textureId);
    //textureTga logoTexture("BushRumsfeld2.tga", textureId);
//  textureTga logoTexture("BushRumsfeld.tga", textureId);
        
}

void exitScene(){

    std::cout<<"Exiting scene..."<<std::endl;

    // Close window
    glutDestroyWindow(windowId);

    // Free any allocated memory

    // Exit program
    exit(0);
}

void setViewport(int width, int height) {

    // Work out window ratio, avoid divide-by-zero
    if(height==0)height=1;
    float ratio = float(width)/float(height);
    win_width = width;
    win_height = height;

    // Reset projection matrix
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    
    // Fill screen with viewport
    glViewport(0, 0, width, height);

    // Set a 45 degree perspective
    gluPerspective(45, ratio, .1, 1000);

}

int main(int argc, char *argv[]){
        
    // Initialise OpenGL
    glutInit(&argc, argv); 

    // Set window position, size & create window
    glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
    glutInitWindowPosition(50,50);
    glutInitWindowSize(win_height,win_width);
    windowId = glutCreateWindow("Graphics Lab 2: Hierarchical Transformations");
    
    // Set GLUT callback functions
    glutReshapeFunc(setViewport);
    glutDisplayFunc(renderScene);
    glutIdleFunc(updateScene);
    glutKeyboardFunc(keypress);

    // Setup OpenGL state & scene resources (models, textures etc)
    setupScene();

    // Show window & start update loop
    glutMainLoop();    

    return 0;
    
}

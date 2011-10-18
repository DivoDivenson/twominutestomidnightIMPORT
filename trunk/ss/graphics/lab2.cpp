#include <windows.h>    // for timeGetTime()
#include <mmsystem.h>    // ditto
#include <iostream>        // I/O
#include "glut/glut.h"


void setupScene();
void updateScene();
void renderScene();
void exitScene();
void keypress(unsigned char key, int x, int y);
void setViewport(int width, int height);


int         rotationAngle=0;
bool        wireframe=false;
int         windowId;
DWORD        lastTickCount;
GLfloat white_light[] = {1.0, 1.0, 1.0, 1.0};
GLfloat left_light_position[] = {1,0,-1, 1.0}; 
GLfloat right_light_position[] = {-1,0,-1, 1.0};

GLfloat emerald_ambient[] =
{0.0215, 0.1745, 0.0215}, emerald_diffuse[] =
{0.07568, 0.61424, 0.07568}, emerald_specular[] =
{0.633, 0.727811, 0.633}, emerald_shininess = 76.8;
 
void renderScene(){
        
    // Clear framebuffer & depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    //Enable lighting
         glEnable(GL_LIGHTING);

    //Set the material properties
        glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, emerald_ambient);
        glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, emerald_diffuse);
        glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, emerald_specular);
        glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, emerald_shininess);   

    // Reset Modelview matrix          
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    
    GLUquadric* nQ;
    nQ=gluNewQuadric();

    // Set view position & direction
    // (Camera at (0,0,5) looking down the negative Z-axis)
    gluLookAt(0,0,5,  0,0,-1,  0,1,0);

    //Rotate for so i can see what i'm doing
    glRotatef(45.0f, 1.0f, 1.0f, 0.0f);
    glTranslatef(0.0, -1.0, 0.0);

    //Draw cube at origin
    glutSolidCube(1.0f);

    //0.5 + 1
    glTranslatef(0.0f, 0.5f, 0.0f);
    //Rotate to rotate joint here
    glRotatef(90.0f, -1.0, 0.0, 0.0f);
    gluCylinder(nQ, 0.15, 0.15, 1.0f, 20, 5);
    glRotatef(90.0f, 1.0, 0.0, 0.0f);

    glTranslatef(0.0f, 1.0f, 0.0f);



    //To setup the creation of quadric objects
    /*GLUquadric* nQ;
    nQ=gluNewQuadric();

    glPushMatrix(); //remember the current state of the modelview matrix

    //Draw cube at origin
    glTranslatef(0.0, 2.0f, 0.0f);
    gluCylinder(nQ, 0.15, 0.15, 2, 20, 5); 

    glPopMatrix();

    glPushMatrix();
    glutSolidCube(1.0f);
    //Rotate and draw a lit cylinder using glu

    /*glTranslatef(1.25,0.0,-2.0);
    //glRotatef(-1*rotationAngle/4.f,0,1,0);
   // glRotatef(-1*rotationAngle/3.f,1,0,0);
   // glRotatef(-1*rotationAngle/2.f,0,0,1);    

    gluCylinder(nQ, 0.15, 0.15, 1.5, 20, 5); 

    glPopMatrix(); //restore the state of the modelview matrix
   
    glPushMatrix(); //remember the current state of the modelview matrix
    glTranslatef(-1.25,0.0,-2.0);
    //glRotatef(rotationAngle/4.f,0,1,0);
   // glRotatef(rotationAngle/3.f,1,0,0);
    //glRotatef(rotationAngle/2.f,0,0,1);    

    //Rotate and draw a lit sphere using glut


    glutSolidSphere(0.5, 20, 20);
    */

    glDisable(GL_LIGHTING);
    glPopMatrix(); //restore the state of the modelview matrix

    // Swap double buffer for flicker-free animation
    glutSwapBuffers();
        
}

void updateScene(){
    
    // Wait until at least 16ms passed since start of last frame
    // Effectively caps framerate at ~60fps
    while(timeGetTime()-lastTickCount<16);
    lastTickCount=timeGetTime();
    
    // Increment angle for next frame
    rotationAngle+=2;

    // Do any other updates here
    
    // Draw the next frame
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
//    textureTga logoTexture("BushRumsfeld.tga", textureId);
        
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
    glutInitWindowSize(640,480);
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


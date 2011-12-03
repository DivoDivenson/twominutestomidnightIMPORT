#include <btBulletCollisionCommon.h>
#include <iostream>


int main(){
	std::cout << "Hello World" << std::endl;

	btBroadphaseInterface * broadphase = new btDbvtBroadphase();


	btDefaultCollisionConfiguration * collisionConfiguration = new btDefaultCollisionConfiguration();
	btCollisionDispatcher * dispatcher = new btCollisionDispatcher(collisionConfiguration);

	//btSequentialImpulseConstraintSolver * solver = new btSequentialImpulseConstraintSolver();




	return 0;
}
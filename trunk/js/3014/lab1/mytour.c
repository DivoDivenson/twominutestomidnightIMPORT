
/*#include "node.h"
#include "sort.c"
//Putting node code here, might put it in its own file later

struct node * insert_Node(const point cities[], int depth){
   struct node * new;
   new = malloc(sizeof(struct node));
   new->parent = NULL;
   printf("%d\n",depth);
   new->cord = cities[depth];
   //new->less = insert_Node(cities,depth+1);
   return new;
}


struct node * newTree(const point cities[], int ncities){
   struct node * result;
   result = malloc(sizeof(struct node));
   result->parent = insert_Node(cities,0);
   return result;
}


void my_tour(const point cities[], int tour[], int ncities){
   struct node * new;
   new = newTree(cities, ncities); 
   printf("%f\t%f\n",cities[0].x,cities[0].y);
   swap(&cities[0], &cities[1]);
   printf("%f\t%f\n",cities[0].x,cities[0].y);
}*/


//Remove the sqrt as the actual distance is not important, just the relative
//distance between cities.

double distSqr(const point cities[], int i, int j) {
  return sqr(cities[i].x-cities[j].x)+
	      sqr(cities[i].y-cities[j].y);
}

int pPoint(const point *in){
   printf("X: %f \t Y: %f\n", in->x, in->y);
}



void my_tour(const point cities[], int tour[], int ncities)
{
//  simple_find_tour(cities, tour, ncities);
  int i,j;
  char *visited = alloca(ncities);
  int ThisPt, ClosePt=0;
  double CloseDist, temp;
  int endtour=0;
  //Cleans the array. Some machines do this @ malloc, stoker does not.
  for (i=0; i<ncities; i++)
    visited[i]=0;

  ThisPt = ncities-1;
//  visited[ncities-1] = 1;
  visited[ThisPt] = 1; //Yes I am clutching at straws now. Compiler probably does this anyway
  tour[endtour++] = ThisPt;

  printf("Starting tour\n");
  // Goes through array linearly. Prehaps sort array or come up with some crazy way to step through it.
  // Sorting creates a completly different tour.... 
  for (i=1; i<ncities; i++) {
    CloseDist = DBL_MAX;
    for (j=0; j<ncities-1; j++) {
      if (!visited[j]) {
         temp = distSqr(cities, ThisPt, j);
      	if (temp < CloseDist) { //j starts at beginning of array, ThisPt at the end.
            //CloseDist = distSqr(cities, ThisPt, j); //Doing this twice, compiler probably catches it...
            //Well that made no difference at all.
            CloseDist = temp;
	         ClosePt = j;
	      }
      }
    }
    tour[endtour++] = ClosePt;
    //pPoint(&cities[ClosePt]);
    visited[ClosePt] = 1;
    ThisPt = ClosePt;
  }
}




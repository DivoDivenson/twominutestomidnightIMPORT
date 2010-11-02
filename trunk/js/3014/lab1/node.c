#include "node.h"

struct node * newTree(const point cities[]){
   struct node * result;
   result = malloc(sizeof(struct node));
   result->parent = NULL;
   return result;
}


#include <stdio.h>
#include <stdlib.h>

struct listNode{
   int value;
   struct listNode * next;
};

struct memPool{
   struct listNode ** nodes;
   struct listNode * p;
   int size; //Size of a "section" of the memory pool
};

struct memPool * memPool_new(int size){
   struct memPool * result;
   result = malloc(sizeof(struct memPool));
   result->nodes = malloc(sizeof(struct listNode) * size);
   result->p = result->nodes[0];
   result->size = size;
   return result;
   
}

struct listNode * memPool_allocate( struct memPool * pool, int value){
   if(pool->p+1 == NULL){ //If at end of list get some more memoryNOT SURE IF THIS WILL WORK
      //Try this first, if not use pointer to continue in
      //pool->nodes = malloc(sizeof(struct listNode) * (pool->size * 2));
      //pool->size = pool->size * 2;
      printf("Out of memory\n");
   }

   pool->nodes[0]->value = value;
   //pool->p = pool->nodes[1];
   //printf("%d\n%d\n\n", pool->p, pool->p-1);
   return pool->p;
}


int main(){
   struct memPool * temp = memPool_new(10);
   struct listNode * new = memPool_allocate(temp,1);
   struct listNode * two = memPool_allocate(temp,2);
   struct listNode * three = memPool_allocate(temp,3);
   printf("%d\n%d\n%d\n", new->value, two->value, three->value);
   return 0;
}

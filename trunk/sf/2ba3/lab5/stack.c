#include <stdio.h>
#include <stdlib.h>
#include "stack.h"

struct stack * newStack(int size){
   struct stack * result;
   result = malloc(sizeof(struct stack));
   result -> value = malloc(sizeof(double) * size);
   result -> size = size;
   result -> sp = 0;
   return result;
}


void push(struct stack * this, double item){
   this -> value[this -> sp] = item;
   this -> sp++;
}

double pop(struct stack * this){
   this -> sp--;
   return this -> value[this -> sp];
}


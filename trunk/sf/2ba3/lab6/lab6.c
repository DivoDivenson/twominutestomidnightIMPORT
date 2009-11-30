#include <stdio.h>
#include <stdlib.h>
#include "bitset.h"

int main(){

   struct bitset * mybitset;
   mybitset = bitset_new(8); //Make pointer from int without cast. What?
   printf("%s\n",print_set(mybitset));
   return 0;
    
}



#include <stdio.h>
#include <stdlib.h>
#include "bitset.h"

int main(){

   struct bitset *mybitset;
   mybitset = bitset_new(16); //Make pointer from int without cast. What?
   bitset_add(mybitset,2);
   printf("%d\n",bitset_lookup(mybitset,2));
   return 0;
    
}



#include <stdio.h>
#include <stdlib.h>
#include "bitset.h"

int main(){

   struct bitset *mybitset,* myotherbitset;
   mybitset = bitset_new(8); //Make pointer from int without cast. What?
   myotherbitset = bitset_new(8);
   bitset_add(mybitset,2);
   bitset_add(mybitset,1);
   printf("%d   %d\n",mybitset->bits[0],bitset_lookup(mybitset,14));
   bitset_remove(mybitset,14);
   printf("%d   %d\n",mybitset->bits[0],bitset_lookup(mybitset,8));
   struct bitset *setunion;
   setunion = mk_union(mybitset, myotherbitset);
   printf("%s\n",print_set(setunion));
   return 0;
    
}



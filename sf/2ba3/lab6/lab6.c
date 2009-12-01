#include <stdio.h>
#include <stdlib.h>
#include "bitset.h"

int main(){

   struct bitset *mybitset,* myotherbitset;
   mybitset = bitset_new(16); //Make pointer from int without cast. What?
   myotherbitset = bitset_new(20);
   bitset_add(mybitset,14);
   bitset_add(mybitset,1);
   printf("%d   %d\n",mybitset->bits[0],bitset_lookup(mybitset,14));
   bitset_remove(mybitset,14);
   printf("%d   %d\n",mybitset->bits[0],bitset_lookup(mybitset,14));
   mk_union(mybitset, myotherbitset);
   return 0;
    
}



#include "listset.h"
#include <stdio.h>
#include <stdlib.h>

/* Almost done. Get rid of temp ballox in listset.c and write the test code */

int main(){
   struct listset * new;
   new = listset_new();
   listset_add(new, 12);
   listset_add(new,13);
   listset_add(new,12);
   listset_add(new,77);
   printlist(new);
   listset_remove(new,77);
   //listset_remove(new,13);
   printf("Demo remove\n");
   printlist(new);

   struct listset * two = listset_new();
   listset_add(two,12);
   listset_add(two,14);
   printf("Union\n");
   printlist(set_union(new,two));
   printf("Intersect\n");
   printlist(set_intersect(new,two));
   return 0;
}

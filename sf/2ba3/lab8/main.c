#include "listset.h"
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
struct listset * read_in(){
  struct listset * result = listset_new();
  printf("Enter: ");
  char temp[100];
  fgets(temp,100,stdin);
  int i;
  for(i = 0;temp[i] != '\0';i++){
    listset_add(result,temp[i]);
    printf("%d\n",temp[i]);
  }

}

int main(){

  /*Start quick demo code */
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

   /*End demo*/
   struct listset * input1 = read_in();
   struct listset * input2 = read_in();
   printf("First Line:\n");
   printlist(input1);
   printf("Second Line:\n");
   printlist(input2);
   return 0;
}

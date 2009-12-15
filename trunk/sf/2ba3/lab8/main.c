#include "listset.h"
#include <stdio.h>
#include <stdlib.h>

/*Reads in data from user and puts it in a new linked list*/
struct listset * readlist(){
  struct listset * result = listset_new();
  
  printf("Please enter in:\n");
  char temp[100];
  fgets(temp,100,stdin);
  int input;
  while(sscanf("%d",&input)){
    listset_add(result,input);
  }
  return result;
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
   struct listset * input1 = readlist();
   struct listset * input2 = readlist();
   printlist(input1);
   return 0;
}

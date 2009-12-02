#include <stdio.h>
#include <stdlib.h>
#include "bitset.h"
#define MAX 100

/*Parse a string, writting the elements to a bitset
  Return the bitset */
struct bitset * parse_string(char *input){
  struct bitset * result;
  result = bitset_new(256);

  int i;
  for(i =0;input[i] != '\0'; i++){
    bitset_add(result,input[i]);
  }
  return result;
}

int main(){

   struct bitset *mybitset,* myotherbitset;
   /*Checking
   mybitset = bitset_new(17); //Make pointer from int without cast. What?
   myotherbitset = bitset_new(8);
   bitset_add(mybitset,2);
   bitset_add(mybitset,1);
   bitset_add(myotherbitset,4);
   printf("%d   %d\n",mybitset->bits[0],bitset_lookup(mybitset,2));
   printf("%d   %d\n",mybitset->bits[0],bitset_lookup(mybitset,3));
   struct bitset *setunion;
   setunion = mk_union(mybitset, myotherbitset);
   print_set(mybitset);
   print_set(myotherbitset);
   print_set(setunion);
   //End checking*/

   /*This block creates two sets and reads in two strings
     It the puts the strings in the set */
   struct bitset *one,*two;
   char input1[MAX],input2[MAX];
   printf("First sentace plox: \n");
   fgets(input1,MAX,stdin);
   one = parse_string(input1);
   printf("Second sentance plox\n");
   fgets(input2,MAX,stdin);
   two = parse_string(input2);
   
   printf("Line 1\n\n");
   print_set(one);
   printf("Line 2\n\n");
   print_set(two);

   struct bitset * setunion;
   setunion = mk_union(one,two);
   printf("\nUnion:\n");
   print_set(setunion);

   struct bitset * intersection;
   intersection = intersect(one,two); 
   printf("\nIntersetion:\n");
   print_set(intersection);
   
   
   return 0;
    
}



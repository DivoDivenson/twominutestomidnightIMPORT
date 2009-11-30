#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "bitset.h"

struct bitset * bitset_new(int size){
  
  struct bitset * result;
  result = malloc(sizeof(struct bitset));
  result->size = (size / 8) + !((size % 8) == 0); //Add one byte to the size if needed 
  result->bits = malloc(result->size); 
  return result;

}

/*int bitset_lookup(struct bitset * this, int item){
   if(item >= 0){
      return(this->bits & (1<<item));
   }
}*/

char print_set(struct bitset * this){

   //Copy the bitset so not to effect it
   struct bitset * tempset;
   tempset = bitset_new(this->size);
   tempset->size = this->size;
   tempset->bits = this->bits;

   //Create a string to store representation of bitset
   char result[100];
   
   

   sprintf(result,"%c",'\0');

   return * result;

 
}

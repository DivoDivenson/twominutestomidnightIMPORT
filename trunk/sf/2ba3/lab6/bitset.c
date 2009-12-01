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

int bitset_lookup(struct bitset * this, int item){
   if(item >= 0){
      return(this->bits[0] & (1<<item));
   }else{
      return -1;
   }
}

int bitset_add(struct bitset * this, int item){
  if(item >= 0)
    this->bits[0] | (1<<item);
    printf("Set\n");
    return 1;
  }else{
    return 0;
  }
}

   

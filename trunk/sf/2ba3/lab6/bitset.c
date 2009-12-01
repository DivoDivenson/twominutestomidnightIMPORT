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
     if(this->bits[0] & (1<<item)){
          return 1;
         }else{
           return 0;
         }
   }else{
      return -1;
   }
}

int bitset_add(struct bitset * this, int item){
  if(item >= 0){
    int index = set_byte(item);
    item = set_bit(item,index);
    this->bits[index] |= (1<<item);
    return 1;
  }else{
    return 0;
  }
}

int set_byte(int item){
  return item / 8;
}

int set_bit(int item, int index){
  return (item - (8*index));
}

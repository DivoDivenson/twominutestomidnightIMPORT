#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "bitset.h"

struct bitset * bitset_new(int size){
  /*Will malloc zero wipe the space? Assuming it does, this is prehaps very bad */
  if(size > 0){
    struct bitset * result;
    result = malloc(sizeof(struct bitset));
    result->bit_size = size; //Start the set at 0
    result->size = (size / 8) + !((size % 8) == 0); //Add one byte to the size if needed 
    result->bits = malloc(result->size); 
    return result;
  }else{
     return NULL;
  }

}

int bitset_lookup(struct bitset * this, int item){
   if(outOfrange(this,item)){
     int index = set_byte(item);
     item = set_bit(item,index);
     if(this->bits[index] & (1<<item)){
          return 1;
         }else{
           return 0;
         }
   }else{
      return -1;
   }
}

int bitset_add(struct bitset * this, int item){
  if(outOfrange(this,item)){
    int index = set_byte(item);
    item = set_bit(item,index);
    this->bits[index] |= (1<<item);
    return 1;
  }else{
    return 0;
  }
}

int bitset_remove(struct bitset * this, int item){
   if(outOfrange(this,item)){
      int index = set_byte(item);
      item = set_bit(item,index);
      this->bits[index] &= ~(1<<item);
      return 1;
   }else{
      return 0;
   }
}

struct bitset * mk_union(struct bitset * one, struct bitset * two){
  struct bitset * result, * small,* big;
  if(one->bit_size > two->bit_size){
      big = one;
      small = two;
  }else{
      big = two;
      small = one;

  }
  result = bitset_new(big->bit_size);
  printf("%d\n",big->size);
  
  int index=0;
  int small_set = 0; //Container for bitset_lookup(small) to save calling the method twice
  /*{ //Union of two sets
     small_set = bitset_lookup(small,index);
     if(small_set || bitset_lookup(big,index)){
        bitset_add(result,index);
     }
     index++;     
  }while(small_set != -1);*/

  while(small_set = (bitset_lookup(small,index)) != -1){
     if(small_set || bitset_lookup(big,index)){
        bitset_add(result,index);      
     }
     index++;
   
  }
   
  /*Insert remaining elements in bigger set */
  while(index <= big->bit_size){
     if(bitset_lookup(big,index)){
        bitset_add(result,index);         
     }
  }
  
  return result;
      
}

int outOfrange(struct bitset * this, int item){
   if(item <= this->bit_size && item >= 0){
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

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "bitset.h"
/*bitset.h is where to find out what most of the methods do */

/* Create the bit set */
struct bitset * bitset_new(int size){
  /*Will malloc zero wipe the space? Assuming it does, this is prehaps very bad */
  if(size > 0){
    struct bitset * result;
    result = malloc(sizeof(struct bitset));
    result->bit_size = size; //Start the set at 0
    result->size = (size / 8) + !((size % 8) == 0); //Add one byte to the size if needed 
    result->bits = malloc(result->size);
    int i;
    for(i =0; i <= result->size; i++){
      result->bits[i] = 0;
    }
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
//          printf("Set\n");
         }else{
           return 0;
//           printf("Not set\n");
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
  
  int index=0;
  int small_set = 0; //Container for bitset_lookup(small) to save calling the method twice

  while((small_set = (bitset_lookup(small,index))) != -1){
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
     index++;
  }
  
  return result;
      
}

struct bitset * intersect(struct bitset * one, struct bitset * two){
  struct bitset * result, * small,* big;
  if(one->bit_size > two->bit_size){
      big = one;
      small = two;
  }else{
      big = two;
      small = one;

  }
  result = bitset_new(big->bit_size);
  
  int index=0;
  int small_set = 0; //Container for bitset_lookup(small) to save calling the method twice

  while((small_set = (bitset_lookup(small,index))) != -1){
     if(small_set && bitset_lookup(big,index)){
        bitset_add(result,index);      
     }
     index++;
   
  }
   
  /*Insert remaining elements in bigger set */
  while(index <= big->bit_size){
     if(bitset_lookup(big,index)){
        bitset_add(result,index);         
     }
     index++;
  }
  
  return result;
      
}

/*Simply dumps each element and its index to screen. Not enough
  time to do a return string and such */
char * print_set(struct bitset * this){
  char * result;
  result = malloc(this->size+1);
  int temp;
  int index = this->bit_size;

  while(index >= 0){
    printf("%d : %d\n ", index, bitset_lookup(this,index));
    index--;
  }
  printf("\n");
}

/* Quick check to see if item is outside of bitset */
int outOfrange(struct bitset * this, int item){
   if(item <= this->bit_size && item >= 0){
      return 1;
   }else{
      return 0;
   }

}
/*Next two calcualte index's*/
int set_byte(int item){
  return item / 8;
}

int set_bit(int item, int index){
  return (item - (8*index));
}


int isEven(struct bitset * this){
   int i;
   int count = 0;
   for(i = 0; i <= this->bit_size; i++){
      if(bitset_lookup(this,i)){
         count++;
      }
   }
   if((count % 2) == 0){
      return 1;
   }else{
      return 0;
   }
}

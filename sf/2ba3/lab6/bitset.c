#include <stdio.h>
#include <stdlib.h>
#include "bitset.h"

struct bitset * bitset_new(int size){
  
  struct bitset * result;
  result = malloc(sizeof(struct bitset));
  result->size = (size / 8) + !((size % 8) == 0); //Add one byte to the size if needed 
  result->bits = malloc(result->size); 
  return result;

}

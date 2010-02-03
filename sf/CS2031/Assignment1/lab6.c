#include <stdio.h>
#include <stdlib.h>
#include <string.h>
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

//Write the input into a 2D array of bits, with LSB being a parity
struct bitset * bitset2D(struct bitset * input){
   struct bitset * result[input->size -1];
   
   int i,j, inputPos = input->bit_size;
   printf("%d\n",inputPos);
   for(i = 0;i <= input->size -1;i++){
      result[i] = bitset_new(8);
      for(j = 8; j < 0; j++){

      }
   }

   return * result;

}

int main(){


   //Never mind good practise, lets just fire it all into main.
   int reverse_x = 0,length,bit_index;
   char input[100]; //Assumtion about user input....
   printf("Please enter in string of bits to process: ");
   scanf("%s",&input);

   
   printf("\n%s\n",input);
   length = strlen(input);
   reverse_x = length;
   struct bitset * input_set = bitset_new(length);
   while(reverse_x-- >= 0){
      if(input[reverse_x] == '1'){
        bitset_add(input_set,reverse_x);
      }
   }
  
   struct bitset * twoDset = bitset2D(input_set);
   //printset_line(twoDset);

  
   return 0;
    
}



#include <stdio.h>
#include <stdlib.h>
#include "stack.h"
#include <string.h>
#define MAX 100

/* Read in a line from stdin and return its length */
int getline(char * line,int max){
   if(fgets(line,max,stdin) == NULL){
      return 0;      /*Indicate am error. Even if the user presses enter, 
                       the carrige return is counted as a character */
   }else{
      return strlen(line) -1; // -1 to account for carrige return
   }

}

/* Return the character at input[0] to an int and return it. -1 indicates error */
int getop(char input[]){
   if(!isdigit(input[0])){
      return input[0];
   }else{
      return -1;
   }
}

int main(){
   struct  stack * myStack;
   myStack = newStack(10);

   char input[MAX]; //Buffer for input


   while((getline(input,MAX)) !=  0){
      char * value = strtok(input, " "); //Use strtok to parse input
      double operand;  //Temp storage in the case of - and /
      while(value != NULL && !isfull(myStack)){
         /* If a digit is enterd push to stack, else apply operand to 2 top most vales
            and push the result back on */
         if(isdigit(value[0])){
            push(myStack,atof(value));
         }else if(value[0] == '-' && isdigit(value[1])){       
                                    /* Deal with negative numbers. Skips the minus and writes in the double
                                        as a positive value. Multiply it by -1 to make it negative again.
                                        Best way I think of at the moment...
                                     Also, operators are not checked for. Assume user only uses an operator when there
                                     are >= two elements on the stack*/
            char * temp;
            temp = &value[0];
            push(myStack,(atof((temp + 1)) * -1));
         }else{
            switch (getop(value)){
               case '+':
                  push(myStack,(pop(myStack) + pop(myStack)));
                  break;
               case '*':
                  push(myStack,(pop(myStack) * pop(myStack)));
                  break;
               case '-':
                  operand = pop(myStack);
                  push(myStack,((pop(myStack)) - operand));
                  break;
               case '/':
                  operand = pop(myStack);
                  if(operand != 0.0){
                     push(myStack,(pop(myStack) / operand));
                  }else{
                     printf("Division by 0. Ignoring instruction\n");
                  }
                  break;
               case 'f':
                  flush(myStack);
                  break;
               default:
                  printf("Error: unknow operator %c\n", value);
                  break;

            }// End switch
         }
         value = strtok(NULL, " "); //get the mext tokem
      }
      printf("%f\n", pop(myStack)); // Print the result for a line
   }
   printf("Done\n");
   return 0;
}


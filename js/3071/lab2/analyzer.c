#include <stdio.h>
#include <stdlib.h>

int digit(char *s){

   int i, result;
   result = 0;
   for(i = 0; isdigit(s[i]); i++)
      result = result * 10 + (s[i] - '0');
   
   
   return result;
}

int check(char *s, int sign){
   int i;
   for(i = 0; isdigit(s[i]); i++)
      ;
   if(i < 5){
      return 1;
   }else if(i > 5){
      return 0;
   }else{
   //else do some sort of character compare  
   //this is gonna be a bit brutal....
      if(s[0] >= '3')
         if(s[1] >= '2')
            if(s[2] >= '7')
                  if(s[3] >= '6')
                     if(sign == 1 && s[4] > '7'){
                        //Overflow
                        return 0;
                     }else if(s[4] > '8'){ //Sign will have to be 1 or -1 by the time this code executes
                        return 0;
                     }

      return 1;
   }
}


int main(int argc, char * argv[]){

   char *c;
   int val = 0;
   int sign = 1;

   c = argv[1];


   if(c[0] == '-'){
      sign = -1;
      c = &c[1];
   }else if(c[0] == '+'){
      c = &c[1];
   }

   if(check(c, sign))
      val = digit(c) * sign;

   printf("%d\n",val);

   
   return 0;
}


#include <stdio.h>
#include <stdlib.h>

int start(char *s){

   int i, result;
   result = 0;
   for(i = 0; isdigit(s[i]); i++)
      result = result * 10 + (s[i] - '0');
   
   
   return result;
}

int check(char *s){
   int i;
   for(i = 0; isdigit(s[i]); i++)
      ;
   if(i < 5){
      return 1;
   }else if(i > 5){
      return 0;
   }
   //else do some sort of character compare  
}

int main(int argc, char * argv[]){

   char *c;
   int val = 0;
   int sign;

   c = argv[1];

   if(check(c))
      val = start(c);

   printf("%d\n",val);

   
   return 0;
}


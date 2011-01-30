#include <stdio.h>

int main(){
   int i =2;
   printf("%d\n", i);
   while(1){
      i += i;
      printf("%d\n",i);
   }

   return 0;
}

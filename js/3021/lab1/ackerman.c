#include <stdio.h>
#include <stdlib.h>

int count= 0;
int ackerman(int x, int y){
   count++;
   if(x == 0)
      return y+1;
   else if (y ==0)
      return ackerman(x-1,1);
   else
      return ackerman(x-1, ackerman(x, y -1));
}


int main(){
   int r = ackerman(3,6);
   printf("Called %d times\n", count);
   return 0;
}


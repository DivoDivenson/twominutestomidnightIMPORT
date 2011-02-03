#include <stdio.h>
#include <stdlib.h>
#include <time.h>

static int count =0;
int ackerman(int x, int y){
   count++;
   changeWindow();
   if(x == 0)
      return y+1;
   else if (y ==0)
      return ackerman(x-1,1);
   else
      return ackerman(x-1, ackerman(x, y -1));
}

void changeWindow(){
   ;
}

int main(){

   int test[6];
   int windowSize = 60;
   int i;
   for(i = 0; i < 100; i++){
      test[i] = 0;
   }


/*struct timeval start_time, end_time;
   long long compute_time;
int window[6];   
   printf("hello\n");
   for(i =0; i < 400; i++){ //Why does this run??
      window[i] = 0;
   }
   gettimeofday(&start_time,NULL);
   int r = ackerman(3,6);
   gettimeofday(&end_time, NULL);
   compute_time = (end_time.tv_sec - start_time.tv_sec) * 1000000L +
           (end_time.tv_usec - start_time.tv_usec);
   printf("Called %d times\n Took %lld microseconds\n", count, compute_time);
   return 0;*/

      return 0;
}


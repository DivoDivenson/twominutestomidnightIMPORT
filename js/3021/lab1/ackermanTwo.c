#include <stdio.h>
#include <stdlib.h>
#include <time.h>
/*
Stock code takes ~1700 microseconds

   */
int count= 0;
int overflow = 0;
int underflow= 0;
int windowSize = 16;
int windowCount = 0;

int ackerman(int x, int y){
   count++;
   int result;
   if(windowCount == windowSize -1){
      overflow++;
   }else{
      windowCount++;
   }
   if(x == 0)
      result =  y+1;
   else if (y ==0)
      result =  ackerman(x-1,1);
   else
      result =  ackerman(x-1, ackerman(x, y -1));
   if(windowCount <= 0){
      underflow++;
   }else{
      windowCount--;
   }

   return result;
}

int main(){
   struct timeval start_time, end_time;
   long long compute_time;
   gettimeofday(&start_time,NULL);
   int r = ackerman(3,6);
   gettimeofday(&end_time, NULL);
   compute_time = (end_time.tv_sec - start_time.tv_sec) * 1000000L +
           (end_time.tv_usec - start_time.tv_usec);
   printf("With %d register windows, Called %d times\n Took %lld microseconds\n overflow: %d underflow: %d \n", windowSize, count, compute_time, overflow, underflow);
   return 0;
}


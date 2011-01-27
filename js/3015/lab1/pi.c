#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <pthread.h>

#define NUM_THREADS 6
pthread_mutex_t   mutex = PTHREAD_MUTEX_INITIALIZER;
int final_count = 0;
int darts = 1000000;//Darts is only ever read from


//1) Divide up iterations. Could randomize number so threads don't all finish at once and go for the lock
//2) Run method once per thread, sum the counts,then divide by NUM_THREADS. Wait. That's stupid
//pass in darts as param
void *getPi(void *argument){
   printf("Arg :%d\n\n", (int)argument); 
   
  double x,y,hyp;
  srand(time(NULL));
  int i, count = 0;
  int loop_amount = darts / NUM_THREADS; //Divide up the work
  for(i = 0; i < loop_amount; i++){
    x = rand()/(RAND_MAX + 1.0);
    y = rand()/(RAND_MAX + 1.0);
    hyp = (x * x) + (y * y);
    //hyp = sqrt(hyp); // (x < 1) == ( x^2 < 1)
    if(hyp <= 1){ 
      count++;
    }
  }
  pthread_mutex_lock(&mutex);
  final_count += count;
  pthread_mutex_unlock(&mutex);
//  return (double) ((4.0 *count) / darts);

  //return (void *)*/
}

int main(){
  pthread_t threads[NUM_THREADS];

  int rc, i;
  for(i=0; i<NUM_THREADS; i++){
    printf("Creating thread\n");
    rc = pthread_create(&threads[i], NULL, getPi, (void *)i);
  }
  for(i = 0; i < NUM_THREADS; i++){
    rc = pthread_join(threads[i], NULL);
  }
  final_count = final_count;// / NUM_THREADS;
  printf("Pi is : %lf\n", (double)((4.0 * final_count) / darts));
  pthread_mutex_destroy(&mutex);
  return 0;

}

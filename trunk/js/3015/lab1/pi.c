#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <pthread.h>

#define NUM_THREADS 60
int darts = 600000;//Darts is only ever read from
//HACK. Make darts a multiple of number of threads to avoid errors when divinding up the work

#ifdef PTHREADS
pthread_mutex_t   mutex = PTHREAD_MUTEX_INITIALIZER;
int final_count = 0;

//PART II of question
/* Accessing a location concurrently incures serious overhead.
   Instead try have each thread write its result to a unique array position
   In practise it made no difference, variables must be protected, not memory locations?
 
 
 From the timings it is clear that the threaded version of the program runs MUCH slower than the linear version.
 One reason for this could be accessing the shared counter and the end of the method. However, the 
 time it takes seems to increase proportionally with the number of darts. I'm stumped*/
int counts[NUM_THREADS];


//1) Divide up iterations. Could randomize number so threads don't all finish at once and go for the lock
//2) Run method once per thread, sum the counts,then divide by NUM_THREADS. Wait. That's stupid
//pass in darts as param
void *getPi(void *argument){
  double x,y,hyp;
  srand(time(NULL));
  int i, count = 0;
  int loop_amount = (darts / NUM_THREADS); //Divide up the work
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
//  counts[(int) argument] = count;
//  return (double) ((4.0 *count) / darts);

  //return (void *)*/
}
#else
//Serial code
double getPi(){

   double x,y,hyp;
   srand(time(NULL));
   int i, count =0;
   for(i = 0; i < darts; i++){
      x = rand()/(RAND_MAX + 1.0);
      y = rand()/(RAND_MAX + 1.0);
      hyp = (x * x) + (y * y);
      if(hyp <= 1){
         count++;
      }
   }
   return (double) ((4.0*count) / darts);

}

#endif

int main(){
  struct timeval start_time, end_time;
  struct timeval start_thread; //Starting time ignoring thread creation time
  long long compute_time;
  gettimeofday(&start_time, NULL);
  #ifdef PTHREADS
  pthread_t threads[NUM_THREADS];

  int rc, i;
  
  for(i=0; i<NUM_THREADS; i++){
    //printf("Creating thread\n");
    rc = pthread_create(&threads[i], NULL, getPi, (void *)i);
  }
  gettimeofday(&start_thread, NULL);
  for(i = 0; i < NUM_THREADS; i++){
    rc = pthread_join(threads[i], NULL);
  }
  final_count = final_count;// / NUM_THREADS;
  gettimeofday(&end_time, NULL);
/*  for(i = 0; i < NUM_THREADS; i++){
      final_count += counts[i];
  } */  
  printf("Pi is : %lf\n", (double)((4.0 * final_count) / darts));
  pthread_mutex_destroy(&mutex);
#else
  double temp = getPi();
  gettimeofday(&end_time, NULL);
  printf("Pi is : %lf\n", temp);
#endif
  compute_time = (end_time.tv_sec - start_time.tv_sec) * 1000000L +
     (end_time.tv_usec - start_time.tv_usec);
  printf("Done in %lld microseconds\n", compute_time);
#ifdef PTHREADS
  compute_time = (end_time.tv_sec - start_thread.tv_sec) * 1000000L +
     (end_time.tv_usec - start_thread.tv_usec);
  printf("Done in %lld microseconds, ignoring thread creation time\n", compute_time);
#endif
  
  return 0;

}

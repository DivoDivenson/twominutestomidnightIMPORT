#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <pthread.h>

//pass in darts as param
void *getPi(void *argument){
  int darts = 500000;
  double x,y,hyp;
  srand(time(NULL));
  int i, count = 0;
  for(i = 0; i < darts; i++){
    x = rand()/(RAND_MAX + 1.0);
    y = rand()/(RAND_MAX + 1.0);
    hyp = (x * x) + (y * y);
    hyp = sqrt(hyp);
    if(hyp <= 1){ 
      count++;
    }
  }
  return (double) ((4.0 *count) / darts);
  //return (void *)
}

int main(){

  pthread_t threads[NUM_THREADS];
  int thread_args[NUM_THREADS];
  int rc, i;
  for(i=0; i<NUM_THREADS; i++){
    thread_args[i] = i;
    rc = pthread_create(&threads[i], NULL, getPi, (void *) &thread_args[i]);
  }
  for(i = 0; i < NUM_THREADS; i++){
    rc = pthread_join(threads[i], NULL);
  }
  printf("%lf\n",getPi());
  return 0;

}

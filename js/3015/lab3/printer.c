#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <unistd.h>
#define NUM_PRINTERS 3
#define NUM_JOBS 10

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t print_q = PTHREAD_COND_INITIALIZER;

int printers = 3; //No of printers free to do work
void * job(void * arg);

int main(){
   int i;
   pthread_t jobs[NUM_JOBS];
   pthread_mutex_init(&mutex,NULL);
   pthread_cond_init(&print_q,NULL);
   srand(time(NULL));
   for(i=0; i<NUM_JOBS; i++){
      pthread_create(&jobs[i], NULL, job,(void*)i);
      sleep(rand() % 3);
   }
   for(i = 0; i< NUM_JOBS; i++){
      pthread_join(jobs[i], NULL);
   }
   return 0;
}

void * job(void * arg){
   while(1){
      //If there is a printer avaliable to print
      if(printers > 0){
         //If a job can be submited then do, otherwise wait for a time decided by the OS
         pthread_mutex_lock(&mutex);
         printers--;
         pthread_mutex_unlock(&mutex);
         printf("Job %d printing\n", (long) arg);
         sleep(rand() % 10); //simulate how long it take to print. Was using usleep(rand() /1000) which was completly stupid. Thanks eoin
         if(printers <= 0){  //WTF IS THIS DOING HERE
            printf("Job %d signalling\n", (long)arg);
            pthread_cond_signal(&print_q);
         }
         pthread_mutex_lock(&mutex);
         printf("Job %d done\n", (long) arg);
         printers++;
         pthread_mutex_unlock(&mutex);
         break; //Job done, die
      }
      if(printers <= 0){
         pthread_mutex_lock(&mutex);
         printf("Job %d waiting\n", (long) arg);
         pthread_cond_wait(&print_q, &mutex);
         pthread_mutex_unlock(&mutex);
      }

   }
   return NULL;

}


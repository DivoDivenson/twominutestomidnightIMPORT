#include <pthread.h> 
#include <stdio.h> 
#include <stdlib.h> 
#include <semaphore.h>
#include <time.h>
#define NUM_THREADS 6 
#define BUFSIZE 10
pthread_mutex_t mutex;
sem_t full, empty; // Semaphores to keep track of buffer

char buffer[BUFSIZE];
int bufCount;




void *producer(void *arg){
   char token_data;

   while(1){
      sleep((rand() /100000000)); //Make thread sleep for a random time, but not too long
      token_data =  (rand() / (RAND_MAX / (58))) + 65;//Generate a random letter, trust me
      //Acquire locks
      sem_wait(&empty);
      pthread_mutex_lock(&mutex); //Not really needed, but should insure no errors
      if(bufCount < BUFSIZE){
         buffer[bufCount++] = token_data;
         printf("Inserted %c. bufCount = %d\n", token_data, bufCount);
      }else{
         printf("Could not insert %c\n", token_data);
      }

      pthread_mutex_unlock(&mutex);
      sem_post(&full);

   }
}

void *consumer(void *arg){
   char data;
   while(1){
      sleep((rand() / 100000000));  
      sem_wait(&full);
      pthread_mutex_lock(&mutex);
      if(bufCount > 0){
         data = buffer[bufCount--];
         printf("%c removed. bufCount = %d\n", data, bufCount);
      }else{
         printf("Buffer empty\n");
      }

      pthread_mutex_unlock(&mutex);
      sem_post(&empty);
   }
}



//Something wrong with the buffer, but the count works fine and demos use of semaphores
int main () { 
	int i;
   srand(time(NULL));
   pthread_mutex_init(&mutex, NULL);
   sem_init(&full, 0, 0);  //init semaphores, allow sharing between threads only, not processes
   sem_init(&empty, 0, BUFSIZE);
   bufCount = 0;
   pthread_t consumers[NUM_THREADS];
   pthread_t producers[NUM_THREADS-1];

   pthread_t threads;
   for(i =0; i < NUM_THREADS; i++){ //Create consumers
      pthread_create(&consumers[i], NULL, consumer,NULL);
   }

   for(i = 0; i < NUM_THREADS-1; i++){ //Create producers
      pthread_create(&producers[i], NULL, producer, NULL);
   }

   for(i = 0; i < NUM_THREADS-1; i++){
      pthread_join(producers[i], NULL);
      pthread_join(consumers[i], NULL);
   }

   pthread_join(consumers[(NUM_THREADS-1)], NULL);

   sleep(3);
   printf("Done\n");
   exit(0);
}

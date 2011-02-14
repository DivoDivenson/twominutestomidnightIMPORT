#include <pthread.h> 
#include <stdio.h> 
#include <stdlib.h> 
#include <semaphore.h>
#include <time.h>
#include <queue>
#include <unistd.h>
#define NUM_THREADS 6 
#define BUFSIZE 10
#define Q_SIZE 10
#define NUM_JOBS 10
#define NUM_PRINTERS 2 // fence
using namespace std;
pthread_cond_t print_item; //Number of free printers
queue<long> job_queue;
pthread_mutex_t queueTex;


void *job(void *arg){

void *printer(void *arg){
	//When a job is put on the queue a printer wakes up and removes it
	pthread_mutex_lock(&queueTex);
	printf("Printer %d sleeping\n", (long)arg);
	//pthread_cond_wait(&print_item, &queueTex);
	printf("Printing job %d\n", job_queue.front());
	job_queue.pop();
	pthread_mutex_unlock(&queueTex);
}



int main() { 
	int i;
	pthread_t threads[NUM_JOBS];
	pthread_t printers[NUM_PRINTERS];
	pthread_mutex_init(&queueTex, NULL);
	pthread_cond_init(&print_item, NULL);

   for(i = 0; i < NUM_PRINTERS; i++){
		printf("Creating printer %d\n", i);
		pthread_create(&printers[i], NULL, printer, (void *)i);
		pthread_join(printers[i], NULL);
	}
	for(i = 0; i < NUM_JOBS; i++){
		printf("New job\n");
		pthread_create(&threads[i], NULL, job, (void *)i);
		pthread_join(threads[i], NULL);
		usleep((rand() / 100000));
	}
	for(i = 0; i < NUM_JOBS; i++){
		printf("%d\n", job_queue.front());
		job_queue.pop();
	}

	/*for(i = 0; i < NUM_JOBS; i++){
		printf("%d\n", job_queue.front());
		job_queue.pop();
	}*/	
}







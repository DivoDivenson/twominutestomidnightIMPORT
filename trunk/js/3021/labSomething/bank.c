#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

int account_a=100;
int account_b=100;
int account_c=100;

struct account_holder{
	int * src;
	int * dst;
};

int lock_var = 0;
int count_var = 0;

void lock(){
  	
	__asm__(
		"mov $1, %edx;"
		"mov lock_var, %eax;"
		"jmp start;"
			"count:;"
			//	"addl $1,count_var;"
			"start:;"
				//Check if the lock is free
				"test %eax, %eax;"
				"jnz count;"
				//Now try set the lock
				"lock cmpxchg %edx, lock_var;"
				"test %eax, %eax;"
				"jnz count"
			
			/*	"cmpl $0, lock_var;"
				"jne count;"
				"movl $1, lock_var"
			*///	"lock cmpxchg %edx, lock_var;"
				//"test %eax, %eax;" This instruction does not work
			//	"cmpl $0, %eax;"
			//	"jnz count;"
		//		"ret;" 
				);
}

void unlock(){
	__asm__(
	/*	"_unlock:;"
			"movl $0, %edx;"
			"xchg %edx, lock_var;"
			"movl $0, lock_var;"
			"ret;"
	*/	
		"movl $0, lock_var;"
	);

}

void* transfare(void * argument){
	struct account_holder * temp;
//	lock();
	temp = (struct account_holder *) argument;
	int * srcB = temp->src;
	int * dstB = temp->dst;

	int i;
	int amount;
	int one = 1;
	for(i = 0; i < 1000; i++){
		amount = rand() % 21;
		lock();
		//printf("Lock acquired\n");
		
//		printf("%d\n", amount);
	//	printf("lock\n");
		//lock();
		if( *srcB < amount){
			amount = *srcB;
		}
		if(amount > 0){
			*srcB -= amount;
			*dstB += amount;
		}
	//	printf("Unlock\n");
		unlock();
		//printf("Lock released %d\n", i);
	}
	printf("Done\n");
	//pthread_exit(NULL);
	//unlock();
}

int main(){
	srand(time(NULL));
	pthread_t threads[3];
	int i;
	struct account_holder  a,b,c;
	a.src = &account_a;
	a.dst = &account_b;

	b.src = &account_b;
	b.dst = &account_c;

	c.src = &account_c;
	c.dst = &account_a;
	pthread_create(&threads[0], NULL, transfare, (void *) &a);
	pthread_create(&threads[1], NULL, transfare, (void *) &b);
	pthread_create(&threads[2], NULL, transfare, (void *) &c);
	for(i=0; i< 3; i++){
		printf("Joining\n");
		pthread_join(threads[i], NULL);
		printf("Killed %d\n", i);
	}
	printf("Hello\n");
	/*transfare(&a);
	transfare(&b);
	transfare(&c);
	*/
	printf("a: %d, b: %d, c %d\n", account_a, account_b, account_c);
	printf("Sum: %d\n", account_a + account_b + account_c);
	return 0;
}



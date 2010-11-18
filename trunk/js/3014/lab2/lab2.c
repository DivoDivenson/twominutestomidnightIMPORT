#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <xmmintrin.h>

#define	SIZE	4096

/* Note this use of global variables is truly awful programming */
float	*vals;		//initial values
float	*res1, *res2; 	//results
float	a, b;

void cleanup()
{
  free(vals);
  free(res1);
  free(res2);
}

void init()
{
  int i;

  vals = (float *)memalign(16, sizeof(float) * SIZE);	
  res1 = (float *)memalign(16, sizeof(float) * SIZE);
  res2 = (float *)memalign(16, sizeof(float) * SIZE);	

  for(i=0; i<SIZE; i++){
    vals[i] = (float)random()/RAND_MAX;
  }

  a = 23;
  b = 132;
}

void sisd()
{
  int i;

  for(i = 0; i < SIZE; i++){
    float v = vals[i];
    res1[i] = 1.0f/a + (v+3.0f)*(v-4.0f) + (3.0f*v) + 1.0f/(sqrtf(v)) + b/a;
  }	
}



void simd()
{

}

/* This is a special instruction that measure a time stamp
    counter that measures the number of elapsed clock ticks.
    This only works on x86 */
static inline unsigned long long ticks()
{
  return _rdtsc();
}

int main()
{
  int i;
  unsigned long long start_time, middle_time, end_time, sisd_time, 
           simd_time;

  init();

  start_time = ticks();
  sisd();
  middle_time = ticks();
  simd();
  end_time = ticks();

  printf("Dumping results\n\n");

  for( i = 0; i < SIZE; i++ ){
    printf("%f\t%f\t%f\n", vals[i], res1[i], res2[i]);	
  }

  sisd_time = middle_time - start_time;
  simd_time = end_time - middle_time;

  printf("sisd time was %lld\n", sisd_time);
  printf("simd time was %lld\n", simd_time);
  printf("SIMD time was %f%% of SISD time\n", 
         ((float)simd_time/(float)sisd_time)*100.0f);

  cleanup();
  return 0;
}

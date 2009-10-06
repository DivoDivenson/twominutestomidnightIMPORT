/* Program that reads in a text file of integers
   and outputs the numbers in sorted order. */

#include <stdio.h>

int bubbleCount,selectionCount,insertionCount,funnyCount;

/* find smallest element between start and end of array */
int findMin(int a[], int size, int start) {
  int min = a[start];
  int result = start;
  int i;

  for ( i = start; i < size; i++ ) {
    if ( a[i] < min ) {
      min = a[i];
      result = i;
    }
  }
  return result;
}

/* selection sort algorithm: repeatedly find smallest
   element and place at start of unsorted section. */
static void selectionSort(int a[], int size) {
  selectionCount = 0;
  int i,mIndex,temp;
  for(i=0;i<size;i++){
    mIndex = findMin(a,size,i);
    temp = a[i];
    a[i] = a[mIndex];
    a[mIndex]=temp;
    selectionCount = selectionCount + size +1; /*Because findMin searchs
                                                the entire array with each call*/
  }
}

/* insertion sort algorithm: place unsorted array
   elements into the correct location in an initially
   empty sorted part.*/
void insertionSort(int a[], int size) {
  int i,j,temp;
  insertionCount = 0;
  for(i=1;i < size; i++){  //When to stop the loop?
    j = i;
    temp = a[j];
    while (j>0 && a[j -1] > temp){    
      a[j]=a[j-1];
      j--;
      insertionCount++;
      }
    a[j] = temp;
    }
}



/* bubble sort algorithm: repeatedly compare and swap
   adjacent array elements. */
void bubbleSort(int a[], int size) {
  int i;
  int j;
  int temp;
  bubbleCount=0;
  for(i =0; i < size; i++){
    for(j = 0; j < size-i-1;j++){
      if(a[j] > a[j+1]){
        temp = a[j];
        a[j] = a[j+1];
        a[j+1] = temp;
      }
      bubbleCount++;
    }
  }
}

/* funny sort algorithm: like bubble sort only we
   go back to the start every time we find a pair
   out of order. */
void funnySort(int a[], int size) {
  int j, temp;
  funnyCount = 0;
  j = 0;
  while ( j < size - 1 ) {
    if ( a[j] > a[j+1] ) {
      temp = a[j];
      a[j] = a[j+1];
      a[j+1] = temp;
      j = 0;
    }
    else {
      j++;
    }
    funnyCount++;
  }
}

/* Open a file. Abort progam if file cannot be opened */
FILE * open_file(char filename[])
{
  FILE * file;

  file = fopen(filename, "r");
  if ( file == NULL ) {
    printf("FATAL: Error opening file %s. Aborting program.\n", filename);
    exit(1);
  }

  return file;
}

/* read a stream of up to 'size' integers from a text file.
   return number of integers */
int read_in(int a[], int size, char filename[])
{
  const int max_line = 1024;
  char line[max_line];
  int i;
  FILE * file;
  char * eof;

  file = open_file(filename);

  /* read in the ints - one per line */
  i = 0;
  eof = fgets(line, max_line, file);
  while ( eof != NULL && i < size ) {     /* eof == NULL => end of file */
    sscanf(line, "%d", &(a[i]));
    i++;
    eof = fgets(line, max_line, file);
  }
  fclose(file);
  return i;
}

/* write out an array of integers up to 'size' */
void write_out(int a[], int size)
{
  int i;

  for ( i = 0; i < size; i++ ) {
    printf("%d\n", a[i]);
  }
}


/* read in a file of numbers, sort them, and
   write them out to the screen */
int main()
{
  const int size = 1024;
  int nums[size];
  int nnums;
  printf("Now sorting with insertionSort\n");
  nnums = read_in(nums, size, "numbers.txt"); //Because I'm Lazzzzzzzzzzzyyyyyy
  insertionSort(nums, nnums);
  //write_out(nums, nnums);
  printf("\nInsertion sort took %d passes.\nNow sorting with bubbleSort\n", insertionCount);
  /* flush output buffer, so all output appears on screen */
  fflush(stdout);
  nnums = read_in(nums, size, "numbers.txt");
  bubbleSort(nums, nnums);
  //write_out(nums, nnums);
  printf("\nBubble Sort took %d passes.\nNow sorting with slectionSort\n",bubbleCount);
  /* flush output buffer, so all output appears on screen */
  fflush(stdout);
  nnums = read_in(nums, size, "numbers.txt");
  selectionSort(nums, nnums);
  //write_out(nums, nnums);
  /* flush output buffer, so all output appears on screen */
  fflush(stdout);
  printf("\nSelection sort took %d passes.\nNow sorting with funnySort\n",selectionCount);
  /* flush output buffer, so all output appears on screen */
  fflush(stdout);
  nnums = read_in(nums, size, "numbers.txt");
  funnySort(nums, nnums);
  //write_out(nums, nnums);
  /* flush output buffer, so all output appears on screen */
  fflush(stdout);
  printf("\nFunny sort took %d passes\n",funnyCount);
 
  return 0;
}


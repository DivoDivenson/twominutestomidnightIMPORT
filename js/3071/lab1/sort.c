#include <stdio.h>
/* insertion sort algorithm: place unsorted array
   elements into the correct location in an initially
   empty sorted part.*/
void insertionSort(int a[], int size) {
  int i,j,temp;
  int insertionCount = 0;
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


int main(){

  int array[] = {12,4,5,3,18,65,12};
  insertionSort(array, 7);
}



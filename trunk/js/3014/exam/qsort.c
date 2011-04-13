#include <stdio.h>

void swap(int arr[],int i, int j)
{
	int tmp;
	
	tmp = arr[i];
	arr[i] = arr[j];
	arr[j] = tmp;

}

void qsort(int arr[], int left, int right, int level)
{
	int i, last;
	
	if(left >= right)
		return;

	swap(arr, left, (left + right) / 2);
	last = left;
	for(i = left + 1; i <= right; i++){
		if(arr[i] < arr[left])
			swap(arr, ++last, i);
	}
	printf("At level %d \t: ", level);
	print_array(arr, 12);
	swap(arr, left, last);
	qsort(arr, left, last -1, level + 1);
	qsort(arr, last + 1, right, level + 1);

}
void print_array(int array[], int length)
{
	int i;
	printf("[");
	for(i = 0; i < length; i++){
		printf(" %d, ", array[i]);
	}
	printf("\b\b ]\n");
	
}

int main(){
	int array[] = {1,4,6,8,9,2,4,6,4,5,7,2};
	printf("\t\t  ");
	print_array(array, 12);
	qsort(array, 0, 12, 0);

	printf("Final\n");
	print_array(array, 12);
	return 0;

}

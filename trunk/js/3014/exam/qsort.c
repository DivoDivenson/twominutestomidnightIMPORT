#include <stdio.h>
#include <omp.h>
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
	//printf("At level %d \t: ", level);
	//print_array(arr, 12);
	swap(arr, left, last);
	#pragma omp parallel sections
	{

	printf(" Num threads :%d\n", omp_get_num_threads());
		#pragma omp section
		qsort(arr, left, last -1, level + 1);
			
		#pragma omp section
		qsort(arr, last + 1, right, level + 1);
		
	}

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
	int array[] = {255,180,4,
        240,156,4,
        220,124,4,
        156,71,4,
        72,20,4,
        251,180,4,
        180,74,4,
        180,70,4,
        164,91,4,
        100,28,4,
        191,82,4,
        47,5,4,
        138,39,4,
        81,27,4,
        192,89,4,
        61,27,4,
        216,148,4,
        71,14,4,
        142,48,4,
        196,102,4,
        58,9,4,
        132,45,4,
        95,15,4,
        92,21,4,
        166,59,4,
        244,178,4,
        194,121,4,
        120,41,4,
        53,14,4,
        80,15,4,
        23,3,4,
        249,204,4,
        97,25,4,
        124,30,4,
        151,57,4,
        104,36,4,
        239,171,4,
        131,57,4,
        111,23,4,
        4,2,4};
	
	//int array[] = {1,4,6,8,9,2,4,6,4,5,7,2};
	print_array(array, 117);
	qsort(array, 0, 117, 0);
	printf(" Num threads :%d\n", omp_get_num_threads());
	printf("Final\n");
	print_array(array, 117);
	return 0;

}

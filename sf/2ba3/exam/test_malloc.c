#include <stdio.h>
#include <stdlib.h>

int main(){

   int *p;
   p = malloc(sizeof(int) * 100);
   p[40] = 5;
   printf("%d %d", p[40], p[41]);

   return 0;
}

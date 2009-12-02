#include <stdio.h>

int main(){
   
   FILE * fp;
   fp = fopen("characters.txt","w");
   int i;
   for(i = 0; i <= 255; i++){
      fprintf(fp,"%c\n",i);
      printf("%c\n",i);
   }

   return 0;
}

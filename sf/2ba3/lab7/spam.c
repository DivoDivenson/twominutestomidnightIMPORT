#include <stdio.h>
#include <stdlib.h>

char * import_mut(){ /*Read in mutations file, return it in character array.
                      Error checking is for girls*/
  FILE *fp;
  fp = fopen("mutations.txt","r");
  char *result[257];
  int i;
  for(i=0; i <= 256; i++){
    result[i] = malloc(1);
    fscanf(fp,"%s\n",result[i]);
    printf("%s",result);
  }

}

int main(){
  
  char * mutations = import_mut();
  
  return 0;
}

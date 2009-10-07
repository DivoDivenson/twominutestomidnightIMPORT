/* $Id$ */
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

#define MAX_STRING 256


int str2int(char str[],int length,int * success){ //I pass in the length, but what do I do with it?
  int num1;
  if(sscanf(str, "%d", &num1)){
     *success = 1;
  }else{
    *success = 0;
  }
  printf("%d\n",num1);
}

main(){

  char buffer[MAX_STRING];
  fgets(buffer, MAX_STRING, stdin);
  int strlength = strlen(buffer);
  int valid = 0;
  int value;
  value = str2int(buffer, strlength, &valid);
  return !(valid);

}

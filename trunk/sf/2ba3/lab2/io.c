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

int str2float(char str[],int length,int * success){ //I pass in the length, but what do I do with it?
  float num1;
  if(sscanf(str, "%f", &num1)){
     *success = 1;
  }else{
    *success = 0;
  }
  printf("%f\n",num1);
}

int str2double(char str[],int length,int * success){ //I pass in the length, but what do I do with it?
  double num1;
  if(sscanf(str, "%lf", &num1)){
     *success = 1;
  }else{
    *success = 0;
  }
  printf("%lf\n",num1);
}

int str2hex(char str[],int length,int * success){ //I pass in the length, but what do I do with it?
  int num1;
  if(sscanf(str, "%x", &num1)){
     *success = 1;
  }else{
    *success = 0;
  }
  printf("%x\n",num1);
}

main(){

  char buffer[MAX_STRING];
  int valid = 0;
  int value;

  printf("Hex\n");
  fgets(buffer, MAX_STRING, stdin);
  int strlength = strlen(buffer);
  value = str2hex(buffer, strlength, &valid);

  printf("Int\n");
  fgets(buffer, MAX_STRING, stdin);
  strlength = strlen(buffer);
  value = str2int(buffer, strlength, &valid);

  printf("Double\n");
  fgets(buffer, MAX_STRING, stdin);
  strlength = strlen(buffer);
  value = str2double(buffer, strlength, &valid);

  printf("Float\n");
  fgets(buffer, MAX_STRING, stdin);
  strlength = strlen(buffer);
  value = str2float(buffer, strlength, &valid);
  return !(valid);

}

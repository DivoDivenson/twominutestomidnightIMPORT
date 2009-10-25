#include <stdlib.h>  //exit() is in here
#include <stdio.h>
#include <string.h>
#define MAXLINES 100 //Max no line in input file
#define MAXLEN   81  //Max length of any line in input file

char *dict[MAXLINES];
/* Reads a file, input, in from disk and stores each line as seperate string.
   Result is an array of character _pointers_. 
   Each pointer is the location of one of the strings*/
int read_in(char *result[], int maxline, char *input){
  int noLines,length;
  char * eof, * p;
  char line[MAXLEN];

  FILE * file;
  file = fopen(input, "r");
  noLines = 0;

  while((eof = fgets(line, maxline, file)) != NULL){
    length = strlen(line);
    line[length-1] = '\0';
    p = malloc(length); //Turns out this bit is VERY important
    strcpy(p,line);
    result[noLines++] = p;
  }

  return noLines;
}

/*pseudo split an array into two. Inserts a terminating character at the first space (' ')
  in input and writes a pointer to the next element into the result */
void split(char *input[],char *result[], int length){
  int dict_index,line_index;

  for(dict_index = 0; dict_index < length; dict_index++){
    line_index = 0;
    while((*(input[dict_index] + line_index)) != ' '){
      // if(*(input[dict_index] + line_index) == ' '){
        //  *(input[dict_index] + line_index +1) = '\0';
        line_index++;
    }
      *(input[dict_index] + line_index +1) = '\0';
     // result[dict_index] = malloc(1);STOPPED HERE, NOT WORKING
      result[dict_index] = (input[dict_index] + line_index);
    printf("%s %s\n",input[dict_index],result[0]);
   // *(input[dict_index] + line_index) = '\0';
  }


}

int main(){

  int nLines;
  nLines = read_in(dict,MAXLEN, "dict.txt");
  char *american[nLines];
  split(dict,american,nLines);
  return 0;
}



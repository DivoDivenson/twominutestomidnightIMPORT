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
  int line_index,char_index;

  for(line_index = 0; line_index < length; line_index++){
    char_index = 0;
    while((*(input[line_index] + char_index)) != ' '){
        char_index++;
    }
    *(input[line_index] + char_index) = '\0'; /*Assume the while loop exits with line 
                                                   index pointing to the first space.*/

    result[line_index] = (input[line_index] + char_index + 1); /*Store a pointer to the begging of the next string into
                                                                 result */
  }


}

int main(){

  int dictLines,nLines;
  dictLines = read_in(dict,MAXLEN, "dict.txt");
  char *american[dictLines],*input_file[MAXLINES];
  split(dict,american,dictLines);
  nLines = read_in(input_file,MAXLEN, "translate.txt");
  
  return 0;
}



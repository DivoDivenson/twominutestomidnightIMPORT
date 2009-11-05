#include <stdlib.h>  //exit() is in here
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#define MAXLINES 100 //Max no line in input file
#define MAXLEN   81  //Max length of any line in input file
/* Does not detect punctuation or translate to  Californian.*/

char *dict[MAXLINES];

//void numWords();

/* Reads a file, input, in from disk and stores each line as seperate string.
   Result is an array of character _pointers_. 
   Each pointer is the location of one of the strings
 
 
  Bug: If the input file is only one word, rubbish is inserted as the second entry in result.
  No idea why.  */
int read_in(char *result[], int maxline, char *input){
   int noLines,length;
   char * p;
   char line[MAXLEN];
        
   FILE * file;
   file = fopen(input, "r"); //Assume file is there
   noLines = 0;

   while(fgets(line, maxline, file) != NULL){
      length = strlen(line);
      line[length-1] = '\0';
      p = malloc(length); //Turns out this bit is VERY important
      strcpy(p,line);
      result[noLines++] = p;
    }
   return noLines;
}

/*pseudo split an array of two strings per line into two. 
  Inserts a terminating character at the first space (' ')
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
      result[line_index] = (input[line_index] + char_index + 1); /*Store a pointer to the begging 
                                                                   of the next string into result */
   }
}

/*Seperate each word in a given string into a its own postion in result array. 
  The words are copied, not  just pointers to them. 
  This makes switching them really simple but uses many memories. */

void separate(int length, char *input[], char *result[]){
   int line_Index;
   int char_Index, resIndex = 0;
   char *temp,*word; 
   //Parse each line in input.
   for(line_Index = 0; line_Index < length; line_Index++){ 
      char_Index = 0;

      while(char_Index != -1){
         char *word = malloc(MAXLEN); //Create string to store result of getword
         char_Index = getWord(input[line_Index],word,char_Index);
         char *final = malloc(strlen(word) + 1); //Create another string to store the result, to save memory
         strcpy(final,word);
         //free(word); // Calling free here causes the program to crash at runtime.
         result[resIndex++] = final;
      }
   }
   result[resIndex] = "EOF"; /*Another cheat. Assume that "EOF" will never
                               appear in the input...*/
}

/* separate and getWord should be 10 line collectivly. Oh well */

/* Tokenise method, returns the position of where it left off */
int getWord(char *input, char *word, int index){
   
   char line[MAXLEN]; //Temp array to avoid changing the input
   strcpy(line,input);

   while(isspace(line[index])){
      index++;
   }
   if(ispunct(line[index]) ){
//      printf("Punct\n");
      ;
   }else if(line[index] == '\0'){
      index = -1;
      strcpy(word,"\0");  /*Cheat. Insert terminating charcter as 
                            string to signify the start of a new line
                           Assuming the "\0" never appears in the input file*/
   }else{
      char *start = &line[index];
      while(isgraph(line[index])){
         index++;
      /*   if(ispunct(line[index])){
            printf("Punct\n");
      //Detect punct here, but what to do with it?      
            I dont care
         }*/                 
      }
      line[index] = '\0';
      strcpy(word,start);
   }
   return index;
}

/* Search text of entries in search and 
   replace them with the string in replace. 
   The word, if found, shares the same index in replace
   */
void replace(char *text[], char *search[], char *replace[]){
   int textIdx,searchIdx;
   char *replacement;

   for(textIdx = 0; strcmp(text[textIdx],"EOF"); textIdx++){
      for(searchIdx = 0; search[searchIdx] != NULL; searchIdx++){
         if(!strcasecmp(search[searchIdx],text[textIdx])){
            replacement = malloc(strlen(replace[searchIdx]));
            strcpy(replacement,replace[searchIdx]);
            text[textIdx] = replacement;
         }
      }
   }

}
/* At the moment just dumps out a printf. 
   Change to return a string of formated output with strcnt() */
void printOut(char *print[]){
   int index;

   for(index = 0; strcmp(print[index],"EOF"); index++){
      if(!strcmp(print[index],"\0")){
        printf("\n");
      }else{
         printf("%s ", print[index]);
      }
   }
}





int main(){



   int dictLines,nLines;
   dictLines = read_in(dict,MAXLEN, "dict.txt");

   char *american[dictLines],*input_file[MAXLINES];
   split(dict,american,dictLines);

   nLines = read_in(input_file,MAXLEN, "translate.txt");
   char *inputSeparate[10000]; /*Assumption about the number of
                                 words in the file to translate. Ugh */
   separate(nLines,input_file,inputSeparate);

   replace(inputSeparate,dict,american);
   printOut(inputSeparate);

   return 0;
}



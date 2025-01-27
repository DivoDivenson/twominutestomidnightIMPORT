#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAXLEN 2


int read_in(char *result[], int maxline, char *input){
   //int noLines = 0
   int length;
   char * p;
   char line[MAXLEN];
   int x = 0;   //No lines to return, noLines stopped working
        
   FILE * file;
   file = fopen(input, "r"); //Assume file is there

   while(fgets(line, maxline, file) != NULL){
      length = strlen(line);
      line[length-1] = '\0';
      p = malloc(length); //Turns out this bit is VERY important
      strcpy(p,line);
      result[x] = p;
      x++;
    }
   fclose(file);
   return x;


}

//Return 1 if the text is spam, 0 if not//
int spam_search(char word[], int max_length, char text[], char *mutations[]){
   /*Copy the input to text to avoid changing it*/
   char temp[strlen(text)];
   strcpy(temp,text);

   int i;
   /*First, go through text[] swapping out any mutated characters*/
   for(i = 0; temp[i]!='\0'; i++){
      //printf("%c\n",mutations[temp[i]+1][0]);  //text[i]+1, +1 to account for array starting at 0
      if(mutations[text[i]+1][1] != '\0'){
         temp[i] = mutations[temp[i]+1][1];
      }
   }
   //printf("%s\n",temp); //Demo working function
   
   char *token; 
   token = strtok(temp," ");
   do{
    if(strcmp(token,word)==0){
      return 1;
    }
   }while((token = strtok(NULL," ")) != NULL);
   return 0;
}

int main(){
  
  char * mutations[256];
  int x;
  read_in(mutations,4,"mutations.txt");
  char text[] = "w0rd $ir and stuff\0";

  if(spam_search("word",1,text,mutations)){
    printf("Spam\n");
  }else{
    printf("Not spam\n");
  }
  return 0;
}

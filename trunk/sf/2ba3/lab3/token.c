#include <stdio.h>
#define  RESULTSIZE  4
/*Returns the position of the next ' ' char in str[], -1 if '\0'
  Writes every char until ' ' to result[]
  If str[start]==' ' on first pass, start is incremented until something else is found */

int tokenise(char str[], int start, char result[]){
  int resIdx = 0; //Index of result array
  int found =  0; //Boolean, could also use break.
  result[0] = '\0'; /*Clearing result array. If str[] has spaces at the end, 
                      tokenise won't do anything and result will be unchaged,
                      meaning the previous result will be printed.
                      It only does this at the end and only twice. No idea why.*/


  while(found == 0 && str[start] != '\0'){
    if(str[start] == ' '){
      if(resIdx > 0){  //If valid chars have been found, finish up. The result_index > 0 if valid chars are found
        found = 1;
        result[resIdx] = '\0';
      }
      start++;
    }else{  //assume valid if not a space.
      if(resIdx <= RESULTSIZE){  //Stop from going out of bounds.
        result[resIdx] = str[start];
        resIdx++; 
        start++;
      }else{
        found = 1; 
        result[resIdx] = '\0';
      }
    }
   }
  
<<<<<<< .mine
  if(str[start] == '\0'){  //Doing same check twice...
    printf("%d\n", start);
=======
  if(str[start] == '\0'){  //Doing same check twice...
>>>>>>> .r56
    start = -1;
  }
  return start;
}



<<<<<<< .mine
main(){
char line[] = "The glorified bricklayer picks up a spare\0 "; //Error if space at the end of the string
char result[RESULTSIZE];
char start=0;
=======
int main(){
  char line[] = "      The     glorified bricklayer picks up a       spare              "; //Error if space at the end of the string
  char result[RESULTSIZE];
  char start=0;
>>>>>>> .r56

<<<<<<< .mine
//Assuming line is a valid string.
while( start != -1){
  start = tokenise(line, start, result);
  printf("%s\n",result);
}
=======
  while( start != -1){
    start = tokenise(line, start, result);
    printf("%s\n",result);
  }

>>>>>>> .r56
return 0;
}

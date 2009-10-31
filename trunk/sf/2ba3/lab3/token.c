#include <stdio.h>
#define  RESULTSIZE  4
/*Returns the position of the next ' ' char in str[], -1 if '\0'
  Writes every char until ' ' to result[]
  If str[start]==' ' on first pass, start is incremented until something else is found */

int tokenise(char str[], int start, char result[]){
  int resIdx = 0;
  int found = 0;
  while(found == 0 && str[start] != '\0'){
    if(str[start] == ' '){
      if(resIdx > 0){  //If valid chars have been found, finish up
        found = 1;
        result[resIdx] = '\0';
      }
      start++;
    }else{  //assume valid char if not a  ' '.
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
  
  if(str[start] == '\0'){  //Doing same check twice...
    start = -1;
  }
  return start;
}



int main(){
  char line[] = "      The     glorified bricklayer picks up a       spare              "; //Error if space at the end of the string
  char result[RESULTSIZE];
  char start=0;

  while( start != -1){
    start = tokenise(line, start, result);
    printf("%s\n",result);
  }

return 0;
}

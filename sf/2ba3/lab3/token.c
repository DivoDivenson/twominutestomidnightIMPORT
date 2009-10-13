/*************Please redo me from scratch. This is a crap
 * algorhythm. OR AT LEAST REMOVE THIS MESSAGE************/


#include <stdio.h>
#define  RESULTSIZE  40
int tokenise(char str[], int start, char result[]){
  int resIdx = 0;
  int found = 0;
  while(found == 0 && str[start] != '\0'){
    if(str[start] == ' '){
      if(resIdx > 0){
        found = 1;
        result[resIdx] = '\0';
      }
      start++;
    }else{  //assume valid
      if(resIdx <= RESULTSIZE){
        result[resIdx] = str[start];
        resIdx++; //Make it explicit that ints are being incremented?
        start++;
      }else{
        found = 1;
        result[resIdx] = '\0';
      }
    }
   }
  
  if(str[start] == '\0'){
    start = -1;               //Making check once until i figure out a
                              //better way. ie, rewrite the entire method!
  }
  return start;
 
    
}



main(){
char line[] = "The glorified bricklayer picks up a spare\0";
char result[RESULTSIZE];
char start=0;

start = tokenise(line, 0, result);
while( start != -1){
  printf("%s\n",result);
  start = tokenise(line, start, result);
}
return 0;
}

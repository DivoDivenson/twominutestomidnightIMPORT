#include <stdio.h>

int tokenise(char str[], int start, char result[]){
  int resIdx = 0;
  int found = 0;
  printf("%d\n",start);
  while(found != 1 || str[start] != '\0'){
    if(str[start] == ' '){
      if(resIdx > 0){
        found = 1;
        printf("Found!");
      }
      start++;
    }else{  //assume valid
      result[resIdx] = str[start];
      resIdx++; //Make it explicit that ints are being incremented?
      start++;
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
char result[256];
char start=0;

start = tokenise(line, 0, result);
printf("%s %d\n",result,start);
return 0;
}

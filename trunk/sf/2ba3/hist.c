#include <stdio.h>

main(){
  int c, x, y;
  int digit[20];
  c = x = y = 0;
  while(x <= 20){
    digit[x] = 0;
    x++;
  }
  x = 0;

  while((c = getchar()) != 'p'){
    if(c != ' '){
	digit[x]++;
    }else{
	x++;
    }
  }
  x = 0;

  while(x <= 20){
    printf("%d ,",digit[x]);
    x++;
  }
 
  y = 10;
  x = 0;
  printf("\n");

  while( y >= 0 ){
   while( x <= 20 ){
	if(digit[x] >= y){
	  printf("-"); 
	}
     x++;
     
   }
   x = 0;
   printf("%d  \n",y);
   y = y - 1;
  }
	
}




import java.util.*;
import java.lang.Math;
class Star{
 public static void main(String Args[]){

  int stars[] = new int[18910];
  int tris[] = new int[65536];

  for(int n = 1; n < stars.length; n++){
    stars[n] = ( 6 * n ) * ( n - 1 ) + 1;
  }

  for(int n = 0; n < tris.length; n++){
   tris[n] = ((n*n) + n) / 2;
  }
  
  ArrayList tristar = new ArrayList();
  System.out.println("Stars: " + stars.length);     
  System.out.println("Tris: " + tris.length);     
  
  //FUCK THIS it made it slower.Must have cocked up somewhere 
  for(int number = 0; number < stars.length;number++){  //Fancy pants way of searching through two arrays. Does that search through half pokery
    int search = tris.length / 2;
    int half = search;
    int total = tris.length -1;
//  System.out.println(" Number: " + number); 

    
    while(search != 0){
    //System.out.println(" Tris: " + search + " " + stars[number] + " " + tris[search-1]);
       if(stars[number] == tris[search-1]){
        // tristar.add(stars[number]);
         search = 0;
         System.out.println("Added number " + stars[number]);
       }else if(search == total){
          total = half - 1;
          search = total / 2;
          half = search;
       }else{
          search++;
       }
    }
  } 

  /*for(int number = 0;number < stars.length;number++){  //Brute force approach to see if it verks.
    int search = 0;
    while(search != tris.length){
      if(stars[number] == tris[search]){
        System.out.println("Added the number" + stars[number]);
        break;
      }else{
        search++;
      }
    }
  }*/ 
 }
}

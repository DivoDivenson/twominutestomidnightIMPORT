class Number{
  int stars[] = new int[18910];
  int tris[] = new int[65535];
  
  public Number(){
      for(int n = 0; n < stars.length; n++){
        stars[n] = ( 6 * n ) * ( n - 1 ) - 1;
       }

      for(int n = 0; n < tris.length; n++){
        tris[n] = ((n*n) + n) / 2;
      }
  }

  public int[] getStar(int x){
    return stars[x];
  }

  public int[] getTris(int x){
    return tris[x];
  }
}

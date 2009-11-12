class Diamond{
int width;
int[] numbers;
char[][] diamond;
int current;
  public Diamond(int width){
    this.width = width;
    if(width % 2 !=0){ //odd
      current = 1;
      numbers = new int[width/ 2 + 1 ];
      diamond = new char[width][];
    }else{//even
      current = 2;
      numbers = new int[width/ 2 + 1 ];
      diamond = new char[width -1][];
    }
    create();
  }

  private void create(){
    for(int x =0; current <= width; x++){
      numbers[x] = current;
      current = current +2;
      System.out.println("Number " + numbers[x] + " " + x);
    }
    for(int count = 0; count < diamond.length/2 +1; count++){
      diamond[count] = new char[width];
      int dia = (width - numbers[count]) / 2;
      for(int row = 0; row <= diamond[count].length -1; row++){
          if(row < dia || row >= (dia + numbers[count])){ //If its a blank space insert a space
          diamond[count][row] = ' ';
        }else{
          diamond[count][row] = '*';
        }
      }
    }
    for(int count = diamond.length/2 + 1; count < diamond.length; count++){
      int countAccess = numbers.length -1;
      diamond[count] = new char[width];
      int dia = (width - numbers[countAccess]) / 2;
      for(int row = 0; row <= diamond[count].length -1; row++){
          if(row < dia || row >= (dia + numbers[countAccess])){ //If its a blank space insert a space
          diamond[count][row] = ' ';
        }else{
          diamond[count][row] = '*';
        }
      }
      countAccess--;
    }

    stringOf();
  }
  public void stringOf(){
    for(int col = 0; col < diamond.length; col++){
      for(int row = 0; row < diamond[col].length; row++){
        System.out.print(diamond[col][row]);
      }
      System.out.println("");
   }
 }


}

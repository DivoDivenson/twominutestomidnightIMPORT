import java.util.Scanner;

public class Lock{
  int combination;
  boolean locked;
  int attempts;
  

  public Lock(int combination){
    this.combination = combination;
    this.locked = true;
    
    this.attempts = 0;
  }

  public void isLocked(){
    System.out.println("It is locked is " + Boolean.toString(locked));
  }

  public void isJammed(){
    System.out.println("It is jammed is " + Boolean.toString(Jammed()));
  }

  private boolean Jammed(){
    if(attempts >= 3){
      return true;
    }else{
      return false;
    }
  }

  public void Open(int attempt){
    if(!Jammed()){
      if(attempt == combination){
        locked = false;
     }else{
      attempts++;
     }
    }
  }
}

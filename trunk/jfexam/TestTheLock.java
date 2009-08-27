class TestTheLock{
  public static void main(String args[]){
	Lock lock = new Lock(333);
  Lock lock2 = new Lock(333);
	lock.isLocked();
  lock.Open(111);
  lock.Open(111);
  lock.Open(111);
  lock.isJammed();
  System.out.println("\nNow onto lock #2");
  lock2.Open(333);
  lock2.isLocked();
	}
}

package fib;

public class fib {

	public static int count;
	
	public long fib(long num){
		count++;
		if(num == 1 || num == 0) return num;
		else
		return fib(num - 1) + fib(num - 2);
	}
}

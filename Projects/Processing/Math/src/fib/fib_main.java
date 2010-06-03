package fib;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class fib_main {

	public static void main(String Args[]){
		fib temp = new fib();
		NumberFormat f = new DecimalFormat("#,###,###");
		System.out.println(f.format(Long.MAX_VALUE));
		System.out.println(f.format(temp.fib(40)));
		System.out.println(f.format(temp.count));
	}
}



proctype gcd(byte a, b) {
	do
	  :: 	a >= b -> a = a - b;
		printf(" a is %d and b is %d \n",a,b);
	  ::	a <= b -> b = b - a;
		printf("a is %d and b is %d  \n",a,b);	  
		if
	  		:: 	a == 0 -> printf("the gcd = %d \n",b);break;
	  		::	b == 0 -> printf("the gcd = %d \n",b);break;
		fi; 
	od
}





init {
 	run gcd(15,3); 
}


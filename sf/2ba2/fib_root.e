 

class

        FIB_ROOT

 

creation 

        make

 

feature  

 

        make is

               local

                       f: FIB;

                       n: INTEGER

               do

                       create f;

                       print ("Input a Nat. Num. ");

                       io.read_integer;

                       n := io.last_integer;

		       print("%NUsing fib1 gives : ")

                       io.put_integer (f.fib1 (n));

		       print("%NUsing fib2 gives : ")

                       io.put_integer (f.fib2 (n));

		       print("%NUsing fib3 gives : ")

                       io.put_real_format (f.fib3 (n), 0);

                       print("%NUsing original Recursive function : ")

                       io.put_integer (f.fib (n));

		       io.read_character;
		       io.read_character

               end; -- make

        

end -- class FIB_ROOT


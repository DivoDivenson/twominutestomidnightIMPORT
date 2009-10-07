 

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

                       io.put_integer (fib_rep (n));

		      
		       io.read_character;
		       io.read_character

               end; -- make

      
	fib_rep(n:INTEGER) is
	-- Zeckendorf representation
	local
			f: FIB;
			m, mxf : INTEGER
		do
		from
		create f;
			m := n;
			mxf := f.max_fib(m)
		until
			m = mxf	
		loop
			io.put_integer(mxf);
			io.put_character('+');
			m := m - mxf ;
			mxf := f.max_fib(m)
		end;
			io.put_integer(mxf)
	end; -- fib_rep

end -- class FIB_ROOT


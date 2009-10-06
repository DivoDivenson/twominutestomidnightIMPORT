class
	FIB


feature  

	fib (k: INTEGER): INTEGER is
			--recursive version of fibonnaci
		require
			pre_fib: k >= 0
		do
			if k = 0 then
				Result := 0
			elseif k = 1 then
				Result := 1
			else
				Result := fib (k - 2) + fib (k - 1)
			end
		end;
	
	fib1 (k: INTEGER): INTEGER is
		require
			pre_fib: k > 0
		local
			i, p, c, n: INTEGER
		do
			from
				p := 0;
				c := 1;
				i := 1
			until
				i = k
			loop
				n := p + c;
				p := c;
				c := n;
				i := i + 1
			end;
			Result := c
		end;
	
	fib2 (k: INTEGER): INTEGER is
			-- saving an entity location
		require
			pre_fib: k > 0
		local
			i, c, n: INTEGER
		do
			from
				c := 0;
				n := 1;
				i := 1
			until
				i = k
			loop
				n := n + c;
				c := n - c;
				i := i + 1
			end;
			Result := n
		end;
	
	fib3 (k:  INTEGER): INTEGER is
		local
			r: REAL;
			m: MATH_CONSTANTS
		do
			r := (m.Phi^k)/(5.0).sqrt;
			Result := my_round(r)
		end;

	my_round (x : REAL) : INTEGER is
		do
			Result := x.rounded.force_to_integer_32
		end --  my_round
				

 


end -- class FIB


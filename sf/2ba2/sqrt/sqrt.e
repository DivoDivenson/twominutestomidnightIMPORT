class SQRT 

feature

	simp_sqrt(x:REAL):INTEGER is
	require
		pre_sq_rt:	x >= 0.0
	local
	   r : INTEGER
	do
		from
			r := 0; 
		until
			((r+1)*(r+1)).to_real_32 > x
		loop
			r := r+1
		end
		Result := r
	ensure
	  post_sq_rt:	Result^2 <= x and x < (Result+1)^2
	end -- simp_sqrt

-----------------------------------------------------------------------------------


	floor_sqrt(x:REAL):INTEGER is
		require
			pre_sq_rt:	x >= 0.0
		local
	   		r,n,s : INTEGER
		do
	  		from
				r := 0; n := 1; s := 1
	  		until
				s > x
	  		loop
				r := r+1; n := n+2; s := s+n
	  		end
	  		Result := r
		ensure
	  		post_sq_rt:	Result^2 <= x and x < (Result+1)^2
		end -- floor_sqrt

-----------------------------------------------------------------------------------


	bin_sqrt_r (low, high: REAL; tol: REAL; x: REAL): REAL is
        -- (Recursive version)
		require
			within: low ^ 2 <= x and x < high ^ 2 
		local
			mid: REAL
		do
			if low + tol < high then
				mid := (low + high) / 2;
				if mid ^ 2 - x <= 0 then
					Result := bin_sqrt_r (mid, high, tol, x)
				else
					Result := bin_sqrt_r (low, mid, tol, x)
				end
			else
				Result := low
			end
		ensure
			Result ^ 2 <= x and x < (Result + tol) ^ 2
		end;

	sqrt_r (x: REAL): REAL is
		require
			pre_sqrt_r: x >= 0.0
		local
			y: REAL
		do
			from
				y := 1
			until
				y ^ 2 > x
			loop
				y := 2 * y
			end;
			 Result := bin_sqrt_r (0, y, 0.0001, x) 
		end;

-----------------------------------------------------------------------------------

	newton_sqrt(n,x,tol:REAL):REAL is
		-- find solution to x^2 - n = 0
		-- i.e. find the (+)square root of n
		local
			t : REAL
		do
			if abs_val(x*x - n) < tol then
				Result := x
			else
				t := (x*x + n)/(2*x)
				Result := newton_sqrt(n,t,tol)
			end
		end -- newton_sqrt

	sqrt_n(n:REAL):REAL is
		do
			Result := newton_sqrt(n,1,0.0001)
		end -- sqrt_n	
		
	abs_val(x:REAL):REAL is
		do
			if x < 0 then
				Result := -x
			else
				Result := x
			end
		end -- abs_val



end -- SQRT

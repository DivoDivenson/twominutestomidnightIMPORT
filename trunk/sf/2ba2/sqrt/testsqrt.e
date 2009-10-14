class TESTSQRT 
  creation
	make
  feature

	make is
	  local
	  	s  : SQRT
		x,r : REAL
		n : INTEGER
	  do	
		create s
		print("Input a number :")
		io.read_real
		x := io.last_real
		print("%NSimp_sqrt is : ")
		n := s.simp_sqrt(x)
		io.put_integer(n)
		print("%NFloor_sqrt is : ")
		n := s.floor_sqrt(x)
		io.put_integer(n)
		io.put_new_line
		print("%NBinary sqrt is : ")
		r := s.sqrt_r(x)
		io.put_real(r)
		io.put_new_line
		print("%NNewton sqrt is : ")
		r := s.sqrt_n(x)
		io.put_real(r)
		io.put_new_line
		print("%N'Eiffel' sqrt is : ")
		r := x.sqrt
		io.put_real(r)
		io.put_new_line
		io.read_character
	  end -- make
	  
end -- TESTSQRT 

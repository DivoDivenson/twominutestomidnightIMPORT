class FLOYD_RAND
creation
	make
feature


	make is
		local
		do
      
		end -- make
		
	
	floyd_perm(m, n : INTEGER): ARRAY[INTEGER] is 
			local
				i,j,k,rn  : INTEGER
				t: TIME
				rand_gen : PRESS_RANDOM_NUMBER_GENERATOR
				s : ARRAY[INTEGER]
			do
				from
					k := n-m+1	
					create s.make(1,m)
					t.update
					create rand_gen.with_seed(t.second)
					j := 1
				until
					j > m
				loop
					rand_gen.next
					rn := rand_gen.last_integer(k)
					if s.has(rn) then
						i := s.fast_first_index_of (rn)
						insert(k,i+1,s,i,j-1)
					else
						s.put(rn,j)
						s.swap(1,j)
					end
					k := k+1
					j := j+1
				end
				Result := s
			end -- floyd_perm
		

	insert(x: INTEGER; i: INTEGER; a: ARRAY[INTEGER]; low, high: INTEGER) is 
		  -- insert x at position i in array a.
		  require 
			 Range: low <= i and i <= high + 1; 
		  local 
			 k: INTEGER;
		  do  
			 from 
				k := high + 1;
			 until 
				k = i
			 loop 
				a.put(a.item(k - 1),k);
				k := k - 1;
			 end; 
			 a.put(x,i);
		  end -- insert

      

	end -- FLOYD_RAND
 

class BOARD
creation
	make
feature


	make (size : INTEGER) is
		local
			sz : INTEGER
			rand_perm : ARRAY[INTEGER]
      board_array : ARRAY2[INTEGER]
      rand : FLOYD_RAND
      i,rowi,coli :INTEGER
		do
      sz := size * size -- User enters N, board has N*N spaces
      !!rand.make;
      rand_perm := rand.floyd_perm(sz-1,sz-1) -- -1 to account for blank space
      !!board_array.make(1,size,1,size)
        from
          i := rand_perm.lower
          rowi := 1
          coli := 1
        until
          i > rand_perm.upper
        loop
          if (i \\ size) = 0 then
             coli := coli + 1
             rowi := 1
          end
        board_array.put(rand_perm.item(i), rowi, coli)
        io.put_character('%N')
        io.put_integer(rand_perm.item(i))
        io.put_character(' ')
        io.put_integer(rowi)
         io.put_character(' ')
        io.put_integer(coli)
  
        rowi := rowi + 1
        i := i + 1
      end --Loop end
        board_array.put(0,3,1) --PROBLEM
        print_array(rand_perm)
        print_board(board_array,size)

		end -- make
		


  print_board(a : ARRAY2[INTEGER]; size : INTEGER ) is 
  --Prints a square 2d array
  --Where size is the length of a row
		local
			i,rowi,coli : INTEGER
		do
        from -- Loop through the rows
          i := 1
          rowi := 1
          coli := 1
        until
          i = size * size
        loop
          if(i \\ size) = 0 then
            coli := coli + 1
            rowi := 1
            io.put_character('%N')
          end
          io.put_string("%T|%T")
          if a.item(rowi,coli) = 0 then
            print("B")
          end
          io.put_integer(a.item(rowi,coli))
          rowi := rowi + 1
          i := i + 1
        
        end --Loop end
 
	end -- print_array

  print_array(a : ARRAY[INTEGER] ) is
      local
            k : INTEGER
                do
                      from
                              io.put_new_line
                                      k := a.lower
                                            until
                                                    k = a.upper
                                                          loop
                                                                  io.put_integer(a.item(k))
          io.put_character(',')
                  k := k+1
                        end
                              io.put_integer(a.item(k))
        io.put_new_line
            end -- print_array

  
end --BOARD
 

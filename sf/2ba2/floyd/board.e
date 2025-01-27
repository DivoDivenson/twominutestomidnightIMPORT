class BOARD
creation
	make
feature
  board_array : ARRAY2[INTEGER] --2D array, the actual 'board'
  sz,size_row : INTEGER --Number of elements total, number of elements per row
  blank_row,blank_col : INTEGER --Grid cords of the blank tile

	make (size : INTEGER) is
		local
			rand_perm : ARRAY[INTEGER] --Result from FLOYD_RAND class
      rand : FLOYD_RAND
      i,rowi,coli :INTEGER
    do
      size_row := size  -- User enters N
      sz := size * size -- User enters N, board has N*N spaces
      !!rand.make;
      
      from
      rand_perm := rand.floyd_perm(sz-1,sz-1) -- -1 to account for blank space
      until 
        (inversions(rand_perm,sz-1,sz-1) \\ 2) = 0 --Keep generating a board until
                                                   --a solvable one is made
      loop
      rand_perm := rand.floyd_perm(sz-1,sz-1)
      end

      !!board_array.make(1,size,1,size)

      blank_row := size --Position of the blank tile. First set to lower left corner
      blank_col := size --These vars are manually updated...

        from  --Parse through the "board" and copy in the random numbers from FLOYD_RAND
              --Basic 2D array parse code used throught program
          i := rand_perm.lower
          rowi := 1
          coli := 1
        until
          i > rand_perm.upper
        loop
        board_array.put(rand_perm.item(i), rowi, coli)
        
        if (i \\ size) = 0 then
          coli := coli + 1
          rowi := 1
          i := i + 1
        else
          rowi := rowi + 1
          i := i + 1
        end
 
      end --Loop end

		end -- make
  
  print_out is  --Print the board, shield client from "print_board" arguments
    do
      print_board(board_array,size_row)
    end


  won : BOOLEAN is  --Return true if user has won
                    --Simply counts from 1 up through the board, comparing
                    --each array position with the count. If the array elements are in 
                    --sequence the game has been one
    local
      i,rowi,coli : INTEGER
    do
        from
          i := 1
          rowi := 1
          coli := 1
        until
          i /= board_array.item(rowi, coli) --Until the count does not match the 
                                            --corisponding array value
        loop
        if (i \\ size_row) = 0 then 
          coli := coli + 1
          rowi := 1
          i := i + 1
        else
          rowi := rowi + 1
          i := i + 1
        end
 
      end --Loop end
 
     Result := i = sz --If the count = board size, assume game has been won
    end -- Won

    --Move methods
    --Following 4 methods are more or less the same
  move_up is
    do
      if blank_col -1 < 1 then --Check user is not moving to invalid position

      else
        board_array.swap(blank_row,blank_col,blank_row,blank_col-1)
        blank_col := blank_col -1
      end --if
  end

move_down is
    do
      if blank_col +1 > size_row then -- For move methods, could use "board_array.column_maximum" n' such
                                      -- but just using ints instead. Faster
      else
        board_array.swap(blank_row,blank_col,blank_row,blank_col+1)
        blank_col := blank_col +1
      end --if
  end

move_left is
    do
      if blank_row -1 < 1 then

      else
        board_array.swap(blank_row,blank_col,blank_row -1,blank_col)
        blank_row := blank_row -1
      end --if
  end

move_right is
    do
      if blank_row +1 > size_row  then

      else
        board_array.swap(blank_row,blank_col,blank_row +1,blank_col)
        blank_row := blank_row +1
      end --if
  end


  print_board(a : ARRAY2[INTEGER]; size : INTEGER ) is 
  --Prints a square 2d array
  --Where size is the length of a row
		local
			i,rowi,coli : INTEGER
		do
        from
          i := 1
          rowi := 1
          coli := 1
        until
          i > size * size
        loop
          io.put_string("%T|%T")
          if a.item(rowi,coli) = 0 then
            io.put_string("BB")
          else
            io.put_integer(a.item (rowi, coli))
          end
        if (i \\ size) = 0 then
          coli := coli + 1
          rowi := 1
          i := i + 1
          io.put_string("%T|%T")
          io.put_character('%N')
        else
          rowi := rowi + 1
          i := i + 1
        end
 
      end --Loop end
 
	end -- print_array


  inversions(a : ARRAY[INTEGER]; low,high : INTEGER) : INTEGER is
    local
      i,j,s : INTEGER
    do
      from
        i := low
        s := 0
      until
        i = high
      loop
        from
          j := i + 1
        until
          j > high
        loop
          if a.item(i) > a.item(j) then
             s := s + 1
          end --if
          j := j+ 1
        end --inner loop
        i := i + 1
      end --outer loop
      Result := s
    end --Inversions

end --BOARD
 

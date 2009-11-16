class
    MASTER

create 
      make
feature
      answer:INTEGER

      make is
          do

          end --make

       countbulls (guess: INTEGER): INTEGER is  --Return out number of bulls
          local
            bulls: INTEGER
            count : INTEGER
            tempguess : INTEGER --Hold one digit of guess
            tempans : INTEGER   --Hold one digit of answer
            localguess: INTEGER
            localanswer : INTEGER
          do
            localguess := guess
            localanswer := answer
            from
                count := 0
                bulls := 0
            until
                count = 4  --Might aswell use divide as the counter. 
            loop
                tempguess := localguess \\ 10
                localguess := localguess // 10
                tempans := localanswer \\ 10
                localanswer := localanswer // 10
                if tempans = tempguess then
                    bulls := bulls + 1
                else
                    --nothing
                end
                count := count + 1
            end
            Result := bulls
          end   

       countcows(guess: INTEGER): INTEGER is
          local
            cows,tempguess,tempans,count_guess,count_ans: INTEGER --temp* stores a sigle digit of *
                                                                    --divide_* stores the number to divide * by
            localguess, localanswer : INTEGER
            found : INTEGER
                --Subdivide the guess and the answer to get one digit from each. Compare them and increment cows
                --if they match. Continue until every number has been compared. If the answer contains the same 
                --number twice two cows are counted.Sometimes.
          do
                localanswer := answer
            from
                cows := 0
                count_ans := 0;
            until
                count_ans = 4
            loop
                tempans := localanswer \\ 10;
                localanswer := localanswer // 10
                from
                    count_guess := 0
                    localguess := guess
                    found := 0
                until
                    count_guess = 4
                loop
                    tempguess := localguess \\ 10;
                    localguess := localguess // 10;
                    if count_guess = count_ans then --avoid counting a bull as a cow by skipping 
                                                    --corrisponding positions
                    elseif found = 1 then

                    elseif tempans = tempguess then
                        cows := cows + 1
                        found := 1
                    else

                    end --end if
                    count_guess := count_guess + 1
                end --end nested loop
                 count_ans := count_ans + 1
            end --end loop
            Result := cows
end --end method
       
        genrandom is
          --Generate a random answer
          local
            rand:PRESS_RANDOM_NUMBER_GENERATOR --Eiffel docs said so
              temp,final,count:INTEGER
          do
            from
                count := 0
                !!rand.make
                rand.next
                final := rand.last_integer(6)
                --Generate the first random number, then loop 3 times
                --for the rest. This is because the number must be
                --multiplied by 10 each time
            until
                count =3
            loop
              final := final * 10
              rand.next
              temp := rand.last_integer(6)
              final := final + temp
              count := count + 1
            end
            answer := final
          end --end method
          
          
end --End master

    



                     


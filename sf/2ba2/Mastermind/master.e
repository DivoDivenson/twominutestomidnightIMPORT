class
    MASTER

create 
      make
feature
      answer:INTEGER

      make is
          do
          answer := 1234
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
                --Subdivide the guess and the answer to get one digit from each. Compare them and increment cows
                --if they match. Continue until every number has been compared. If the answer contains the same 
                --number twice two cows are counted.
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
                until
                    count_guess = 4
                loop
                    tempguess := localguess \\ 10;
                    localguess := localguess // 10;
                    if count_guess = count_ans then --avoid counting a bull as a cow by skipping 
                                                      --corrisponding positions
                    elseif tempans = tempguess then
                        cows := cows + 1
                    else

                    end --end if
                    count_guess := count_guess + 1
                end --end nested loop
                 count_ans := count_ans + 1
            end --end loop
            Result := cows
end --end method
                


end

                     


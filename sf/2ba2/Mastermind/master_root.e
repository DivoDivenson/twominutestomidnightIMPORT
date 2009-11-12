class 
    MASTER_ROOT

creation
    make

feature
    make is
       local
           mmind: MASTER
           guess: INTEGER
           bulls: INTEGER
        do
           !!mmind.make;
           from
                guess := 0 
                bulls := 0
           until
                bulls = 4
           loop
                print("%N Please enter guess :");
                io.read_integer;
                guess := io.last_integer
                bulls := mmind.countbulls(guess)
                print("%NBulls: ")
                io.put_integer(bulls)
                print(" Cows: ")
                io.put_integer(mmind.countcows(guess))
           end
         end
end
           

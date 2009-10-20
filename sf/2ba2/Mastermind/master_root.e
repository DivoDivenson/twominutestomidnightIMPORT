class 
    MASTER_ROOT

creation
    make

feature

    make is
       local
           mmind: MASTER
           guess: INTEGER

        do
           create mmind;
           from
                guess := 4 
           until
                (mmind.split(guess)) != 4
           loop
                print("%N Please enter guess :");
                io.read_integer;
                guess := io.last_integer
           end
         end
end
           

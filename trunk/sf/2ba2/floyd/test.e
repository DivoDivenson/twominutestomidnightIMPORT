class
    TEST

creation 
    make

feature
    
    make is
       local
           n:INTEGER
       do
           print("%N Input an Int plox: ");
           io.read_integer;
           n := io.last_integer;
           print("%N You enter: ");
           io.put_integer(n);
       end
end

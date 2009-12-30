class 
    INTERFACE

creation
    make

feature
    make is
       local
           board: BOARD
           size : INTEGER
        do
           print("Soup")
           print("%NPlease input board size: ")
           io.read_integer
           size := io.last_integer
           !!board.make(size);

        end
end
           

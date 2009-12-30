class 
    INTERFACE

creation
    make

feature
    make is
       local
           board: BOARD
        do
           print("Soup")
           !!board.make;
           print("%NPlease input board size: ")
           io.read_integer
           board.sz = io.last_integer

        end
end
           

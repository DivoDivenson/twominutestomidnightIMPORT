class
    MASTER


feature

        guess : ARRAY[INTEGER]


             split (input: INTEGER):INTEGER is
                  --Split user input into 4 integers
              local
              count:INTEGER
              in:INTEGER
              do
                print("Hello")
                 from
                      in := input
                      count := 3
                      !!guess.make(1,4);
                 until 
                      count = 0
                 loop
                      guess.put((in \\ 10),count)
                      in := in // 10
                      count := count - 1
                 end
                 
                 Result := guess.item(2)
              end
end

                     


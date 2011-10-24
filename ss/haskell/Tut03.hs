-- Steven Diviney 08462267
module Tut03 where
import Data.Char

-- Your code here!
main = do
	input <- readFile "input.txt"
	putStr (show (calc input))

calc x = subs ++ [sum subs]
	where subs =  map calcLine (lines x); 

calcLine::String -> Int
calcLine x = foldl step 0 (words x)
	where step acc x = acc + (read x :: Int)

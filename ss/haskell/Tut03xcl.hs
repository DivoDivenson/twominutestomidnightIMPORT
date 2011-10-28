-- Steven Diviney 08462267

module Tut03xcl where
import Data.Char

xcl :: [Int] -> IO ()
xcl stack = do
		putStrLn $ show stack
		line <- getLine
		apply stack $ head $ words line
		

apply stack x
	| all isDigit x  =  xcl ((read x:: Int):stack)
	| otherwise = isOp x stack 

isOp "+" (x:y:xs) = xcl ((x + y):xs)
isOp "-" (x:y:xs) = xcl ((x - y):xs)
isOp "*" (x:y:xs) = xcl ((x * y):xs)
--isOp "/" (x:y:xs) = xcl ((x / y):xs)
isOp "c" stack = xcl []

isOp x stack = xcl stack

main = do
	xcl []

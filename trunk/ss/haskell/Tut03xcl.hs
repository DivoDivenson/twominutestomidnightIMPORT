-- Ste=ven Diviney 08462267

module Tut03xcl where
import Data.Char

xcl :: [Int] -> IO ()
xcl stack = do
		putStrLn $ show stack
		line <- getLine
		apply stack $ head $ words line
		

apply stack x
	| isDigits x  =  xcl ((read x:: Int):stack)
	| otherwise = isOp x stack 

isOp "+" (x:y:xs) = xcl ((x + y):xs)
isOp "-" (x:y:xs) = xcl ((x - y):xs)
isOp "*" (x:y:xs) = xcl ((x * y):xs)
--isOp "/" (x:y:xs) = xcl ((x / y):xs)
isOp "c" stack = xcl []

isOp x stack = xcl stack

	
isDigits (x:[]) = isDigit x
isDigits (x:xs) | isDigit x = isDigits xs
		| otherwise = False	
main = do
	xcl []

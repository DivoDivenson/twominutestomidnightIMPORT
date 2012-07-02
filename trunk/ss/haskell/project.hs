--ghc --make ./project.hs 
import System.Environment
import Run
import Parse
import IO
--import Control.Exception

--HAVE TO FIX THE NEWLINE CHARS IN INPUT FILE
--UNIX NEWLINES ONLY


--Got everything done except for date-fix and grid-fix. Not the most
--robust code in the world but nothing is hard coded so should work with 
--any data thrown at it. Would have liked to complete it ( Haskell is fun :) ) but I really need to start studying
main::IO()
main = do
	execute ([],[],(Just stdout), [""])
--original database and selected part
execute (_, _, Nothing, _) =do
	putStrLn "Daisy Daisy...."

execute (db,sel, file, order) = do
		line <- getLine
		let ins = parse line
		case ins of
			Left str ->do
							putStrLn str
							execute (db,sel,file, order)
			Right command ->do
							new <- try(run (db,sel,file, order) command)
							--Prevent the user from crashing the program
							--Yes I do feel slightly bad doing it this way.
							case new of
										Left e->do
												putStrLn $ "Malformed command "++(show e)
												execute (db, sel, file, order)
										Right res->do
												execute res 



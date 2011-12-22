--ghc --make ./project.hs 
import System.Environment
import Run
import Parse
import IO

--HAVE TO FIX THE NEWLINE CHARS IN INPUT FILE
--UNIX NEWLINES ONLY
main::IO()
main = do
	execute ([],[],(Just stdout))
--original database and selected part
execute (_, _, Nothing) =do
	putStrLn "Daisy Daisy...."

execute (db,sel, file) = do
		line <- getLine
		let ins = parse line
		case ins of
			Left str ->do
							putStrLn str
							execute (db,sel,file)
			Right command ->do
							new <-run (db,sel,file) command
							execute new



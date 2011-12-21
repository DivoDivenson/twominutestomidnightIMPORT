--ghc --make ./project.hs 
import System.Environment
import Run
import Parse

--HAVE TO FIX THE NEWLINE CHARS IN INPUT FILE
--UNIX NEWLINES ONLY
main::IO()
main = do
	execute ([],[])
--original database and selected part
execute (db,sel) = do
		line <- getLine
		let ins = parse line
		case ins of
			Left str ->do
							putStrLn str
							execute (db,sel)
			Right command ->do
							new <-run (db,sel) command
							execute new



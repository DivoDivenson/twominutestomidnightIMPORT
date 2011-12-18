--ghc --make ./project.hs 
import System.Environment
import Run
import Parse

main::IO()
main = do
	execute []

execute x = do
		line <- getLine
		let ins = parse line
		case ins of
			Left str ->do
							putStrLn str
							execute x
			Right command ->do
							x2 <-run x command
							execute x2



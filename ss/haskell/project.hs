import System.Environment

main = dp
	args <- getArgs
	mapM_ (interactFile (unlines . map processIt . lines)) args

interactFile f filename = do
	s <- readFile filename
	putStr (f s)

processIt s = show (length s)

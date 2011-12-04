main = do
	s <- getContents
	let r = map process (lines s)
	putStr (unlines r)

process s = show (length s)

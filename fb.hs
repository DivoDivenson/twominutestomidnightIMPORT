main = do
   strings <- getLine --Laziness means only what's needed will be read in. 
   sent <- getLine
   let wrds = words strings
       len = length $ wrds!!0
   putStrLn $ show $ master 0 sent wrds len
   return ()


master :: Int -> [Char] -> [String] -> Int -> Int 
master i [] _ len= -1 -- -1 means not found 
master i full@(x:xs) actwrds len
    | (elem (take len full) actwrds) = do_next full actwrds len i
	| otherwise = master (i+1) xs actwrds len

do_next :: [Char] -> [String] -> Int -> Int -> Int 
do_next full@(x:xs) actwrds len i = case res of
		True -> i
		False -> master (i+1) xs actwrds len 
		where res = check (remove_no len full) (remove (take len full) actwrds) len

check :: String -> [String] -> Int -> Bool
check _ [] _= True 
check full wrds len 
	| (elem (wrd) wrds) = check (remove_no len full) nwrds len
	| otherwise = False
	where 	wrd = take len full 
		nwrds = remove wrd wrds

remove el [] = []
remove el full@(x:xs)
	| (el == x) = xs
	| otherwise = x:(remove el xs)

remove_no n  [] = []
remove_no 0  xs = xs 
remove_no n full@(x:xs) = remove_no (n-1) xs
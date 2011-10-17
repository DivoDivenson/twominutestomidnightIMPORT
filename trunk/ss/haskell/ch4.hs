import Data.Char

foldlSum :: [Int] -> Int
foldlSum xs = foldl step 0 xs
	where step acc x = acc + x
--meh, close enough
asInt_fold :: String -> Int
asInt_fold xs = foldl step 0 xs
	where step acc x = (acc * 10) + digitToInt x

myconcat :: [[a]] -> [a]
myconcat xs = foldr (++) [] xs

myConcat xss = foldr step [] xss
	where step xs ls = foldr (:) ls xs

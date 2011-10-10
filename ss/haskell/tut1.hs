lastof :: [a] -> a
lastof [x] = x
lastof (x:xs) = lastof xs

lastMine (x:xs) | xs == [] = x
            | otherwise = lastMine xs

isorderd :: [Integer] -> Bool
isorderd [] = True
isorderd [x] = True
isorderd (x:y:xs) | x < y = isorderd (y:xs)
                  | otherwise = False

seconds :: [a] -> [a]
seconds [x] = []
seconds [] = []
seconds (x:y:xs) = y : seconds xs


riffle :: [a] -> [a] -> [a]
riffle _ [] = []
riffle [] _ = []
riffle (x:xs) (y:ys) = x:y: riffle ys xs

{-
vowels = "aeiou"
unixname :: [Char] -> [Char]
unixname [] = []
unixname (c:cs) | vowel c = unixname cs
                | otherwise = [c] ++ unixname cs
                  where vowel x = x 'elem' vowels

-}








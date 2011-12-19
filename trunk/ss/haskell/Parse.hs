module Parse where
import System.IO


data Args = Filename String | Registrations | Column String
	deriving(Eq, Show)

data Command = Load | Save | Report | Distinct
	deriving(Eq, Show)

parse::String -> Either String (Command, [Args])
parse s = parse2(split_on s ' ')

parse2::[String]->Either String (Command,[Args])
parse2 ("load":x:[]) = Right (Load, [Filename x])
parse2 ("save":x:[]) = Right (Save, [Filename x])
parse2 ("report":"registrations":[]) = Right (Report, [Registrations])
parse2 ("dist":x:[]) = Right (Distinct, [Column x]) --SHORT HAND FTM
parse2 (_) = Left "Error"

--String to split, seperator, "tokens"
split_on::String-> Char -> [String]
split_on [] _ = [""]
split_on (x:xs) c 
	| (x == c) = "":rest
	| (x == '"') = rest2--([x]++head rest2):(tail rest2)
	| otherwise = ([x]++head rest): (tail rest)
	where 
		rest = split_on xs c
		rest2 = split_ignore xs c


split_ignore::String->Char->[String] --as above but ignores sep, only looks for quotes
split_ignore [] _ = [""]
split_ignore (x:xs) c
	| (x == '"') = rest2--([x]++head rest2):(tail rest2)
	| otherwise = ([x]++head rest):(tail rest)
	where 
		rest = split_ignore xs c
		rest2 = split_on xs c
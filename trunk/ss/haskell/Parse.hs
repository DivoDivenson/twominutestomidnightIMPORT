module Parse where
import System.IO


data Args = Filename String | Registrations
	deriving(Eq, Show)

data Command = Load | Save | Report
	deriving(Eq, Show)

parse::String -> Either String (Command, [Args])
parse s = parse2(split_on s ' ')

parse2::[String]->Either String (Command,[Args])
parse2 ("load":x:[]) = Right (Load, [Filename x])
parse2 ("save":x:[]) = Right (Save, [Filename x])
parse2 ("report":"registrations":[]) = Right (Report, [Registrations])


split_on::String-> Char -> [String]
split_on [] _ = [""]
split_on (x:xs) c 
	| (x == c) = "":rest
	| otherwise = ([x]++head rest): (tail rest)
	where rest = split_on xs c
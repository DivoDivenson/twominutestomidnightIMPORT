module Parse where
import System.IO

--Filename became a bit of a hold all
data Args = Filename String | Registrations | Completions |Column String | Conditions [String]
	deriving(Eq, Show)

data Command = Load | Save | Report | Distinct | Help | List | Count | Select
	deriving(Eq, Show) 

--Called in user input. Outputs a Command and it's arguments
parse::String -> Either String (Command, [Args])
parse s 
		| (arg_len == 0) = Left "Error: No Input"
		| (arg_len == 1) = comParse (head tokens) []
		| (arg_len >= 2) = comParse (head tokens) $ tail tokens
		where 
			tokens = split_on s ' '
			arg_len = length tokens

--Check if command is valid, then executes it
comParse::String -> [String] -> Either String (Command, [Args])
comParse "load" (x:[]) = Right(Load, [Filename x])
comParse "save" (x:[]) = Right(Save, [Filename x])
comParse "distinct" (x:[]) = Right (Distinct, [Column x])
comParse "help" (x:[]) = Right(Help, [Filename x])
comParse "list" xs = Right(List, [Conditions xs])
comParse "count" xs = Right(Count, [Conditions xs])
comParse "report" ("registrations":[]) = Right(Report, [Registrations])
comParse "select" xs = Right(Select, [Conditions xs])
--comParse "report" ("completions":[]) = Right(Report, [Completions]) --Needs a date parser

comParse _ _ = Left "Error: Invalid command or arguments"		 


--parse2 ("report":"registrations":[]) = Right (Report, [Registrations])
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
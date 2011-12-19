module Run where
import Parse
import System.IO

type Database = [Record]
type Record = [Field]
data Field = Value String | Blank
instance Show (Field) where
	show Blank = ""
	show (Value s) = s

run::Database -> (Command, [Args]) -> IO Database

run _ (Load, [Filename s]) = do
			file <- readFile s
			let db = map parseRecord $ lines file
			putStrLn $ show $ length db
			return db

run db (Save, [Filename s]) = do
				--writeFile s $ unparseDB db
				putStrLn $ unparseDB db
				return db

--run db (Report, [Registrations]) = do
run db (Distinct, [Column x]) = do
				putStrLn $ show $ findColumn x 0 $ head db -- Take first row as column names, seems reasonable
				--putStrLn $ x
				return db
				

findColumn::String -> Int -> Record -> Int
--findColumn ('$':x) 0 _= read x::Int
findColumn x i ((Value y):ys)
	| (x == y) = i
	| otherwise = findColumn x (i+1) ys
findColumn _ _ [] = -1



--findDistinct::Database -> Int -> String
--findDistinct [] _ = 0
--findDistinct x a = x!!a

parseRecord::String -> Record
parseRecord s = map parseField $ split_on s ','

parseField::String -> Field
parseField "" = Blank
parseField s = Value s

unparseDB::Database -> String
unparseDB db = unlines $ map unparseRecord db

unparseRecord::Record -> String
unparseRecord x = joinFields ',' $ map show x	

--Make sure to put the quotes back in
joinFields::Char -> [String]-> String
joinFields _ (x:[]) = x
joinFields seperator (x:xs) = if (hasAnyMine x ',')
							then ['"']++x++['"']++[seperator]++joinFields seperator xs 
							else x++[seperator]++joinFields seperator xs

hasAnyMine::String -> Char -> Bool --Cous I can't depend on the libraries apparently
hasAnyMine [] _ = False
hasAnyMine (x:xs) c 
	| (x == c) = True
	| otherwise = hasAnyMine xs c








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
				writeFile s $ unparseDB db
				return db

run db (Report, [Registrations]) = do
				
					
 	

parseRecord::String -> Record
parseRecord s = map parseField $ split_on s ','

parseField::String -> Field
parseField "" = Blank
parseField s = Value s

unparseDB::Database -> String
unparseDB db = unlines $ map unparseRecord db

unparseRecord::Record -> String
unparseRecord x = joinFields ',' $ map show x	

joinFields::Char -> [String]-> String
joinFields _ (x:[]) = x
joinFields seperator (x:xs) = x++[seperator]++joinFields seperator xs







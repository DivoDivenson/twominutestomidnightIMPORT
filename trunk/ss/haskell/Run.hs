module Run where
import Parse
import System.IO
import Data.List
import Data.Maybe
import Help

type Database = [Record]
type Record = [Field]
data Field = Value String | Blank
	deriving(Eq, Ord)
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
			let colNo =  findColumn x $ head db 
			if isJust colNo
				then putStrLn $ (show $ findDist (fromJust colNo) db )++" distinct values for "++x
				else putStrLn "Invalid Column"
			return db

run db (Help, [Filename x]) = do
			putStrLn $ help x
			return db
				

findColumn::String -> Record ->Maybe Int
findColumn ('$':x) _ = Just (read x::Int)
findColumn str heads = findIndex (==str) $ map show heads --Figure this out later

--Find distinct values in a column
findDist::Int -> Database  -> Int
findDist i db = length $ nub $ getCol i db []


--Return a column
getCol::Int -> Database -> Record ->Record
getCol _ [] col = col
getCol i (x:xs) acc = getCol i xs (acc++[x!!i])
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








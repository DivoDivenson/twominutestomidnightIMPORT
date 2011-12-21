module Run where
import Parse
import System.IO
import Data.List
import Data.Maybe
import Help

type Result = (Database, Database)
type Database = [Record]
type Record = [Field]
data Field = Value String | Blank
	deriving(Eq, Ord)
instance Show (Field) where
	show Blank = ""
	show (Value s) = s

run::Result -> (Command, [Args]) -> IO Result

run (_, _) (Load, [Filename s]) = do
			file <- readFile s
			let db = map parseRecord $ lines file
			putStrLn $ show $ length db
			return (db,db)


run (db,sel) (Save, [Filename s]) = do
				--writeFile s $ unparseDB db
			putStrLn $ unparseDB sel
			return (db,sel)

run (db,sel) (Report, [Registrations]) = do
			putStrLn $ show $ registrations sel
			return (db,sel)

run (db,sel) (List, [Conditions s]) = do
			putStrLn $ show  $ list sel s--list db s
			return (db,sel)

run (db,sel) (Count, [Conditions s]) = do
			putStrLn $ (show $ length $ list sel s)++" for "++(show s)
			return (db,sel)


run (db,sel) (Distinct, [Column x]) = do
			let colNo =  findColumn x $ head db 
			if isJust colNo
				then putStrLn $ (show $ findDist (fromJust colNo) sel )++" distinct values for "++x
				else putStrLn "Invalid Column"
			return (db,sel)

--Select always runs on the complete DB, having it run on smaller and smaller selections seems silly?
run (db,sel) (Select, [Conditions s]) = do
			let sel_new = selection db sel s
			return (db, sel_new)
			
			


run (db,sel) (Help, [Filename x]) = do
			putStrLn $ help x
			return (db,sel)

registrations::Database -> [String]
registrations db = club_register clubNames db
	where 
		clubNames = tail $ nub ( getCol (fromJust $ findColumn "Club" $ head db) db [])
		

--club names
club_register::Record -> Database -> [String]
club_register [] _ = []
club_register (x:xs) db = ([(show x)++" , "++(show $ length maps)])++club_register xs db
	where 
		mapCol = (fromJust $ findColumn "Map Name" $ head db)
		mapIdx = "$"++(show mapCol)
		club = select db $ [mapIdx++"="++(show x)]
		maps = getCol mapCol club []


				
list::Database -> [String] -> Database
list db args = select db args

selection::Database -> Database -> [String] -> Database
selection full _ ("all":[]) = full
selection full selected args = select full args --Don't do select on a selection

select::Database -> [String] -> Database
select _ [] = []
select db (x:xs) = (findRows db col (args!!1)) `myIntersect` (select db xs)
	where 
		args = selectionTokens [x]
		col = fromJust $ findColumn (args!!0) $ head db

--Same as intersect but ignores empty lists
myIntersect:: (Eq a) => [a] -> [a] -> [a]
myIntersect xs [] = xs
myIntersect [] ys = ys
myIntersect xs ys = intersect xs ys

findRows::Database -> Int -> String ->Database
findRows [] _ _  = []
findRows (x:xs) i cond = 
	if cond == show (x!!i) --do globbing here IMPORTANT
		then (findRows xs i cond)++[x]
		else findRows xs i cond

-- Turns [$3=abc, $4=xyz] into [$3,abc,$4,xyz]
selectionTokens::[String] -> [String]
selectionTokens [] = []
selectionTokens (x:xs) = (split_on x '=')++selectionTokens xs


findColumn::String -> Record ->Maybe Int
findColumn ('$':x) _ = Just ((read x::Int) - 1)
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








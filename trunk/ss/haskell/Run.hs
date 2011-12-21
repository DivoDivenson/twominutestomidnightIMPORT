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


--Row numbers added in, so the data starts at col 1, but the user can access col 0 if the want
run (_, _) (Load, [Filename s]) = do
			file <- readFile s
			let db = map parseRecord $ lines file
			let sel = indexDB db 0
			putStrLn $ show $ length db
			return (db,sel)


run (db,sel) (Save, [Filename s]) = do
			writeFile s $ unparseDB sel
			return (db,sel)

run (db,sel) (Report, [Registrations]) = do
			putStrLn $ show $ registrations sel
			return (db,sel)

run (db,sel) (List, [Conditions s]) = do
			let slct = list sel s
			putStrLn $ show  $ removeRowNums slct
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

run (db,sel) (Show, [Empty]) = do
			--Show headings
			--if (head db) == (head sel)
				--then putStrLn $ unlines $ showDB sel 
				--else 
					--do
						--putStrLn $ unparseRecord (head db)
						--putStrLn $ unlines $ showDB sel 
			putStrLn $ unlines $ showDB sel
			return (db,sel)

--Delect row from selected. db _always_ holds a complete copy
run (db, sel) (Delete, [Conditions s]) =do
			let row = (read (head s)::Int)
			let mapped_row = mapRowSel sel row
			if isJust mapped_row
				then
					do
						let row_sel = fromJust mapped_row
						let db_new = deleteRow db row
						let sel_new = deleteFromSel row_sel sel
						return (db_new, sel_new)
				else
					do
						putStrLn "Invalid row"
						return (db, sel)


run (db, sel) (Update, [Conditions s]) = do
			let row = (read (s!!0)::Int)
			let col = findColumn (s!!1) (head db)
			let mapped_row = mapRowSel sel row
			let value = (s!!2)
			if(isJust mapped_row) && (isJust col)
				then
					do
						let db_new = insertField db row ((fromJust col) -1) value
						let sel_new = insertField sel (fromJust mapped_row) (fromJust col) value
						return (db_new, sel_new)
				else
					do
						putStrLn "Invalid args"
						return (db, sel)

	
			


run (db,sel) (Help, [Filename x]) = do
			putStrLn $ help x
			return (db,sel)


insertField::Database -> Int -> Int -> String -> Database
insertField db row col value = top++[new]++bottom
	where 
		(top,xs) = splitAt row db
		bottom = tail xs
		(row_top,rest) = splitAt col (head xs)
		new = row_top++[Value value]++(tail rest)

--Decremnt col values greater than Int
deleteFromSel::Int -> Database -> Database
deleteFromSel _ [] = []
deleteFromSel i (x:xs)
	| num == i = deleteFromSel i xs
	| num > i = [[Value (show (num-1))]++(tail x)]++deleteFromSel i xs
	| otherwise = [x]++deleteFromSel i xs
	where num = (read (show (head x))::Int) 

deleteRow::Database -> Int -> Database
deleteRow db i = x++(tail xs)
	where (x,xs) = splitAt (i) db

--Map a row number to the index of said row based on the row num column
mapRowSel::Database -> Int ->Maybe Int
mapRowSel [] _ = Nothing
mapRowSel (x:xs) i
	| (read (show (head x))::Int) == i = Just i
	| otherwise = mapRowSel xs i


--BROKEN
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
selection full _ ("all":[]) = indexDB full 0
selection full selected args = [[Value "0"]++(head full)]++(select selected args)


select::Database -> [String] -> Database
select _ [] = []
select db (x:xs) = (findRows db col (args!!1)) `myIntersect` (select db xs)
	where 
		args = selectionTokens [x]
		col = fromJust $ findColumn (args!!0) $ head db

removeRowNums::Database -> Database
removeRowNums [] = []
removeRowNums (x:xs) = [(tail x)]++removeRowNums xs

--Same as intersect but ignores empty lists
myIntersect:: (Eq a) => [a] -> [a] -> [a]
myIntersect xs [] = xs
myIntersect [] ys = ys
myIntersect xs ys = intersect xs ys

findRows::Database -> Int -> String ->Database
findRows [] _ _  = []
findRows (x:xs) i cond = 
	if cond == show (x!!i) --do globbing here IMPORTANT, -1 to account for col numbers
		then [x]++(findRows xs i cond)
		else findRows xs i cond

-- Turns [$3=abc, $4=xyz] into [$3,abc,$4,xyz]
selectionTokens::[String] -> [String]
selectionTokens [] = []
selectionTokens (x:xs) = (split_on x '=')++selectionTokens xs


findColumn::String -> Record ->Maybe Int
findColumn ('$':x) _ = Just (read x::Int)
findColumn str heads = Just (1 + (fromJust $ findIndex (==str) $ map show heads )) --TEMP

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

indexDB::Database -> Int -> Database
indexDB [] _ = []
indexDB (x:xs) i = [(indexRecord x i)]++(indexDB xs (i+1))

indexRecord::Record -> Int -> Record
indexRecord rec i = [Value (show i)]++rec

parseRecord::String -> Record
parseRecord s = map parseField $ split_on s ','

parseField::String -> Field
parseField "" = Blank
parseField s = Value s

showDB::Database -> [String]
showDB []  = []
showDB (x:xs)  = [(showRecord  x)]++showDB xs 

unparseDB::Database -> String
unparseDB db = unlines $ map unparseRecord db

unparseRecord::Record -> String
unparseRecord x = joinFields ',' $ map show $ (tail x)

showRecord::Record ->String
showRecord x = joinFields '\t' $ map show x

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








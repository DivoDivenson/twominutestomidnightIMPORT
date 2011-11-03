-- CS3016/CS4012 Tutorial 4   Steven Diviney 08462267 

module Tut04 where

import Data.List

data BinTree k d = Nil | Branch (BinTree k d) k d (BinTree k d)

addEntry :: (Ord k) => k -> d -> BinTree k d -> BinTree k d
addEntry key datum Nil = Branch Nil key datum Nil
addEntry key datum (Branch ltree key' datum' rtree)
 | key < key'  =  Branch (addEntry key datum ltree) key' datum' rtree
 | key > key'  =  Branch ltree key' datum' (addEntry key datum rtree)
 | otherwise   =  Branch ltree key datum rtree

findEntry key Nil = Nothing
findEntry key (Branch ltree key' datum' rtree)
 | key < key'  =  findEntry key ltree
 | key > key'  =  findEntry key rtree
 | otherwise   =  Just datum'
 
tree2list Nil = []
tree2list (Branch ltree key datum rtree)
 = tree2list ltree ++ [(key,datum)] ++ tree2list rtree
 
list2tree :: (Ord k) => [(k,d)] -> BinTree k d
list2tree = foldr (uncurry addEntry) Nil

mergeTrees tree1 tree2 
 = list2tree (tree2list tree1 ++ tree2list tree2)

-- Q1  Write an instance of Eq for BinTree (the derived one)
--     (Leave this commented out in the final deliverable)
--data BinTree k d = Nil | Branch (BinTree k d) k d (BinTree K d) deriving ( Show )

--Or did you mean write it to have the same behaviour?
--instance (Eq k, Eq d) => Eq (BinTree k d) where
--	Nil == Nil = True
--	Branch ltree1 key1 data1 rtree1 == Branch ltree2 key2 data2 rtree2 = ltree1 == ltree2 && key1 == key2 && data1 == data2 && ltree1 == rtree2
--	_  == _   = False

-- Q2  Write an instance of Show for BinTree
instance (Show k, Show d) => Show (BinTree k d) where
	show Nil = ""
	show (Branch ltree key datum rtree) =  show ltree ++ show key ++ " |-> " ++ show datum ++ ", " ++ show rtree

-- Q3 Write an instance of Eq for BinTree that respects Show
instance (Eq k, Eq d) => Eq (BinTree k d) where
--	Branch ltree1 key1 data1 rtree1 == Branch ltree2 key2 data2 rtree2 = tree2list (Branch ltree1 key1 data1 rtree1) == tree2list (Branch ltree2 key2 data2 rtree2)
	a == b = tree2list a == tree2list b
-- Q4 Design a Ordered Collection Class (OrdColl): none fuse
class OrdColl a where
	none :: a
	fuse :: a -> a -> a

-- Q5 Instantiate List as OrdColl
instance (Ord a) => (OrdColl [a]) where
	none = []
	fuse [] b = b
	fuse b [] = b
	fuse (x:xs) (y:ys) | x < y = x : (fuse xs (y:ys))
			   | x >= y = y : (fuse (x:xs) ys)
-- Q6 Instantiate BinTRee as OrdColl
instance (Ord k) => OrdColl (BinTree k d) where
	none = Nil
	fuse x y = foldl step y (tree2list x) 
		where step acc (k, d) = addEntry k d acc   

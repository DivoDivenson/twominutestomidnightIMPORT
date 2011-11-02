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
--instance Eq BinTree where
	

-- Q2  Write an instance of Show for BinTree
 

-- Q3 Write an instance of Eq for BinTree that respects Show

  
-- Q4 Design a Ordered Collection Class (OrdColl): none fuse


-- Q5 Instantiate List as OrdColl
  
-- Q6 Instantiate BinTRee as OrdColl
  
  

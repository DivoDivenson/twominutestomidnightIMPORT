-- Steven Diviney 08462267 
module Ex01 where

type Ident = String

data Expression = Val Float
                | Add Expression Expression
                | Multiply Expression Expression
                | Subtract Expression Expression
                | Divide Expression Expression
                | Var Ident
                | Def Ident Expression Expression
     deriving Show

type Dictionary k d = [(k,d)]

find :: Eq k => Dictionary k d -> k -> Maybe d
find [] _ = Nothing
find ( (k,v) : ds ) name | name == k = Just v
                         | otherwise = find ds name

define :: Dictionary k d -> k -> d -> Dictionary k d
define d k v = (k,v):d

{- eval that dies if we divide by zero or use an undefined variable -}

--eval :: Dictionary Ident Float -> Expression -> Float
--eval _ (Val x) = x
--eval d (Add x y) = eval d x + eval d y
--eval d (Multiply x y) = eval d x * eval d y
--eval d (Subtract x y) = eval d x - eval d y
--eval d (Divide x y) = eval d x / eval d y
--eval d (Var i) = fromJust (find d i)
--eval d (Def i e1 e2) = eval d' e2
  --                    where d' = define d i (eval d e1) 
fromJust (Just a) = a

{- wanted: -}
eval :: Dictionary Ident Float -> Expression -> Maybe Float
eval _ (Val x) = Just x
eval d (Add x y) = addm(eval d x) (eval d y)
eval d (Subtract x y) = subm(eval d x) (eval d y)
eval d (Multiply x y) = mulm(eval d x) (eval d y)
eval d (Divide x y) = divm(eval d x) (eval d y)
eval d (Var i) = (find d i) --find does what we want on its own
eval d (Def i e1 e2) = if isJust(eval d e1)
    		       then eval d' e2
		       else Nothing
			where d' =  define d i fromJust(eval d e1)
				   

-- eval d e = Nothing -- need to make this do something useful.

addm (Just a) (Just b) = Just (a + b)
addm _ _ = Nothing

subm (Just a) (Just b) = Just (a + b)
subm _ _ = Nothing

mulm (Just a) (Just b) = Just (a + b)
mulm _ _ = Nothing

divm (Just a) (Just 0) =  Nothing 
divm (Just a) (Just b) = Just (a / b)
divm _ _ = Nothing






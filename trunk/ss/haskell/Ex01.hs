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
--                      where d' = define d i (eval d e1) 
fromJust (Just a) = a

{- wanted: -}
eval :: Dictionary Ident Float -> Expression -> Maybe Float
eval _ (Val x) = Just x
eval d (Add x y) = addm(eval d x) (eval d y)


-- eval d e = Nothing -- need to make this do something useful.

addm (Just a) (Just b) = Just (a + b)
addm _ _ = Nothing

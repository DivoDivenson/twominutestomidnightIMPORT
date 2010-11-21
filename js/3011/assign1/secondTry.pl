numera(0).
numeral(s(X)) :- numeral(X).
numeral(X + Y) :- numeral(X), numeral(Y).
numeral(p(X)) :- numeral(X).
numeral(-X) :- numeral(X).
numeral(X-Y) :- numeral(X), numeral(Y).

add(0,X,X).
add(s(X),Y,s(Z)) :- add(X,Y,Z).

add2(0,Y,Z) :- eval(Y,Z).
add2(s(W),Y,s(Z)) :- add2(W,Y,Z).
add2(U+V,Y,Z) :-
   add2(U,V,W),
   add2(W,Y,Z).
add2(U-V,Y,Z) :-
  subtract(U,V,W),
  add2(W,Y,Z).

add2(p(W),Y,Z):-add2(W,Y,s(Z)),!.
add2(p(W),Y,p(Z)):-add2(W,Y,Z),!.

add2(-X,Y,Z) :- minus(X,W), add2(W,Y,Z),!.
add2(X,-Y,Z) :- minus(Y,W), add2(X,W,Z),!.

eval(0,0).
eval(p(X),p(Z)) :- eval(X,Z),!.
eval(s(X),s(Z)) :- eval(X,Z),!.
eval(s(p(X)),Z) :- eval(X,Z),!.
eval(p(s(X)),Z) :- eval(X,Z),!.
eval(X+Y,Z) :- add2(X,Y,W), eval(W,Z),!.

minus(0,0).
minus(s(p(X)),Z) :- minus(X,Z),!.
minus(p(s(X)),Z) :- minus(X,Z),!.
minus(s(X),p(Z)) :- minus(X,Z),!.
minus(p(X),s(Z)) :- minus(X,Z),!.
minus(X-Y,Z) :- subtract(X,Y,W), minus(W,Z),!.

subtract(X,Y,Z) :- minus(Y,W), add2(X,W,Z),!.
subtract(X,-Y,Z) :- add2(X,Y,Z),!.
subtract(-X,Y,Z) :- add2(X,Y,Z),!.

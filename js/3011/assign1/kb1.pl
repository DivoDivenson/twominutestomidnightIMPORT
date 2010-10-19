numeral(0).
numeral(s(X)) :- numeral(X).
numeral(X + Y) :- numeral(X), numeral(Y).

add(0,X,X).
add(s(X), Y, s(Z)) :- add(X,Y,Z).



reduce(numeral(X)):- numeral(X).
reduce(numeral(X + Y), Z):- add(X,Y,Z).

add2(X,Y,Z):- reduce(X,Q), reduce(Y,R), add(Q,R,Z).

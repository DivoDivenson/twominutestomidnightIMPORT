increment(X,Y):- X is Y -1.

sum(X,Y,Z):- Z is Y + X.


addone([],[]).
addone([X|Y],[R|S]):- R is X +1, addone(Y,S).

accMin([H|T],A,Min):-
  H < A,
  accMin(T,H,Min).

accMin([H|T],A,Min):-
  H >= A,
  accMin(T,A,Min).

accMin([],A,A).

min(List,Min):-
  List = [H|_],
  accMin(List,H,Min).


scalerMult(_,[],[]).
scalerMult(A,[H|T],[X|Y]):-
  X is H * A,
  scalerMult(A,T,Y).

dot([],[],_).
dot([A|B],[C|D],X):-
  Y is X,
  X is A * C,
  Y is X + Y,
  dot(B,D,Y).


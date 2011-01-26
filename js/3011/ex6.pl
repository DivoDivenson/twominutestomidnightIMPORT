append([],L,L).
append([H|T],L2,[H|L3]):-append(T,L2,L3).
                          


double(X):-append(Y,Y,X).


accRev([H|T],A,R):- accRev(T,[H|A],R).
accRev([],A,A).

palindrome(X):- accRev(X,[],X).

second(X,[_|T]):- head(X,T).
head(X,[H|_]):- H = X.

swap12([],[]).
swap12([H|T],[A|B]):- head(H,B), head(A,T), pad(T,B).
pad([H|T],[A|B]):- equal(T,B).
equal([],[]).
equal([H|T],[A|B]) :- H = A, equal(T,B).



final(X,[H|T]):- H = X, T = [].
final(X,[H|T]):- final(X,T).

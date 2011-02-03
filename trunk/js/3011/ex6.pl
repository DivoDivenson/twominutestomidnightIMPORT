append([],L,L).
append([H|T],L2,[H|L3]):-append(T,L2,L3).
 
                         
member(X,[X|_]).
member(X,[_|T]):-member(X,T).                         


double(X):-append(Y,Y,X).


flatten([],[]).
flatten([H|T],[H|T2]) :- flatten(T,T2).

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

set([],_).                      
set([X|Y],A) :- member(A,X), set(Y,A). %Does not work, problem lies with append but it's fairly close
%set([X|Y],A) :- append([X],[A],B), set(Y,B).
set([X|Y],[X|A]) :- set(Y,A).

final(X,[H|T]):- H = X, T = [].
final(X,[H|T]):- final(X,T).

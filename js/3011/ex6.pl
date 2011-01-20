append([],L,L).
append([H|T],L2,[H|L3]):-append(T,L2,L3).

double(X):-append(Y,Y,X).


accRev([H|T],A,R):- accRev(T,[H|A],R).
accRev([],A,A).

palindrome(X):- accRev(X,[],X).

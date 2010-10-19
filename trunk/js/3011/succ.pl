numeral(0).
numeral(succ(X)) :- numeral(X).
greater(succ(X), 0);
greater(succ(X), succ(Y)) :- greater(X, Y).


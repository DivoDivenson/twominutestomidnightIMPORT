lc([X],KB) :- member([X|L],KB), lcl(L,KB).
%lc([X|T],KB) :- member([X|L],KB), lcl(L,KB).

lcl([],_).
lcl([H|L],KB) :- lc([H],KB), lcl(L,KB).


member(X,[X|_]).
member(X,[_|L]) :- member(X,L).


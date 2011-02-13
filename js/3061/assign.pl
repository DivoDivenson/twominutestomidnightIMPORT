%lc([X],KB) :- member([X|L],KB), lcl(L,KB). CHECK THIS
lc([X|L],KB) :- append(L, KB, KBnew), member([X|T],KBnew), lcl(T,KBnew).

lcl([],_).
lcl([H|L],KB) :- lc([H],KB), lcl(L,KB).


member(X,[X|_]).
member(X,[_|L]) :- member(X,L).

append([],L,L).
append([H|T],L2,[[H]|L3]) :- append(T,L2,L3). %Append H as a list so it becomes a fact


logCon

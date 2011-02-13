%lc([X],KB) :- member([X|L],KB), lcl(L,KB). CHECK THIS
lc([X|L],KB) :- append(L, KB, KBnew), member([X|T],KBnew), lcl(T,KBnew).

lcl([],_).
lcl([H|L],KB) :- lc([H],KB), lcl(L,KB).


member(X,[X|_]).
member(X,[_|L]) :- member(X,L).

append([],L,L).
append([H|T],L2,[[H]|L3]) :- append(T,L2,L3). %Append H as a list so it becomes a fact


bottomUp(Goal,KB):-logCon(KB,C), memberOrig(Goal,C).


logCon(KB,C) :- addAll(KB,[],KBNew,C).

addAll(KB,C,KB,C):- empty(KB,C),!.
addAll(KB,C,KB1,C1):-add(KB,C,KB2,C2), addAll(KB2,C2,KB1,C1).

empty([],_).

memberOrig(X,[X|_]).
memberOrig(X,[_|T]):-memberOrig(X,T).

add([A|KB],C,KBnew,Cnew):-canBeAdded(A,C),!,A=[B|_]  ,KBnew=KB,Cnew=[[B]|C].
                          
add([A|KB],C,KBnew,Cnew):-add(KB,C,KBtemp,Cnew), KBnew=[A|KBtemp].

canBeAdded([_],_).
canBeAdded([_|T],C):-allMembers(T,C).


allMembers([],_).
allMembers([H|T],X):-member1([H],X),allMembers(T,X).


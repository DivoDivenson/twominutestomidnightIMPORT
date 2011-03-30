
normalizeRight(left(L),L) : - !.
normalizeRight(L,right(L)).

% Q4

normalizeLeft( right(L), L) :- !.
normalizeLeft( L, left(L)).



holdPrim( intState(q0), init ).
holdPrim( headPos(0), init ) .

poss( moveRight, Situation ):-
	mr(Q,X,Qnew),
	holdPrim( intState(Q), Situation),
	holds( derAt(X,Loc), Situation ),
	holdPrim( headPos( Loc ), Situation ).
	
delL( moveRight, Situation, intState(Q)) :-
	holdPrim( intState(Q), Situation ). 

delL(moveRight, Situation, headPos(Loc)) :-
	holdPrim((Los), Situation).

addL(moveRight, Situation, intState(Q)) :-
	mr(Q0, X, Q),
	holdPrim( intState(Q0), Situation),
	holds(derAt(X,Loc), Situation),
	holdPrim(headPos(Loc), Situation).

addL(moveRight, Situation, headPos(Loc)) :-
	holdPrim(headPos(L0), Situation),
	normalizeRight(L0, Loc).

holdPrim(PrimAtom, do(Action, Situation)):-
	poss(Action, Situation),
	holdPrim(PrimAtom, Situation),
	not(delL(Action, Situation, PrimAtom)).

holdPrim(PrimAtom, do(Action, Situation)):-
	poss(Action, Situation),
	addL(Action, Situation, PrimAtom).

holds(derAt(X, Loc), Situation) :- holdPrim(at(X, Loc), Situation), !.

holds(derAt(b-k,Loc), Situation).

%not(P) :- P, !, fail ; true. % Is this built in?

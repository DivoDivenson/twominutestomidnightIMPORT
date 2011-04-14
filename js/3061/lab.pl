
normalizeRight(left(L),L) : - !.
normalizeRight(L,right(L)).

% Q4

normalizeLeft( right(L), L) :- !.
normalizeLeft( L, left(L)).



holdPrim( intState(q0), init ).
holdPrim( headPos(0), init ) . %Not sure what these two do, something about relativizing dynamic predicates to a state. I have no idea what I just said

poss( moveRight, Situation ):-
	mr(Q,X,Qnew),
	holdPrim( intState(Q), Situation),
	holds( derAt(X,Loc), Situation ),
	holdPrim( headPos( Loc ), Situation ).
	
delL( moveRight, Situation, intState(Q)) :-
	holdPrim( intState(Q), Situation ). 

delL(moveRight, Situation, headPos(Loc)) :-
	holdPrim(headPos(Loc), Situation).

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

poss( moveLeft, Situation):-
	ml(Q, X, Qnew),
	holdPrim(intState(Q), Situation),
	holds(derAt(X, Loc), Situation),
	holdPrim(headPos(Loc), Situation).

delL(moveLeft, Situation, intState(Q)):-
	holdPrim( intState(Q), Situation).

delL(moveLeft, Situation, headPos(Q)):-
	holdPrim( headPos(Loc), Situation).

addL(moveLeft, Situation, intState(Q)) :-
	ml(Q0, X, Q),
	holdPrim( intState(Q0), Situation),
	holds(derAt(X,Loc), Situation),
	holdPrim(headPos(Loc), Situation).

addL(moveLeft, Situation, headPos(Loc)) :-
	holdPrim(headPos(L0), Situation),
	normalizeLeft(L0, Loc).

poss(write, Situation):-
	wl(Q, X, Y, Qnew),
	holdPrim(intState(Q), Situation),
	holds(derAt(X, Loc), Situation),
	holdPrim(headPos(Loc), Situation).

delL(write, Situation, intState(Q)):-
	holdPrim( intState(Q), Situation).

delL(write, Situation, headPos(Q)):-
	holdPrim( headPos(Loc), Situation).

addL(write, Situation, intState(Q)):-
	wl(Q0, X, Y, Q),
	holdPrim( intState(Q0), Situation),
	holds(derAt(Y,Loc), Situation).

%addL(write, Situation, headPos(Loc)):-  %Dont think we need this
%	holdPrim(headPos(Loc), Situation).


holdPrim(at(0,0), init).
holdPrim(at(1, right(0)), init).

mr(q0, 0, q1).

ml(q1, 1, q2).

wl(q2, 0, 1, q2).

%not(P) :- P, !, fail ; true. % Is this built in?

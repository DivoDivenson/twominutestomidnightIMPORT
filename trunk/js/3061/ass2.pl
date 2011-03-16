arc(N, M, Seed, PCost, Target, Cost) :- M is N * Seed, h(N, Hval, Target),  Cost is 1 + PCost + Hval.
arc(N, M, Seed, PCost, Target, Cost) :- M is N * Seed + 1, h(N, Hval, Target),  Cost is 2 + PCost + Hval.

is_goal(N, Target ) :- 0 is N mod Target.


h(N, Hvalue, Target) :- is_goal(N, Target), !, Hvalue is 0
			;
			Hvalue is 1/N.


search([[Node, Cost] | FRest],_,Target, [Node, Cost]) :- is_goal(Node, Target).
search([[Node, Cost] | FRest],Seed, Target, Found ) :- setof([N, C], arc(Node, N, Seed, Cost,Target, C), FNode), 
				add-to-frontier(FNode, FRest, FNew), 
				search(FNew, Seed, Target, Found).



%less-than( [Node1, Cost1], [Node2, Cost2], Target) :- 
%	h(Node1, Hvalue1, Target) , h(Node2, Hvalue2, Target), 
%	F1 is Cost1 + Hvalue1, F2 is Cost2 + Hvalue2, F1 <= F2.


a-star(Start, Seed, Target, Found) :- setof([N, C], arc(Start, N, Seed, 0 ,Target, C), FNode), 
					add-to-frontier(FNode, [], FNew),
					search(FNew,Seed, Target, Found).



add-to-frontier([], F, F).
add-to-frontier([H | T], FRest, FNew) :- insert_sort([H | FRest], FSorted), 
						add-to-frontier(T, FSorted, FNew).



%VERKS
insert_sort(List,Sorted):-i_sort(List,[],Sorted).
i_sort([],Acc,Acc).
i_sort([H|T],Acc,Sorted):-insert(H,Acc,NAcc),i_sort(T,NAcc,Sorted).
   
insert([N,X],[[NY,Y]|T],[[NY,Y]|NT]):-X>Y,insert([N,X],T,NT).
insert([N,X],[[NY, Y]|T],[[N,X],[NY, Y]|T]):-X=<Y.
insert(X,[],[X]).



s --> [].
s --> [0,1], s.
s --> [1,0], s.
s --> [0],s,[1].
s --> [1],s,[0].


% Second exercise


f --> a(X),a(X).
a([0|X])--> [0],a(X).
a([1|X])--> [1],a(X).
a([2|X])--> [2],a(X).
a([]) --> [].

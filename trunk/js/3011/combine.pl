combine1([],[],[]).
combine1([X|Y],Z,[X|W]) :- combine1(Z,Y,W).


combine2([],[],[[]]).
combine2([X|Y],[Z|A],[[X,Z]|W]):- combine2(Y,A,W).

combine3([],[],join()).
combine3([X|Y],[Z|A],[join(X,Z)|W]):- combine3(Y,A,W).


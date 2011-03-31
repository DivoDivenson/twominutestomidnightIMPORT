arc(a,a).
arc(a,b).
arc(b,c).
arc(c,d).

connected(A,B):- arc(A,B).
connected(A,B):- arc(A,X),A \= X, connected(X,B).

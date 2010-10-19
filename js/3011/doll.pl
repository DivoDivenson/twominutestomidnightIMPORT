directlyIn(olga,katarina).
directlyIn(natsha,olga).
directlyIn(irina,natsha).

in(X, Y):- directlyIn(X, Y).
rn(X, Y):- in(X, Z), directlyIn(Y, Z).

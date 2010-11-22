twice([],[]).
twice([H|T],[H,H|ET]):- twice(T,ET).

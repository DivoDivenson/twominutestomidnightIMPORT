/*s --> a,b,b.
s --> a,s,b,b.
a --> [a].
b --> [b].

%aeven

p -->[].
p --> p, q,q.
q --> [a].


s -->[].
s --> x.
s --> a,s,d.
x --> [].
x --> b,b,x,c,c.

a --> [a].
b --> [b].
c --> [c].
d --> [d].

*/

prop --> [p].
prop --> [q].
prop --> [r].
prop --> not,prop.
prop --> lparen, prop, and, prop,rparen.
prop --> lparen, prop, or, prop,rparen.
prop --> lparen, prop, implies, prop,rparen.
not --> [not].
lparen --> ['('].
rparen --> [')'].
and --> [and].
or --> [or].
implies --> [implies].

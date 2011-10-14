#!/usr/bin/python

import os
'''
Everything printed out on stdout is returned in the reply to the GET reuest,
so first set the header to indicate that we are sending a HTML page
'''
print "Content-type: text/html\n\n";

'''
Get the os environment dictionary and sort it in ascedning alphanumeric order of the names
'''
data = sorted(os.environ);

'''
Iterate through the environment dictionary and display each entries key 
and value pair in a table row
'''
print "<html><body>" #Valid html
print "<table>"
for i in data:
  print "<tr><td>" + i + "</td><td>" + os.environ[i] + "</td></tr>";

print "</table>";
print "</body></html>"

'''
The second part of the question, adding a query string.
The query string is added into the environment dictionary with the 
key 'QUERY_STRING'. Form-urlencoding is preserved. For example "%3D" 
(the value for "=") is stored as "%3D". This is so the literal character =
can still be used to identify values in parsing, even if they contain a 
form-urlencoded = sign
'''

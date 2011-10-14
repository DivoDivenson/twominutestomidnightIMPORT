#!/usr/bin/python

import os
'''
Everything printed out on stdout is returned in the reply to the GET reuest,
so first set the header to indicate that we are sending a HTML page
'''
print "Content-type: text/html\n\n";

'''
Get the request method (GET, PUT etc) and the query string, or the values in the
url after the ?
'''
method = os.environ["REQUEST_METHOD"];

'''
Only want execute if responding to a GET request
Then parse the query string for the two paramaters indexed by "first" and
"second
Finaly add the two paramaters and print the result to stdout so it is sent back
to the requester.
Use & to seperate the two params as per standerd url encoding.
If the url is not as expected, ie "first=x&second=y" the program fails
'''
if method == "GET":
  q_string = os.environ["QUERY_STRING"];
  q_string = (q_string.split("first=")[1]);#.split("second=");
  q_string = q_string.split("&second=");
  one = q_string[0];
  two = q_string[1];
  print  int(one) + int(two);


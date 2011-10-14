#!/usr/bin/env python

import os
import sys
'''
Everything printed out on stdout is returned in the reply to the GET reuest,
so first set the header to indicate that we are sending a HTML page
'''
print "Content-type: text/html\n\n"

'''
Check if the request is a GET or a POST
If it's a GET return a basic form that allows the user to submit two values to be
added. The values are passed throught using a standard POST to the same cgi script
'''
method = os.environ["REQUEST_METHOD"];
if method == "GET":
  print '<form name="input" action="script3.cgi" method="POST">'
  print 'First: <input type="text" name="first"/><br />'
  print 'Second: <input type="text" name="second"/>'
  print '<input type="submit" value="submit"/></form>'
'''
Else if the request is a post (the values from above), process the request string,
add the two values (assumed integer for this case, but extenstion to a float etc would be
trivial) and return the result to the user
'''
elif method == "POST":
#  print os.environ["CONTENT_LENGTH"]
  data = sys.stdin.readline()
  data = (data.split("first=")[1]);#.split("second=");
  data = data.split("&second=");
  one = data[0];
  two = data[1];
  print int(one) + int(two)
 



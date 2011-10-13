#!/usr/bin/env python

import os
import sys
print "Content-type: text/html\n\n"

method = os.environ["REQUEST_METHOD"];
if method == "GET":
  print '<form name="input" action="script3.cgi" method="POST">'
  print 'First: <input type="text" name="first"/><br />'
  print 'Second: <input type="text" name="second"/>'
  print '<input type="submit" value="submit"/></form>'
elif method == "POST":
#  print os.environ["CONTENT_LENGTH"]
  data = sys.stdin.readline()
  data = (data.split("first=")[1]);#.split("second=");
  data = data.split("&second=");
  one = data[0];
  two = data[1];
  print int(one) + int(two)
 



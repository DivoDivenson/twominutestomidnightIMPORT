#!/usr/bin/python

import os

print "Content-type: text/html\n\n";

method = os.environ["REQUEST_METHOD"];
q_string = os.environ["QUERY_STRING"];

#No error checking...
if method == "GET":
  q_string = (q_string.split("first=")[1]);#.split("second=");
  q_string = q_string.split("&second=");
  one = q_string[0];
  two = q_string[1];
  print  int(one) + int(two);


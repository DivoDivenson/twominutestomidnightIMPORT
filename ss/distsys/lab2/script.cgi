#!/usr/bin/python

import os
print "Content-type: text/html\n\n";

data = sorted(os.environ);

print "<table>"
for i in data:
  print "<tr><td>" + i + "</td><td>" + os.environ[i] + "</td></tr>";

print "</table>";

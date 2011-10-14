#!/usr/bin/env python

import os
import cgi 
import cgitb; cgitb.enable() #for debug
'''
Everything printed out on stdout is returned in the reply to the GET reuest,
so first set the header to indicate that we are sending a HTML page
'''
print "Content-type: text/html\n\n"
'''
The CGI lib does not distinguish between a GET and a POST. This is deliberate so the
programmer does not have to worry about it. As the task is to re-write question 3 we need
to distuinguish between them. Use os.environ to check if a get or a post.
If a post use the CGI library to extract the values, then add and return them.
'''
form = cgi.FieldStorage()
if os.environ['REQUEST_METHOD'] == "POST":
  one = form.getvalue("first", "")
  two = form.getvalue("second", "")
  if (one != "") and (two != ""):
    print int(one) + int(two)
'''
If a get send the user a HTML form as in Q3 to pass values in for computation
'''
elif os.environ['REQUEST_METHOD'] == "GET":
  print """
  <form method="post" action="script4.cgi">
  <input type=hidden name="key" value="process">
  First: <input type="text" name="first" /><br/>
  Second: <input type="text" name="second" />
  <input type="submit" value="submit"/>
  </form>
  """




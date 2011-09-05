#!/usr/bin/python

import urllib
from HTMLParser import HTMLParser
token = ''
class MyParser(HTMLParser):

	def handle_startendtag(self, tag, attrs):
		if(tag == 'input'):
			if(attrs[0][1] == 'hidden'):
				print attrs
				global token
				token = attrs[3][1]


def getData():

	f = urllib.urlopen("https://p6.secure.hostingprod.com/@www.alienthink.com/ssl/myvendor/mdownload/customerlogin.php")
	s = f.read()
	f.close()
	fd = file("page.html", 'w')
	fd.write(s)
	fd.close()
	return s

def getStaticData():
	f = file("page.html")
	s = ''
	for line in f:
		s += line
	f.close()
	return s

def postLogin(passwd, token):
	data = urllib.urlencode({"token" : token, "username" : "divodivenson@gmail.com", "passwd" : passwd})
	f = urllib.urlopen("https://p6.secure.hostingprod.com/@www.alienthink.com/ssl/myvendor/mdownload/customerlogin.php", data)
	s =  f.read()
	print f.info().headers
	f.close()
	fd = file("links.html", 'w')
	fd.write(s)
	fd.close()
	return s


text = getData() 
prsr = MyParser()
prsr.feed(text)
print token
INSERT PASSWORD
temp = postLogin('', token)
#print temp

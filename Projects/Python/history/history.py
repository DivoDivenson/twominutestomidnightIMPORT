import urllib2
import os.path
import re

def get_page(url):
	m = re.search("[\w]*$", url)
	filename =  m.group(0)  #Hurray for lazy assumptions
	if(os.path.exists(filename)):
		print "File exists"
		f = open(filename, 'r')
		text = f.read()
		f.close
	else:
		print "Getting file %s" % url
		opener = urllib2.build_opener() #Bullshit, but SO to the rescue once again
		opener.addheaders = [('User-agent', 'Mozilla/5.0')]
		page = opener.open(url)
		text = page.read()
		f = open(filename, 'w')
		f.write(text)
		f.close

	return text
		
def clean(text,out):
	p = re.compile('<[^<]+?>')
	text = p.sub('',text)
	f = open(out, 'w')
	f.write(text)
	f.close
	#Now just clean it up in VIM

if __name__ == '__main__':
	from sys import argv
	if(argv[1]):
		text = get_page(argv[1])
		#print text
		clean(text, argv[2])
	else:
		print "Needs a url"	
	


import curses
from os import system
import os
stdscr = curses.initscr()
#Not gonna sanatize user input


def gen_key(user):
	keys = "keys/%s" %(user)
	if not os.path.exists(keys): #Don't regerate the keys
		os.makedirs(keys)
		os.popen("openssl genrsa -out %s/private_key.pem 1024" %(keys))
		os.popen("openssl rsa -in %s/private_key.pem -out %s/public_key.pem -outform PEM -pubout" %(keys, keys))
	
#Sign with users private key
def sign(body, user)
	keys = "keys/%s" %(user)
	private_key = keys + '/' + "private_key.pem"
	os.popen(


def get_input(prompt):
	stdscr.clear()
	stdscr.border(0)
	stdscr.addstr(2, 2, prompt)
	stdscr.refresh()
	#3rd arg is the length of the string
	input = stdscr.getstr(3,3, 60)
	return input


def init_curses():

#	stdscr = curses.initscr()
	stdscr.border(0)
	stdscr.addstr(0, 25, "Email client")
	stdscr.addstr(2,2, "Menu:")
	stdscr.addstr(4,4, "1 - Send mail")
#More to come
	stdscr.refresh()

	key = stdscr.getch()
	if key == ord('1'):
		input_type = get_input("Input from file? Y/N")
		if(input_type == 'Y'):
			path = get_input("File location:")
			print path
			f = open(path)
			to = f.readline()
			from_ = f.readline()
			subject = f.readline()
			body = ""
			for line in f:
				body += line
	else:
		#to = get_input("To:")
		to = "assign2.test@gmail.com"
		#from_ = get_input("From:")
		from = "assign2.test@gmail.com"

	gen_key(to)
	stdscr.clear()

def main():
	init_curses()	

if __name__ == '__main__':
	main()

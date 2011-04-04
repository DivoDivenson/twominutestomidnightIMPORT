#! /usr/bin/python
import socket
import curses
from os import system
import os
import smtplib
import email.utils
from email.mime.text import MIMEText
stdscr = curses.initscr()
#Not gonna sanatize user input


def gen_key(user):
	keys = "./keys/%s" %(user)
	if not os.path.exists(keys): #Don't regerate the keys
		os.makedirs(keys)
		os.popen("openssl genrsa -out %s/private_key.pem 1024" %(keys))
		os.popen("openssl rsa -in %s/private_key.pem -out %s/public_key.pem -outform PEM -pubout" %(keys, keys))
	
#Sign digest with users private key. Did not know we had to do this
def sign(body, user):
	keys = "./keys/%s" %(user)
	private_key = keys + '/' + "private_key.pem"
	print private_key
	os.popen("openssl dgst -sha1 -sign %s -out %s %s" %(private_key, "/tmp/digest","/tmp/body"))


def encrypt(text, user, password):
	keys = "./keys/%s" % user
	private_key = keys + '/' + "private_key.pem"
	pipe = os.popen("openssl enc -aes-256-cbc -salt -in /tmp/body -pass pass:%s > encrypted" %password)


def get_input(prompt):
	stdscr.clear()
	stdscr.border(0)
	stdscr.addstr(2, 2, prompt)
	stdscr.refresh()
	#3rd arg is the length of the string
	input = stdscr.getstr(3,3, 60)
	return input

def get_bool_input(prompt):
	stdscr.clear()
	stdscr.border(0)
	stdscr.addstr(2,2,prompt)
	input = stdscr.getch()
	return input

def init_curses():

#	stdscr = curses.initscr()
	stdscr.border(0)
	stdscr.addstr(0, 25, "Email client")
	stdscr.addstr(2,2, "Menu:")
	stdscr.addstr(4,4, "1 - Send mail")
	stdscr.addstr(5,4, "2 - Get mail")
#More to come
	stdscr.refresh()

	key = stdscr.getch()
	if key == ord('1'):
		input_type = get_bool_input("Input from file? Y/N")
		if (input_type == 'Y'):
			path = get_input("File location:")
			print path
			f = open(path)
			to = f.readline()
			from_ = f.readline()
			subject = f.readline()
			body = ""
			for line in f:
				body += line
				print line
		else:
			#to = get_input("To:")
			to = "assign2.test@gmail.com"
			#from_ = get_input("From:")
			from_ = "assign2.test@gmail.com"
			#And subject / body to do
			#subject = get_input("Subject:")
			subject = "Test mail"
			#body = get_input("Body: ")
			body = "This is a test\nI do hope it works"

		#Need to write it out in order to sign it. I guess this is where using a wrapper library would have been better than just throwing the lifting 
		#out to the terminal
		f = open("/tmp/body", 'w')
		f.write(body)
		f.close
		#Make the keys
		gen_key(from_)
		#Get sha1 repr of body of the mail
		sign(body, from_)
		passphrase = get_input("Enter passphrase")
		encrypt(body, from_, passphrase)
		f = open("./encrypted") #If something goes wrong it's probably due to writing the thing out to disk
		encrypted_body = ""
		for line in f:
			encrypted_body += line
		send_mail(encrypted_body, subject, to, from_)	
	#Get mail
	elif  key == ord('2'):	
		user = get_input("Please enter user name:")
		s = smtplib.SMTP('127.0.0.1', 1026)
		#s.helo()
		s.send(user)
		#(family, _, _, _, _) =socket.getaddrinfo('127.0.0.1', 1026)[0]
		#s = socket.socket(family)
		#s.send("hello")
		s.close()
	stdscr.clear()
def send_mail(body, subject, to, from_):
	msg = MIMEText(body)
	msg['Subject'] = subject
	msg['From'] = from_
	msg['To'] = to
	
	s = smtplib.SMTP('127.0.0.1', 1026)
	s.sendmail(from_, to, msg.as_string())
	s.quit()

def main():
	init_curses()	

if __name__ == '__main__':
	main()

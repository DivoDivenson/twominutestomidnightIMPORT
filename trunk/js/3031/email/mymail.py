#! /usr/bin/python
import socket
import curses
from os import system
import os
import smtplib
import email.utils
from email.mime.text import MIMEText
from email import encoders
from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email.MIMEText import MIMEText
from email.MIMEAudio import MIMEAudio
from email.MIMEImage import MIMEImage
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

def get_pub_key(user):
	user_cert = "./cert/%s" %user
	print user_cert
	pipe = os.popen("openssl verify -CAfile ./cert/demoCA/cacert.pem %s/newcert.pem" %user_cert)
	result = pipe.read()
	if result.find("OK") != -1:
		os.popen("openssl x509 -inform pem -in %s/newcert.pem -pubkey -noout > %s" %(user_cert, "/tmp/user_public.pem"))
		return "/tmp/user_public.pem"
	else:
		print "Did not verify"
		return None

def encrypt_password(passphrase, user):
	pub_key = get_pub_key(user)
	#Encrypt the passphrase with the public key
	pipe = os.popen("echo %s | openssl rsautl -encrypt -pubin -inkey %s -out /tmp/encrypted_pass" %(passphrase, pub_key))

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
		#
		#Not nice but it works
		encrypted_password = encrypt_password(passphrase, to)
		
		send_mail(encrypted_body, subject, to, from_)	
		
	
	
	#Get mail
	elif  key == ord('2'):	
		#user = get_input("Please enter user name:")
		user = "assign2.test@gmail.com"
		(family, _, _, _, sockaddr) = socket.getaddrinfo('127.0.0.1', 8080)[0]
		s = socket.socket(family)
		s.connect(sockaddr)
		s.send(user)
		s.close()


	stdscr.clear()
def send_mail(body, subject, to, from_):
	print "Sending"
	msg = MIMEMultipart()
	#msg = MIMEText(body)
	msg.attach(MIMEText(body, 'plain'))
	
	msg["Subject"] = subject
	msg["From"] = from_
	msg["To"] = to
 	file_pass = MIMEBase('application', "octet-stream")
	file_pass.set_payload(open("/tmp/encrypted_pass", "rb").read())
	#Set attachment name
	file_pass.add_header("Content-Disposition", "attachment; filename = passphrase")
	msg.attach(file_pass)
	
	file_digest = MIMEBase('application', "octet-stream")
	file_digest.set_payload(open("/tmp/digest", "rb").read())
	file_digest.add_header("Content-Disposition", "attachment; filename = digest")
	msg.attach(file_digest)
	
	s = smtplib.SMTP('127.0.0.1', 1026)
	s.sendmail(from_, to, msg.as_string())
	s.quit()

def main():
	init_curses()	

if __name__ == '__main__':
	main()

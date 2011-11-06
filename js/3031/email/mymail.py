#! /usr/bin/python
import socket
import curses
from os import system
import os
import smtplib
import email.utils
from email.mime.text import MIMEText
from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email.MIMEText import MIMEText
from email.MIMEAudio import MIMEAudio
from email.MIMEImage import MIMEImage
from email import encoders
stdscr = curses.initscr()
#Not gonna sanatize user input

#Check if key pair for user exists, if not then make one
def gen_key(user):
	keys = "./keys/%s" %(user)
	if not os.path.exists(keys): #Don't regerate the keys
		os.makedirs(keys)
		os.popen("openssl genrsa -out %s/private_key.pem 1024" %(keys))
		os.popen("openssl rsa -in %s/private_key.pem -out %s/public_key.pem -outform PEM -pubout" %(keys, keys))
	
#Sign digest of message body with users private key.
def sign(body, user):
	keys = "./keys/%s" %(user)
	private_key = keys + '/' + "private_key.pem"
	print private_key
	os.popen("openssl dgst -sha1 -sign %s -out %s %s" %(private_key, "/tmp/digest","/tmp/body"))


#Encrypt message text with senders (user) private key
def encrypt(text, user, password):
	keys = "./keys/%s" % user
	private_key = keys + '/' + "private_key.pem"
	pipe = os.popen("openssl enc -aes-256-cbc -salt -in /tmp/body -pass pass:%s > encrypted" %password)

#Get input from user. Wasting time doing this sure was a good idea
def get_input(prompt):
	stdscr.clear()
	stdscr.border(0)
	stdscr.addstr(2, 2, prompt)
	stdscr.refresh()
	#3rd arg is the length of the string
	input = stdscr.getstr(3,3, 60)
	return input

#Get a single charater from user. Another well spent few minutes
def get_bool_input(prompt):
	stdscr.clear()
	stdscr.border(0)
	stdscr.addstr(2,2,prompt)
	input = stdscr.getch()
	return input

#Verify the users cert against the CA cert
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


#Encrypt the passphrase the message is encrpyted with so it can be sent along with the message
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

#All this code is horrible and really does nothing
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
			to = get_input("To:")
			to = "assign2.test@gmail.com"
			#from_ = get_input("From:")
			from_ = "assign2.test@gmail.com"
			#And subject / body to do
			#subject = get_input("Subject:")
			subject = "Test mail"
			#body = get_input("Body: ")
			body = "This is a test\nI do hope it works"

		#Need to write it out in order to sign it. I guess this is where using a wrapper library would have been better than just throwing the lifting 
		#out to the terminal. Needs to be done so our openssl commands can get to it. Could use echo....
		f = open("/tmp/body", 'w')
		f.write(body)
		f.close
		#Make the keys
		gen_key(from_)
		#Get sha1 checksum of body of the mail
		sign(body, from_)
		passphrase = get_input("Enter passphrase")
		encrypt(body, from_, passphrase)
		f = open("./encrypted") #If something goes wrong it's probably due to writing the thing out to disk
		#Not really sure why the above line is there but lets not take any chances
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
		data = s.recv(8196) #Limiting to really small messages, fix this
		msg = email.message_from_string(data)
		#Pull the message apart and decrypt it
		parse_recv(msg, user)
		
		
		s.close()


	stdscr.clear()
#Take in email and decrypt it
def parse_recv(msg, user):
	first = False
	for part in msg.walk():
		#And this is where our story ends. message_from_string(data) gives no complaints but there is clearly something wrong
		#as it cannot be parsed in the normal way. Maybe im just over looking something silly
		if part.get_content_type() == "text/plain":
			ebody = part.get_payload()
			f = open("/tmp/enc_data", "w")
			f.write(ebody)
			f.close()
		if part.get_content_type() == "application/octet-stream":
			edigest = part.get_payload(decode=True)
		else:
			open("/tmp/encrypted_pass_recv", "w").write(part.get_payload(decode=True))
			#passwd = decrypt_pass(user)
			passwd = "test" #le sigh
			first = True
	
	os.popen("echo %s | openssl enc -d -aes-256-cbc -a -out %s -pass pass:test" %(ebody, "/tmp/mail"))


#decrypt the password attatched to the message so we can then decrypt the message body
def decrypt_pass(user):
	private_key = "./keys/%s/private_key.pem" %user
	print "\n\nUser :",private_key
	os.popen("openssl enc -a -d -in /tmp/encrypted_pass_recv -out /tmp/encrypted_pass_formatted")
	os.popen("openssl rsautl -decrypt -inkey %s -in %s" %( private_key, "/tmp/encrypted_pass_formatted"))



def send_mail(body, subject, to, from_):
	print "Sending"
	msg = MIMEMultipart()
	#msg = MIMEText(body)
	msg.attach(MIMEText(body, 'plain'))
	
	msg["Subject"] = subject
	msg["From"] = from_
	msg["To"] = to
	#Attatch the encrypted password to the message
 	file_pass = MIMEBase('application', "octet-stream")
	file_pass.set_payload(open("/tmp/encrypted_pass", "rb").read())
	#Set attachment name
	file_pass.add_header("Content-Disposition", "attachment; filename = passphrase")
	msg.attach(file_pass)
	
	#Attatch the checksum of the message body to the message
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

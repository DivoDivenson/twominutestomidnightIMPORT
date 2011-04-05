import smtpd
import asyncore
import socket
from smtpd import SMTPChannel
import os
import string
import email
from email import encoders
from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email.MIMEText import MIMEText
from email.MIMEAudio import MIMEAudio
from email.MIMEImage import MIMEImage
class CustomServer(smtpd.SMTPServer):
	def process_message(self, sender, sender_addr, rec, data):
		print "Saving message addressed to ", rec
		rec = rec[0]
		
		mails = "./mail/%s" %rec
		
		f = open(mails, 'w')
		f.write(sender_addr)
		f.write(rec[0]) #Assume only one receiver for the moment, this also overwrites any previous mails
		f.write(data)
		f.close
		return



	
try:
	server = CustomServer(('127.0.0.1', 1026), None)

	print("Serving...")
	asyncore.loop()
except KeyboardInterrupt:
	print "Interupt caught"
	server.close()	



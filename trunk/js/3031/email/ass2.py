#!/usr/bin/python

import smtplib
from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email.MIMEText import MIMEText
from email import Encoders
import os

user = "assign2.test@gmail.com"
pwd = "Test1234test"

def mail(to, subject, body):
	msg = MIMEMultipart()
	msg['From'] = user
	msg['To'] = to
	msg['Subject'] = subject

	msg.attach(MIMEText(body))

	#Set up client session
	mailServer = smtplib.SMTP("smtp.gmail.com", 587)
	#Identify as Extended SMTP server. Makes gmail like us
	mailServer.ehlo()
	print("Hello")
	#Put connection in TLS mode
	mailServer.starttls()
	#Say hello again cous the docs say so
	mailServer.ehlo()
	print("Loging in...")
	mailServer.login(user, pwd)
	print("Logged in\nSending...")
	mailServer.sendmail(user, to, msg.as_string())
	print("Done, closeing")
	mailServer.close()

def main():

	mail("divodivenson@gmail.com", "Hello!", "This is a test, I hope it went well");

if __name__ == '__main__':
	main()

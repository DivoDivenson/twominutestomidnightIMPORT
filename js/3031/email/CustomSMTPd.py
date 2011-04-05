import smtpd
import asyncore
import socket
from smtpd import SMTPChannel
import os


class CustomServer(smtpd.SMTPServer):
	def process_message(self, sender, sender_addr, rec, data):
		print "Saving message addressed to ", rec
		mails = "./mail/%s" %rec
		f = open(mails, 'w')
		print f
		print "From ", sender_addr
		f.write(sender_addr)
		print "To ", rec
		f.write(rec[0]) #Assume only one receiver for the moment, this also overwrites any previous mails
		print "Data ", repr(data)
		f.write(data)
		f.close
		return


	def handle_accept(self):
		conn, addr = self.accept()
		print "Incomming! %s" % repr(addr)
		mychannel = MySMTPChannel(self, conn, addr)

class MySMTPChannel(smtpd.SMTPChannel):

	def collect_incomming_data(self, data):
		print "Collect_incoming_data"
		self.__line.append(data)
		print data
#Extend SMTPChannel and overwrite it's handle read method ( from async_chat). Simple. Not	
	
try:
	server = CustomServer(('127.0.0.1', 1026), None)

	print("Serving...")
	asyncore.loop()
except KeyboardInterrupt:
	print "Interupt caught"
	server.close()	



import socket
import thread
import os, sys
import email
class ConnectionHandler:
	def __init__(self, connection, addr):
		self.connection = connection
		print "Connected"
		user =	connection.recv(1024)
		print user
		mails = "./mail/%s" % user
		print mails
		if os.path.exists(mails):
			print "Found"
			f = open(mails, "rb")
			data = ""
			for line in f:
				data += line

		#	msg = email.message_from_string(data)
			connection.send(data)	
		else:
			print "Not found"




if __name__ == '__main__':
	print "Serving..."
	try:
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.bind(('', 8080))
		s.listen(3)
		while 1:
			thread.start_new_thread(ConnectionHandler, s.accept())

	except KeyboardInterrupt:
		print "Caught"

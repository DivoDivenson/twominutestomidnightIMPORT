import socket
import thread
import os, sys
class ConnectionHandler:
	def __init__(self, connection, addr):
		self.connection = connection
		print "Connected"
		user =	connection.recv(1024)
		print user





if __name__ == '__main__':
	try:
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.bind(('', 8080))
		s.listen(3)
		while 1:
			thread.start_new_thread(ConnectionHandler, s.accept())

	except KeyboardInterrupt:
		print "Caught"

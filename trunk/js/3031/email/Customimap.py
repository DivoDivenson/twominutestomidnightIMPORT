import socket
import thread
import os, sys
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
			f = open(mails)
			text = ""
			#Read in mail and parse it
			f.readline()
			f.readline()
			f.readline() #Discard first 3 lines
			subject = f.readline()
			subject = subject[subject.find(':')+3:] #+3 to hack out the whitespace
			from_ = f.readline()
			from_ = from_[from_.find(':')+3:]
			to = f.readline()
			to = to[to.find(':')+3:]
			text = ""
			for line in f:
				text += line
			print text
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

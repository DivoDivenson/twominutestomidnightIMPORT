#!/usr/bin/python
from SimpleXMLRPCServer import SimpleXMLRPCServer
import xmlrpclib
import os
from file_id import *

#Filehandle should be immutable, do this with FileHandle factory? WHAT THE FUCK SHOULD A FILE HANDLE BE?


class RemoteFile():

	

	def __init__(self, filename):
		self.filename = filename


	def ls(self):
		return "Hello"

class FileServer():


	#Put in lots of lovely exception handleing
	def lookup(self, direc, name):
		contents = os.listdir(direc)
		result = None
		if name in contents:
			result = file_identifier(name, direc)

		return result

	def read(self, file_id):
		print file_id



			




server = SimpleXMLRPCServer(("localhost", 8080))
print "Serving"
server.register_instance(FileServer())
server.serve_forever()
	
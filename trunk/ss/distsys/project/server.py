#!/usr/bin/python
from SimpleXMLRPCServer import SimpleXMLRPCServer
import xmlrpclib
import os
from file_id import *

#Filehandle should be immutable, do this with FileHandle factory? WHAT THE FUCK SHOULD A FILE HANDLE BE?

#RPC methods must return something

class RemoteFile():

	

	def __init__(self, filename):
		self.filename = filename


	def ls(self):
		return "Hello"

class FileServer():


	#Put in lots of lovely exception handleing
	def lookup(self, path, name):
		contents = os.listdir(path)
		result = None
		if name in contents:
			result = file_identifier(path, name)

		return result

	def read(self, file_id, size):
		file_ = file_identifier.fromOne(file_id)
		f = open(file_.fileString())

		return f.read(size)



			




server = SimpleXMLRPCServer(("localhost", 8080))
print "Serving"
server.register_instance(FileServer())
server.serve_forever()
	
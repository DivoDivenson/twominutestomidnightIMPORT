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
	def lookup(self,file_id):
		#I prefare this way to trying to open and catching the exception if it fails. 
		#Not using exception handeling for flow control
		result = None


		file_ = file_identifier.fromOne(file_id)
		temp = file_.fileString().split('/')
		temp.pop(0) #Get rid of strting blank
		path = "/" + temp.pop(0)	
		for i in temp:
			print path
			contents = os.listdir(path)
			if(i in contents):
				path += '/' + i
			else:
				return result

		result = file_id

		return result

	def read(self, file_id, size):
		file_ = file_identifier.fromOne(file_id)
		f = open(file_.fileString())
		data = f.read(size)
		f.close()
		return data

	def write(self, file_id, data):
		#Enforce access model in case the client chooses not too
		#Not need for read, just they way python roles.
		if(self.lookup(file_id)):
			file_ = file_identifier.fromOne(file_id);
			f = open(file_.fileString(), 'w')
			f.write(data)
			f.close();
			return True
		
		return False

	def create(self, file_id):
		#If it exists, don't overwrite it, and if the directory exists
		file_ = file_identifier.fromOne(file_id)
		if( (self.lookup(file_id) != None) and self.lookup(file_.path)):
			f = open(file_.fileString)
			f.close()
			return file_id
		
		return None


 

	



			




server = SimpleXMLRPCServer(("localhost", 8080))
print "Serving:"
server.register_instance(FileServer())
server.serve_forever()
	
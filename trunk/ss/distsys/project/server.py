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
	#Test this later
	def lookup(self,file_id):
		#I prefare this way to trying to open and catching the exception if it fails. 
		#Not using exception handeling for flow control
		result = False

		#The following 4 lines are a bit gak
		file_ = file_identifier.fromOne(file_id)
		temp = file_.fileString().split('/')
		temp.pop(0) #Get rid of strting blank
		path = "/" + temp.pop(0)
			
		for i in temp:
			contents = os.listdir(path)
			if(i != '' and i in contents):
				path += '/' + i
				print path + " : " + i +":"
			else:
				print "Not found " + i + " in " + path
				return result

		result = file_id

		return result

	def read(self, file_id, size):
		if(self.lookup(file_id)):
			file_ = file_identifier.fromOne(file_id)
			f = open(file_.fileString())
			if(size == 0):
				data = f.read()
			else:
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
		#If it exists,overwrite it, but check if the directory exists
		file_ = file_identifier.fromOne(file_id)
		print file_.path
		#Just want to check if the directory exists
		#THIS LOOKUP IS FAILING
		if(self.lookup({'path' : file_.path, 'name' : ''})):
			print file_id
			f = open(file_.fileString, 'w')
			f.close()
			return file_id
		
		return False


 

	



			




server = SimpleXMLRPCServer(("localhost", 8080))
print "Serving:"
server.register_instance(FileServer())
server.serve_forever()
	
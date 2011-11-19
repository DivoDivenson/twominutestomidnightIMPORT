#!/usr/bin/python
from SimpleXMLRPCServer import SimpleXMLRPCServer
import xmlrpclib
import os
#from file_id import *

#Filehandle should be immutable, do this with FileHandle factory? WHAT THE FUCK SHOULD A FILE HANDLE BE?

#RPC methods must return something

class FileServer():


	#Put in lots of lovely exception handleing
	#Test this later

	#0 If file does not exist
	#1 If directory does not exist
	#fle_id Otherwise
	def lookup(self,file_id):
		#I prefare this way to trying to open and catching the exception if it fails. 
		#Not using exception handeling for flow control
		result = False
		if(self.lookupDir(file_id)):
			name = file_id.split('/').pop()
			path = file_id.rstrip(name)
			if(name in os.listdir(path)):
				result = file_id
		
		return result


	def lookupDir(self, file_id):
		result = False

		temp = file_id.split('/')
		temp.pop()
		temp.pop(0)
		path = '/' + temp.pop(0)

		for i in temp:
			contents = os.listdir(path)
			if(i in contents):
				path += '/' + i
			else:
				return result
		result = True

		return result



	def read(self, file_id, size):
		if(self.lookup(file_id)):
			f = open(file_id)
			if(size == 0):
				data = f.read()
			else:
				data = f.read(size)
			f.close()
			return data

	def write(self, file_id, data):
		#Enforce access model in case the client chooses not too
		if(self.lookup(file_id)):
			print file_id
			f = open(file_id, 'w')
			f.write(data)
			f.close();
			return True
		
		return False

	def create(self, file_id):
		#If the directory does not exist this will fail
		if(self.lookupDir(file_id)):
			f = open(file_id, 'w')
			f.close
			return file_id
		
		return False

	
	#Will probably take this out
	def readdir(self, file_id):
		if(self.lookup(file_id)):
			return os.listdir(file_id)
		else:
			return False




 

	



			




server = SimpleXMLRPCServer(("localhost", 8080))
print "Serving:"
server.register_instance(FileServer())
server.serve_forever()
	
#!/usr/bin/python
import xmlrpclib, httplib
import os
#from file_id import *

#Part of the client proxy
#This class presents the same functionality as a normal file object
class RemoteFile():

	server = xmlrpclib.ServerProxy("http://localhost:8080/")
	open_file = ""
	permission = ""

	def open(self, filename, mode="r"):
		response = self.server.lookup(filename)
		if response:
			self.open_file = response
			self.permission = mode
		elif mode == 'r':
			raise IOError ("No such file or directory: " + filename)
		elif mode == 'w':
			response = self.server.create(filename)
			if(response):
				self.open_file = response
				self.permission = mode
			else:
				raise IOError ("No such directory: " + filename)







	def read(self, size=0):
		result = self.server.read(self.open_file, size)

		return result

	def write(self, string):
		if(self.permission == 'w'):
			result = self.server.write(self.open_file, string)
		else:
			raise IOError ("File not open for writing: " + self.open_file)


	def close(self, string):
		open_file = ""

		return result

		


	

	

c = RemoteFile()
c.open('/home/divo/media/sata/College/twominutestomidnight/ss/distsys/project/server.py')
print c.read()
#b = RemoteFile()
#b.open('/home/divo/media/sata/College/twominutestomidnight/ss/distsys/project/test.txt', 'w');
#b.write("This is a test")






#proxy = xmlrpclib.ServerProxy("http://localhost:8080/")
#print proxy.ls()
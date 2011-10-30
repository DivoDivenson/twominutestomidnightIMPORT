#!/usr/bin/python
import xmlrpclib
import os
from file_id import *

#This class presents the same functionality as a normal file object
class ClientProxy():

	server = xmlrpclib.ServerProxy("http://localhost:8080/")
	open_file = ""

	#Extend for access type for locking
	def open(self, filename):
		#first do a lookup, split filename into path and name
		temp = filename.split('/')
		name = temp.pop()
		path = ''
		for i in temp:
			path += i + '/'

		#Check if file exists on server
		response = self.server.lookup(path, name)
		if response:
			self.open_file = file_identifier.fromOne(response)
		else:
			raise IOError ("No such file or directory: " + name)

	def read(self, size):
		print self.open_file
		result = self.server.read(self.open_file, size)

		return result
		

		

	
c = ClientProxy()
c.open('/home/divo/media/sata/College/twominutestomidnight/ss/distsys/project/server.py')
print c.read(1024)





#proxy = xmlrpclib.ServerProxy("http://localhost:8080/")
#print proxy.ls()
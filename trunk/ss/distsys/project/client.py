#!/usr/bin/python
import xmlrpclib
import os
from file_id import *

#This class presents the same functionality as a normal file object
class ClientProxy():

	server = xmlrpclib.ServerProxy("http://localhost:8080/")
	open_file = ""

	#Extend for access type for locking
	def open(self, filename, mode="r"):
		#first do a lookup, split filename into path and name
		#if mode not in {'r', 'w'}
		temp = filename.split('/')
		name = temp.pop()
		path = ''
		for i in temp:
			path += i + '/'

		#Check if file exists on server
		temp = file_identifier(path, name)
		response = self.server.lookup(temp)
		print response
		if response:
			self.open_file = file_identifier.fromOne(response)
		elif mode == 'r':
			raise IOError ("No such file or directory: " + name)
		elif mode == 'w':
			print "Attempting"
			response = self.server.create(temp)
			print response
			if ( response ):
				print "HEllo"
				self.open_file = file_identifier.fromOne(response)
			else:
				raise IOError ("No such directory: " + path)

	def read(self, size=0):
		result = self.server.read(self.open_file, size)

		return result

	def write(self, string):
		result = self.server.write(self.open_file, string)

		return result
		

		

	
c = ClientProxy()
c.open('/home/divo/media/sata/College/twominutestomidnight/ss/distsys/project/server.py')
#print c.read()
c.open("/home/divo/media/sata/College/twominutestomidnight/ss/distsys/project/sample.txt", "w")
#c.write("This is a test")






#proxy = xmlrpclib.ServerProxy("http://localhost:8080/")
#print proxy.ls()
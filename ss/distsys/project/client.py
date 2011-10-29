#!/usr/bin/python
import xmlrpclib
import os
from file_id import *


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
			open_file = file_identifier(response['name'], response['path'])
			print response.name
		else:
			raise IOError ("No such file or directory: " + name)

	def read(self):
		server.read(open_file)
		

		

	
c = ClientProxy()
c.open('/home/divo/media/sata/College/twominutestomidnight/ss/distsys/project/server.py')




#proxy = xmlrpclib.ServerProxy("http://localhost:8080/")
#print proxy.ls()
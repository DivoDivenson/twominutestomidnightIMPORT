#!/usr/bin/python
from SimpleXMLRPCServer import SimpleXMLRPCServer
import xmlrpclib

class RemoteFile(file):

	def __init__(self):
		

	def ls(self):
		return "Hello"


server = SimpleXMLRPCServer(("localhost", 8080))
print "Serving"
server.register_instance(RemoteFile())
server.serve_forever()
	
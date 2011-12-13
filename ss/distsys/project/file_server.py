#!/usr/bin/python

import SocketServer
import hashlib
import sys
from crypto import *
import json
from misc import *
from ss import *

#Servers private key
key = ""

'''

Services Server, this is the fileserver

So far the client sends lovely formatted messages with all sort of magical fields.
The server just farts data back across the wire.

'''



class TCPServer(SocketServer.TCPServer):
	allow_reuse_address = True


class FileServer(SocketServer.BaseRequestHandler, ServicesServer):
	cache = {}

	def handle(self):
		#Read in message of any length
		temp = self.request.recv(msg_size)
		data = temp
		while len(temp) == msg_size:
			temp = self.request.recv(msg_size)
			data += temp
		
		data = json.loads(data, strict=False)
		#Two types of unencrypted packets, login request and requests (the contents of which is encrypted)
		if(data['type'] == "auth"):
			self.handle_auth(data, key)
		#Else, check if user is already authenticated
		elif(data['type'] == "request"):
			#If user authenticated
			if(self.users.has_key(data['user'])):
				message = decrypt(data['message'], self.users[data['user']])
				message = json.loads(message, strict=False)

				filename = message['file']
				args = message['args']

				response = "Invalid request"
				#Now process the request
				if(message['type'] == "lookup"):
					response = self.lookup(filename)
				elif(message['type'] == "create"):
					response = self.create(filename)
				elif(message['type'] == "read"):
					response = self.read(filename)
				elif(message['type'] == "write"):
					response = self.write(filename, args)
				elif(message['type'] == "check"):
					response = self.check(filename)
				
				self.respond(response, data['user'])


	

	def create(self, filename):
		if(self.lookupDir(filename)):
			f = open(filename, 'w')
			f.close();
			return "created successfully"
		return "invalid directory"


	def read(self, filename):
		if(self.lookup(filename)):
			if(filename in self.cache.keys()):
				data = self.cache[filename]
			else:
				f = open(filename)
				data = f.read()
				f.close
				self.cache[filename] = data #Server side caching
			
			return data

	##Returns a checksum of the file for the client to do with as they please
	def check(self, filename):
		if(filename in self.cache.keys()):
			return genKey(self.cache[filename])
		else:
			data = self.read(filename)
			return genKey(data)	


	def write(self, filename, data):
		#Need to enforce permissions
		if(self.lookup(filename)):
			f = open(filename, 'w')
			f.write(data)
			if(filename in self.cache.keys()):
				self.cache[filename] = data
			f.close()
			return "file modified"
		
		return "Could not write"


	def lookup(self, filename):
		#Check the directory exists
		if(self.lookupDir(filename)):
			name = filename.split('/').pop()
			path = filename.rstrip(name)
			#Check the file exists
			if(name in os.listdir(path)):
				result = "file found"
			else:
				result = "file not found"
		else:
			result = "invalid directory"
			
		return result


	def lookupDir(self, filename):
		temp = filename.split('/')
		temp.pop()
		temp.pop(0)
		path = '/' + temp.pop(0)

		for i in temp:
			contents = os.listdir(path)
			if(i in contents):
				path += '/' + i
			else:
				return False
			
		return True


		
if __name__ == "__main__":
	if(len(sys.argv) == 2):
		print sys.argv[1]
	key = (read_config("./config/fs.json"))['key']
	
	config = (read_config("./config/servers.json"))['servers']['fs']
	server = TCPServer((config[0], int(config[1])), FileServer)

	try:
		print "Services server running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()

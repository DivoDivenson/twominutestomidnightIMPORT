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
cache_size = ""
#filename : user

'''

Services Server, this is the fileserver

So far the client sends lovely formatted messages with all sort of magical fields.
The server just farts data back across the wire.

'''



class TCPServer(SocketServer.TCPServer):
	allow_reuse_address = True


class FileServer(SocketServer.BaseRequestHandler, ServicesServer):
	cache = {}
	key_stack = []
	locks = {}




	#Inserts value in the cache, and also cleans the cache on every run. (Key is filename)
	def write_to_cache(self, key, value):
		if(len(self.key_stack) > cache_size):
			#If the cache is full, delete the oldest entry
			del cache[self.key_stack.pop(0)]
		
		#if file already in cache, update position
		if(key in self.key_stack):
			self.key_stack.remove(key)
			self.key_stack.append(key)
		#else cache the file
		else:
			self.cache[key] = value
			self.key_stack.append(key)

		
	#On read, update keys position in stack
	def read_from_cache(self, key):
		self.key_stack.remove(key)
		self.key_stack.append(key)
		return self.cache[key]

	

	def handle(self):
		#Read in message of any length
		temp = self.request.recv(msg_size)
		data = temp
		while len(temp) == msg_size:
			temp = self.request.recv(msg_size)
			print temp
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
				self.user = data['user']
				message = json.loads(message, strict=False)

				filename = message['file']
				args = message['args']

				response = "Invalid request"
				#Now process the request
				if(message['type'] == "lookup"):
					response = self.lookup(filename, args)
				elif(message['type'] == "create"):
					response = self.create(filename, args)
				elif(message['type'] == "read"):
					response = self.read(filename)
				elif(message['type'] == "write"):
					response = self.write(filename, args)
				elif(message['type'] == "check"):
					response = self.check(filename)
				elif(message['type'] == "close"):
					self.close(filename)
				
				self.respond(response, data['user'])


	

	def create(self, filename, args):

		if(args):
			self.locks[filename] = self.user
		if(self.lookupDir(filename)):
			f = open(filename, 'w')
			f.close();
			return "created successfully"
		return "invalid directory"

	#give up lock on close if file is locked by current user
	def close(self, filename):
		if (filename in self.locks):
			if self.locks[filename] == self.user:
				del self.locks[filename]


	def read(self, filename):
		if(self.lookup(filename, False)):
			if(filename in self.cache.keys()):
				data = self.read_from_cache(filename)
			else:
				f = open(filename)
				data = f.read()
				f.close
				self.write_to_cache(filename, data)
			
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
		if(filename in self.locks.keys()):
			if(self.locks[filename] != self.user):
				return "Could not write, file locked"

		if(self.lookup(filename, False)):
			f = open(filename, 'w')
			f.write(data)
			if(filename in self.cache.keys()):
				self.write_to_cache(filename, data)
			f.close()
			return "file modified"
		
		return "Could not write"


	def lookup(self, filename, args):
		#set lock on lookup
		if(args):
			self.locks[filename] = self.user
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
		name = sys.argv[1]
	else:
		name = "fs"




	key = (read_config("./config/"+name+".json"))['key']
	stack_size = int((read_config("./config/fs.json"))['cache_size'])
	
	config = (read_config("./config/servers.json"))['servers'][name]
	server = TCPServer((config[0], int(config[1])), FileServer)

	try:
		print "Services server running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()

#!/usr/bin/python
import socket
import sys
from crypto import *
import json
import time
from client_auth import *
from misc import *
import errno
#Tcp version
class RemoteFile():

	#Only one directory server for the moment
	all_servers = (read_config("./config/servers.json"))['servers']
	ds_server = all_servers['ds']
	open_file = ""
	permission = ""
	opened = False

	#user and client_ss_key
	def __init__(self, _user, _password):
		#self.fs_key = get_ss_key("fs")
		self.user = _user
		self.password = _password
		
		self.user = user
		self.cache = {} #Files opened already
		#self.mapped = {} #File already looked up

	#Query directory server to map filename
	def map_filename(self, filename, lock):
		#Check if filename has already been lookeduo and ask dir server if it has changed
		#if(filename in self.mapped.keys() and (self.get_sum(self.ds_server) == genKey(self.mapped[filename]))):
		#Actauly, there is no point doing this as the amount of work needed to check if the file
		#has moved is roughly the same as just looking it up again

		#If the directory server is down, try another one
		try:
			self.ds_key = get_ss_key("ds", self.user, self.password)
		except socket.error:
			print "hello"
		#Username, filename and a if a lock is being requested
		self.open_file = filename #This is a hack to send the filename to the directory server
		message = self.construct_message("map", self.ds_key, lock)
		response = self.send_message(message, self.ds_server)
	
		response = decrypt(response, self.ds_key)

		return response


	
	def open(self, filename, mode="r", lock=False):
		#first contact directory server and map the filename
		filename = self.map_filename(filename, lock)
		if(filename == "Invalid User" or filename == "Not Found"):
			self.open_file = ""
			raise IOError(filename)
		else:
			filename = json.loads(filename)
			self.server = filename['server']
			self.open_file = filename['file']
			self.fs_key = get_ss_key(self.server, self.user, self.password)

			message = self.construct_message("lookup", self.fs_key)
			response = self.send_message(message, self.all_servers[self.server])
			response = decrypt(response, self.fs_key)

			if response == "file found":
				self.permission = mode
				self.opened = True
			elif response == "invalid directory":
				self.opened = False
				raise IOError ("No such directory")
			elif  mode == "r":
				self.opened = False
				raise IOError ("No such file")
			elif mode == "w":
				#create the file
				message = self.construct_message("create", self.fs_key)
				response = self.send_message(message, self.all_servers[self.server])
				response = decrypt(response, self.fs_key)
				self.opened = True

	def get_sum(self, server):
		message = self.construct_message("check", self.fs_key)
		response = self.send_message(message, server)
		response = decrypt(response, self.fs_key)
		return response



	#Extend for size and such later
	def read(self, size=0):
		if(self.permission == 'w' or self.opened == False):
			raise IOError("File not open for reading")
		#Check if in cache and if the file has been modified
		if (self.open_file in self.cache.keys()) and (self.get_sum(self.all_servers[self.server]) == genKey(self.cache[self.open_file])):
			#Compute checksum of file and ask server to verify
			return self.cache[self.open_file]
		else:
			#Get the file and store in local cache
			message = self.construct_message("read", self.fs_key)
			response = self.send_message(message, self.all_servers[self.server])
			response = decrypt(response, self.fs_key)
			self.cache[self.open_file] = response

		return response

	def write(self, data):
		if(self.permission == 'r' or self.opened == False):
			raise IOError("File not open for writing")
		message = self.construct_message("write", self.fs_key, data)
		response = self.send_message(message, self.all_servers[self.server])
		response = decrypt(response, self.fs_key)
		#Update cached copy
		self.cache[self.open_file] = data

		return response

	def close(self):
		self.opened = False;
		self.open_file = ""
		#if locked then release


	def construct_message(self, request, key, args=""):
		#file is also being sent with directory server requests (as empty field). Optimize later
		payload = json.dumps({"type" : request, "file" : self.open_file, "args" : args})
		payload = encrypt(payload, key)
		message = json.dumps({"type" : "request", "user" : self.user, "message" : payload})
		return message

	#Open a connection to services server and return the response
	def send_message(self, message, server):
		sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		try:
			sock.connect((server[0], int(server[1])))
			sock.send(message)
			
			response = ""
			while 1:
				data = sock.recv(msg_size)
				if not data: break
				response += data
			
			return response
	
		finally:
			sock.close()

	

if __name__ == "__main__":
	user = "divines"
	password = "thisisapassword"
	r_file = RemoteFile(user, password)
	r_file.open("/home/divo/vdrive/test.txt", 'r', True)
	print r_file.read()
	'''print r_file.read()
	r_file.close()
	r_file.open("/home/divo/vdrive/test.txt" , 'w')

	r_file.write("test write 1234")'''
	r_file.close()


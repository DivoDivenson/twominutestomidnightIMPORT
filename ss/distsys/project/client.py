#!/usr/bin/python
import socket
import sys
from crypto import *
import json
import time
from client_auth import *
from misc import *

#Tcp version
class RemoteFile():

	#Only one directory server for the moment
	all_servers = (read_config("./config/servers.json"))['servers']
	ds_server = all_servers['ds']
	open_file = ""
	permission = ""

	#user and client_ss_key
	def __init__(self, user):
		#self.fs_key = get_ss_key("fs")
		self.ds_key = get_ss_key("ds")
		self.user = user
		self.cache = {}

	#Query directory server to map filename
	def map_filename(self, filename):
		message = self.construct_message("map", self.ds_key, filename)
		response = self.send_message(message, self.ds_server)
		response = decrypt(response, self.ds_key)

		return response



	def open(self, filename, mode="r"):
		#first contact directory server and map the filename
		filename = self.map_filename(filename)
		filename = json.loads(filename)
		self.server = filename['server']
		self.open_file = filename['file']
		self.fs_key = get_ss_key(self.server)

		message = self.construct_message("lookup", self.fs_key)
		response = self.send_message(message, self.all_servers[self.server])
		response = decrypt(response, self.fs_key)

		if response == "file found":
			self.permission = mode
		elif response == "invalid directory":
			raise IOError ("No such directory " + filename)
		elif  mode == "r":
			raise IOError ("No such file " + filename)
		elif mode == "w":
			#create the file
			message = self.construct_message("create", self.fs_key)
			response = self.send_message(message, self.all_servers[self.server])
			response = decrypt(response, self.fs_key)

	def get_sum(self):
		message = self.construct_message("check", self.fs_key)
		response = self.send_message(message, self.all_servers[self.server])
		response = decrypt(response, self.fs_key)
		return response



	#Extend for size and such later
	def read(self, size=0):
		#Check if in cache and if the file has been modified
		if (self.open_file in self.cache.keys()) and (self.get_sum() == genKey(self.cache[self.open_file])):
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
		message = self.construct_message("write", self.fs_key, data)
		response = self.send_message(message, self.all_servers[self.server])
		response = decrypt(response, self.fs_key)
		#Update cached copy
		self.cache[self.open_file] = data

		return response

	#Prehaps need to add an arguments field
	def construct_message(self, request, key, args=""):
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
	r_file = RemoteFile(user)
	r_file.open("/home/divo/vdrive/test.txt", 'w')
	print r_file.read()
	print r_file.read()

	r_file.write("test write 1234")


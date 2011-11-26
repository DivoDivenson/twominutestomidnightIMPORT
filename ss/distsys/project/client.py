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

	server =(read_config("./config/servers.json"))['servers']['ss']
	open_file = ""
	permission = ""

	#user and client_ss_key
	def __init__(self, user, auth_key):
		self.key = auth_key
		self.user = user

	def open(self, filename, mode="r"):
		self.open_file = filename
		message = self.construct_message("lookup")
		response = self.send_message(message)
		response = decrypt(response, self.key)

		if response == "file found":
			self.permission = mode
		elif response == "invalid directory":
			raise IOError ("No such directory " + filename)
		elif  mode == "r":
			raise IOError ("No such file " + filename)
		elif mode == "w":
			#create the file
			message = self.construct_message("create")
			response = self.send_message(message)
			response = decrypt(response, self.key)

	#Extend for size and such later
	def read(self, size=0):
		message = self.construct_message("read")
		response = self.send_message(message)
		response = decrypt(response, self.key)

		return response

	def write(self, data,):
		message = self.construct_message("write", data)
		response = self.send_message(message)
		response = decrypt(response, self.key)	

		return response

	#Prehaps need to add an arguments field
	def construct_message(self, request, args=""):
		payload = json.dumps({"type" : request, "file" : self.open_file, "args" : args})
		payload = encrypt(payload, self.key)
		message = json.dumps({"type" : "request", "user" : self.user, "message" : payload})
		return message

	#Open a connection to services server and return the response
	def send_message(self, message):
		sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		try:
			sock.connect((self.server[0], int(self.server[1])))
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
	service_key = get_ss_key()
	user = "divines"
	r_file = RemoteFile(user, service_key)
	r_file.open("/home/divo/media/sata/College/twominutestomidnight/ss/distsys/project/test.txt", 'w')
	#print r_file.read()
	r_file.write("test")


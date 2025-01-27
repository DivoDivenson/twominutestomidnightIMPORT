#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json
from misc import *

#Put all this stuff in files later
#users = {'divines' : "pass"}

#Make these complicated later
#Secret key
#TGS_key = "thisbeakey"
TGS_key = ""
users = ""


msg_size = 1024


requests = ["login"]


class TCPServer(SocketServer.TCPServer):
	allow_reuse_address = True

	
#Login works
#Client-TGS key is just a AES of the client key against the TGS secret key
class AuthenticationServer(SocketServer.BaseRequestHandler):

	def handle(self):
		#for the moment presume all message are under 1024
		user = self.request.recv(msg_size)
	
		client_key = self.lookup(user)
		if(client_key):
			#Client-TGS session key
			client_TGS_key = encrypt(client_key, TGS_key)
			a = encrypt(client_TGS_key, client_key)
			

				#Encryptde ticket
			b = json.dumps({"user" : user, "address" : self.client_address[0], "client_tgs" : client_TGS_key})
			b = encrypt(b, TGS_key)
			response = json.dumps({"public" : a, "private" : b})
		else:
			response = "Invalid User"
				#reply
		self.request.send(response)

		print "Session key sent to " + user



		
			

	#Lookup a user an return a client key
	def lookup(self, name):
		if name in users:
			password = users[name]
			key = genKey(password)
			return key
		else:
			return None




if __name__ == "__main__":
	users = (read_config("./config/users.json"))['users']
	TGS_key = (read_config("./config/as.json"))['key']

	config = (read_config("./config/servers.json"))['servers']['as']


	server = TCPServer((config[0], int(config[1])), AuthenticationServer)

	try:
		print "Authentication Server Running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()



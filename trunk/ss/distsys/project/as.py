#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json

#Put all this stuff in files later
users = {'divines' : "pass"}

#Make these complicated later
TGS_key = "thisbeakey"

#client_TGS_key = "anotherkey"

msg_size = 1024


requests = ["login"]

#Leave expiry out for now
'''
All messages are now using json
Messages
Client -> Server
Login request: "login\n<username>\n"

Server -> Client
Login response: "client_key(<client tgs key>)\nTGS(<username>,<client address>,<client tgs key>\n


'''
#Login works
#Client-TGS key is just a AES of the client key against the TGS secret key
class AuthenticationServer(SocketServer.BaseRequestHandler):

	def handle(self):
		#for the moment presume all message are under 1024
		self.data = self.request.recv(msg_size)
		self.data = json.loads(self.data)
		if(self.data['type'] == "login"):
			user = self.data['user']
			#Gen client key by hashing user password
			client_key = self.lookup(user)

			#Client-TGS session key
			client_TGS_key = encrypt(client_key, TGS_key)
			a = encrypt(client_TGS_key, client_key)
			print client_TGS_key

			b = json.dumps({"user" : user, "address" : self.client_address[0], "client_tgs" : client_TGS_key})
			b = encrypt(b, TGS_key)
			response = json.dumps({"public" : a, "private" : b})

			self.request.send(response)

			#b = user + '\n' + self.client_address[0] + '\n' + client_TGS_key
			#response += encrypt(encrypted_payload, TGS_key)
			#Send back encryptde message
			#self.wfile.write(response)'''
			
			

	#Lookup a user an return a client key
	def lookup(self, name):
		if name in users:
			password = users[name]
			key = genKey(password)
			return key




if __name__ == "__main__":
	server = SocketServer.TCPServer(("localhost", 8081), AuthenticationServer)

	try:
		print "Authentication Server Running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()



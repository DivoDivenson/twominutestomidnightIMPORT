#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *

#Put all this stuff in files later
users = {'divines' : "pass"}

#Make these complicated later
TGS_key = "thisbeakey"

client_TGS_key = "anotherkey"




requests = ["login"]

#Leave expiry out for now
'''
Messages
Client -> Server
Login request: "login\n<username>\n"

Server -> Client
Login response: "client_key(<client tgs key>)\nTGS(<username>,<client address>,<client tgs key>\n


'''


class AuthenticationServer(SocketServer.StreamRequestHandler):

	def handle(self):
		self.data = self.rfile.readline().rstrip('\n')
		print self.data
		if(self.data == "login"):
			user = self.rfile.readline().rstrip('\n')
			client_key = self.lookup(user)

			response = encrypt(client_TGS_key, client_key) + '\n'
			encrypted_payload = user + '\n' + self.client_address[0] + '\n' + client_TGS_key + '\n'
			response += encrypt(encrypted_payload, TGS_key)
			#Send back encryptde message
			self.wfile.write(response)
			
			

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



#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json

msg_size = 1024

#No need for service ID as we only have one

#Secret key
TGS_key = "thisbeakey"
SS_key = "anotherkey"


class TCPServer(SocketServer.TCPServer):
	allow_reuse_address = True


class TicketGrantingServer(SocketServer.BaseRequestHandler):

	def handle(self):
		data = self.request.recv(msg_size)
		
		data = json.loads(data, strict=False)
		#Decrypt AS private payload and get client_TGS_key
		ticket = decrypt(data['ticket'], TGS_key)
		ticket = json.loads(ticket, strict=False)
		client_TGS_key = ticket['client_tgs']

		authenticator = decrypt(data['auth'], client_TGS_key)
		authenticator = json.loads(authenticator, strict=False)

		#Client-SS key is sha1 of username encrypted with AES against the SS key
		client_SS_key = encrypt(genKey(authenticator['user']), SS_key)
		ticket = json.dumps({"user" : authenticator['user'], "address" : self.client_address[0], "client_ss" : client_SS_key} )
		ticket = encrypt(ticket, SS_key);

		#Not quite sure what the point of this part is
		client_ss = json.dumps({"client_ss" : client_SS_key})
		client_ss = encrypt(client_ss, client_TGS_key)

		response = json.dumps({"ticket" : ticket, "client_ss" : client_ss})

		self.request.send(response)
		print "Response sent to client"







if __name__ == "__main__":
	server = SocketServer.TCPServer(("localhost", 8082), TicketGrantingServer)

	try:
		print "Ticket granting server running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()

	
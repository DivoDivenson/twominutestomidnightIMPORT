#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json
from misc import *

key = ""

msg_size = 1024


class TCPServer(SocketServer.TCPServer):
	allow_reuse_address = True


class ServicesServer(SocketServer.BaseRequestHandler):


	def handle(self):
		data = self.request.recv(msg_size)
		data = json.loads(data, strict=False)

		tgs_ticket = data['ticket']
		tgs_ticket = decrypt(tgs_ticket, key)
		tgs_ticket = json.loads(tgs_ticket, strict=False)

		tgs_user = tgs_ticket['user']

		client_ss_key = tgs_ticket['client_ss']

		authenticator = data['auth']
		authenticator = decrypt(authenticator, client_ss_key)
		authenticator = json.loads(authenticator, strict=False)

		ss_user = authenticator['user']

		#Verfiy that it's the user that contaced the TGS
		if(tgs_user == ss_user):
			#If users match, send back user timestamp encryptde with CLient_ss_key
			response = encrypt(authenticator['time'], client_ss_key)
			response = json.dumps({"time" : response})
		else:
			print "Client authentication failed"
			response = "Nope"

		print("Sending response to client")
		self.request.send(response)

		







if __name__ == "__main__":
	key = (read_config("./config/ss.json"))['key']
	
	config = (read_config("./config/servers.json"))['servers']['ss']
	server = TCPServer((config[0], int(config[1])), ServicesServer)
	
	try:
		print "Services server running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()

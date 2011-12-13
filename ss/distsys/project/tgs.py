#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json
from misc import *

msg_size = 1024

#No need for service ID as we only have one

TGS_key = ""
#FS_key = ""
#DS_key = ""

DS_KEYS = ""
FS_KEYS = ""
keys = {}


class TCPServer(SocketServer.TCPServer):
	allow_reuse_address = True


class TicketGrantingServer(SocketServer.BaseRequestHandler):

	def handle(self):
		data = self.request.recv(msg_size)
		
		data = json.loads(data, strict=False)
		#Decrypt AS private payload and get client_TGS_key
		'''if(data['request'] == "ds"):
			service_key = 
		elif(data['request'] == "fs"):
			service_key = FS_key'''
		service_key = keys[data['request']]

		ticket = decrypt(data['ticket'], TGS_key)
		ticket = json.loads(ticket, strict=False)
		client_TGS_key = ticket['client_tgs']

		authenticator = decrypt(data['auth'], client_TGS_key)
		authenticator = json.loads(authenticator, strict=False)

		#Client-SS key is sha1 of username encrypted with AES against the SS key
		client_SS_key = encrypt(genKey(authenticator['user']), service_key)
		ticket = json.dumps({"user" : authenticator['user'], "address" : self.client_address[0], "client_ss" : client_SS_key} )
		ticket = encrypt(ticket, service_key);

		#Not quite sure what the point of this part is
		client_ss = json.dumps({"client_ss" : client_SS_key})
		client_ss = encrypt(client_ss, client_TGS_key)

		response = json.dumps({"ticket" : ticket, "client_ss" : client_ss})

		self.request.send(response)
		print "Response sent to client"







if __name__ == "__main__":
	keys = read_config("./config/tgs.json")
	TGS_key = keys['tgs_key']
	FS_KEYS  = keys['fs_keys']

	DS_KEYS = keys['ds_keys']

	keys = dict(FS_KEYS.items() + DS_KEYS.items())



	config = (read_config("./config/servers.json"))['servers']['tgs']
	server = TCPServer((config[0], int(config[1])), TicketGrantingServer)

	try:
		print "Ticket granting server running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()

	
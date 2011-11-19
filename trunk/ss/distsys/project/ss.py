#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json

key = "anotherkey"

msg_size = 1024


class TCPServer(SocketServer.TCPServer):
	allow_reuse_address = True


class ServicesServer(SocketServer.BaseRequestHandler):


	def handle(self):
		data = self.request.recv(msg_size)
		data = json.loads(data, strict=False)

		client_ss_key = data['ticket']
		client_ss_key = decrypt(client_ss_key, key)

		authenticator = data['auth']
		authenticator = decrypt(authenticator, client_ss_key)
		print authenticator






if __name__ == "__main__":
	server = SocketServer.TCPServer(("localhost", 8083), ServicesServer)

	try:
		print "Services server running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()

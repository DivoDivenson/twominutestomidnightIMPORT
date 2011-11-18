#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json

msg_size = 1024


#Secret key
TGS_key = "thisbeakey"


class TicketGrantingServer(SocketServer.BaseRequestHandler):

	def handle(self):




if __name__ == "__main__"
	server = SocketServer.TCPServer(("localhost", 8082, TicketGrantingServer))

	try:
		print "Ticket granting server running"
		server.serve_forever()
	except KeyboardInterrupt:
		server.shutdown()

	
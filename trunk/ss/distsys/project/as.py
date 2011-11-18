#!/usr/bin/python

import SocketServer

class AuthenticationServer(SocketServer.StreamRequstHandler):

	def handle(self):
		self.data = self.rfile.readline()
		print self.data


if __name__ == "__main__":
	server = SocketServer.TCPServer(("localhost", 8081), AuthenticationServer)

	server.serve_forever()


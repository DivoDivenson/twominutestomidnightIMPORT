#!/usr/bin/python
import socket
import sys
from crypto import *

HOST, PORT = "localhost", 8081

USER = "divines"
PASS = "pass"


#All of these methods throw socket exceptions
def login(socket):
	sock.send("login\n" + USER + '\n')
	response = ""
	while 1:
		data = sock.recv(1024)
		if not data: break
		response += data

	return response







sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
	sock.connect((HOST, PORT))
	ticket = login(sock)
	key = genKey(PASS)
	tgs_key = decrypt(ticket.split('\n')[0], key)
	print tgs_key

	

	
finally:
	sock.close()


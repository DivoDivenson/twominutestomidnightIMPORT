#!/usr/bin/python
import socket
import sys
from crypto import *
import json
import time

HOST, AS_PORT = "localhost", 8081
TGS_PORT = 8082

msg_size = 1024

USER = "divines"
PASS = "pass"


def login():
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	try:
		sock.connect((HOST, AS_PORT))
		sock.send(USER)
		response = ""
		while 1:
			data = sock.recv(msg_size)
			if not data: break
			response += data

		return response
	
	finally:
		sock.close()

def getTicket(as_ticket, tgs_client_key):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	try:
		sock.connect((HOST, TGS_PORT))
		authenticator = json.dumps({"user" : USER, "time" : time.time()}) 
		authenticator = encrypt(authenticator, tgs_client_key)

		message = json.dumps({"ticket" : as_ticket, "auth" : authenticator})
		sock.send(message)
		response = ""
		while 1:
			data = sock.recv(msg_size)
			if not data: break
			response += data
	
		return response

	finally:
		sock.close()






login_ticket = json.loads(login(), strict=False)
key = genKey(PASS)
tgs_client_key = decrypt(login_ticket['public'], key)
print "Session key obtained, contacting TGS"
tgs_ticket = getTicket(login_ticket['private'], tgs_client_key)
print tgs_ticket

	



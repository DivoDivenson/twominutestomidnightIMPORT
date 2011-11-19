#!/usr/bin/python
import socket
import sys
from crypto import *
import json
import time

HOST, AS_PORT = "localhost", 8081
TGS_PORT = 8082
SS_PORT =  8083

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

def getTicket(as_ticket, tgs_client_key, time_stamp):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	try:
		sock.connect((HOST, TGS_PORT))

		authenticator = build_authenticator(time_stamp)
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

#Supposed to encrypt tgs_ticket with TGS key. That must be wrong? Encrypted with SS key surly?
def auth_ss(tgs_ticket, client_ss, time_stamp):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

	try:
		sock.connect((HOST, SS_PORT))

		authenticator = build_authenticator(time_stamp)
		authenticator = encrypt(authenticator, client_ss)

		message = json.loads({"ticket" : tgs_ticket, "auth" : authenticator})
		sock.send(message)

	finally:
		sock.close()


def build_authenticator(time_stamp):
	return json.dumps({"user" : USER, "time" : time_stamp})




login_ticket = json.loads(login(), strict=False)
key = genKey(PASS)
tgs_client_key = decrypt(login_ticket['public'], key)
print "Session key obtained, contacting TGS"
time_stamp = time.time()
tgs_response = getTicket(login_ticket['private'], tgs_client_key, time_stamp)
print "TGS ticket obtained, contacing services server"
tgs_response = json.loads(tgs_response, strict=False)
print tgs_response
tgs_ticket = tgs_response['ticket']

client_ss = tgs_response['client_ss']
client_ss = decrypt(client_ss, tgs_client_key)
client_ss = json.loads(client_ss, strict=False)['client_ss']

time_stamp = time.time()
ss_respone = auth_ss(tgs_ticket, client_ss, time_stamp)


	



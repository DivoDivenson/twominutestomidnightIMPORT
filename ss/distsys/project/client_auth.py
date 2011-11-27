#!/usr/bin/python
import socket
import sys
from crypto import *
import json
import time
from misc import *

#READ THIS IN LATER
HOST, AS_PORT = "localhost", 8081
TGS_PORT = 8082
SS_PORT =  8083

servers = ""

USER = "divines"
PASS = "thisisapassword"

#Auth with AS
def login(server):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	try:
		sock.connect(server)
		sock.send(USER)
		response = ""
		while 1:
			data = sock.recv(msg_size)
			if not data: break
			response += data
		
		return response
	
	finally:
		sock.close()

#Get a ticket from ticket granting server
def getTicket(server, as_ticket, tgs_client_key, time_stamp, service):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	try:
		sock.connect(server)

		authenticator = build_authenticator(time_stamp)
		authenticator = encrypt(authenticator, tgs_client_key)

		message = json.dumps({"request" : service, "ticket" : as_ticket, "auth" : authenticator})
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
def auth_ss(server, tgs_ticket, client_ss, time_stamp):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

	try:
		sock.connect(server)

		authenticator = build_authenticator(time_stamp)
		authenticator = encrypt(authenticator, client_ss)

		#Service servers have arequest type because they can do more than one thing
		message = json.dumps({"type" : "auth", "ticket" : tgs_ticket, "auth" : authenticator})
		sock.send(message)

		response = ""
		#Really no need for these while loops, but just in case I decided to use some crazy crypto algo....
		#That's never going to happen is it?
		#Probably not, no.....
		#.....
		#*smack*
		#;_;
		while 1:
			data = sock.recv(msg_size)
			if not data: break
			response += data
	
		return response

	finally:
		sock.close()


def build_authenticator(time_stamp):
	return json.dumps({"user" : USER, "time" : time_stamp})

#Convert input json to a typle to be used in initing a socket
def json_to_tuple(json_data, service):
	return (json_data[service][0], int(json_data[service][1]))


#Authenticate and get a Client-SS key to use
#Put in error checkign on service type

#IMPLEMENT THIS
#def get_ss_key(service_type, user, password):
def get_ss_key(service_type):

	servers = read_config("./config/servers.json")
	servers = servers['servers']
	as_tuple = json_to_tuple(servers, "as")

	login_ticket = json.loads(login(as_tuple), strict=False)
	key = genKey(PASS)
	tgs_client_key = decrypt(login_ticket['public'], key)
	print "Session key obtained, contacting TGS"

	#Convert timestamps to strings for simplicity
	time_stamp = str(time.time())
	tgs_tuple = json_to_tuple(servers, 'tgs')
	tgs_response = getTicket(tgs_tuple, login_ticket['private'], tgs_client_key, time_stamp, service_type)
	print "TGS ticket obtained, contacing services server"

	tgs_response = json.loads(tgs_response, strict=False)
	tgs_ticket = tgs_response['ticket']

	client_ss = tgs_response['client_ss']
	client_ss = decrypt(client_ss, tgs_client_key)
	client_ss = json.loads(client_ss, strict=False)['client_ss']

	time_stamp = str(time.time())

	ss_tuple = json_to_tuple(servers, service_type)
	ss_response = auth_ss(ss_tuple, tgs_ticket, client_ss, time_stamp)
	ss_response = json.loads(ss_response, strict=False)

	ss_time = ss_response['time']
	ss_time = decrypt(ss_time, client_ss)

	if(ss_time == time_stamp):
		return client_ss
	else:
		raise Warning ("Something went wrong contacting SS server")


	



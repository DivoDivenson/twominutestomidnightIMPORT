#!/usr/bin/python

import SocketServer
import hashlib
import base64
from crypto import *
import json
from misc import *

#Servers private key



class ServicesServer():
		
	users = {}
	#Takes in a bunch of json
	def handle_auth(self, data, key):
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
			#If users match, store them as authenticated and
			#send back user timestamp encryptde with Client_ss_key
			self.users.update({ss_user : client_ss_key})

			response = encrypt(authenticator['time'], client_ss_key)
			response = json.dumps({"time" : response})
		else:
			print "Client authentication failed"
			response = "Nope"

		print("Sending response to client")
		self.request.send(response)


		#Encrypt message for ser and send
	def respond(self, message, user):
		response = encrypt(message, self.users[user])
		print "Sending response to " + user
		self.request.sendall(response)




		

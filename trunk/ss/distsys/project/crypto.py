#/usr/bin/python

import os
import hashlib
import base64

def encrypt(text, key):
	data = base64.b64encode(text)
	return aes(data, key)
	

def decrypt(text, key):
	data = aes(text, key, "-d")
	return base64.b64decode(data)

#mode = "" is encryption
#mode = "-d" is decryption
def aes(text, key, mode=""):
	command = "echo \'"+text+"\' | openssl enc "+mode+" -aes-256-cbc -a -salt -pass pass:\""+key+"\""
	pipe = os.popen(command)
	data = pipe.read().strip()
	pipe.close()
	'''f = open("output.txt")
	data = f.read()
	f.close()'''
	return data

def genKey(text):
	return hashlib.sha1(text).hexdigest()


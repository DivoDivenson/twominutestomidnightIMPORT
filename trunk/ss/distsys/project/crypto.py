#/usr/bin/python

import os
import hashlib

def encrypt(text, key):
	return aes(text, key)
	

def decrypt(text, key):
	return aes(text, key, "-d")

#mode = "" is encryption
#mode = "-d" is decryption
def aes(text, key, mode=""):
	command = "echo \'"+text+"\' | openssl enc "+mode+" -aes-256-cbc -a -salt -pass pass:\""+key+"\""
	pipe = os.popen(command)
	data = pipe.read().strip()
	pipe.close()
	return data

def genKey(text):
	return hashlib.sha1(text).hexdigest()


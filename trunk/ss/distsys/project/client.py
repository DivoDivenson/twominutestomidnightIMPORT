#!/usr/bin/python
import socket
import sys

HOST, PORT = "localhost", 8081

USER = "divines"

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
	sock.connect((HOST, PORT))
	sock.send("login\n" + USER + '\n')
finally:
	sock.close()


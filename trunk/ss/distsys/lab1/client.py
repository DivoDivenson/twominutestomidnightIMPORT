import socket
import sys

HOST = ''
PORT = 8080
#Create a connection
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#Connect to server
s.connect((HOST, PORT))
#Send the user string (Given as an argument to the script)
s.send(sys.argv[1])
#Recv and print filtered string
data = s.recv(1024)
print data
s.close()

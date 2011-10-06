import sys, string
import socket, thread, select

HOST = '' #Alias for any and all hosts
PORT = 8080
BUF = 1024

#Basic list of badwords
badwords = ["first", "eample", "test"]
#Make a set because python hates me
badwords = set(badwords)

#Go through the message and replace any badwords with the right number of x
#Bit of a round-about way of doing it but meh
def handleData(data):
	data = data.split(' ')
	result = ""
	for i in data:
		temp = i.lower()
		if(temp in badwords):
			for c in temp:
				result += 'x'
		else:
			result += i
	
		result += ' '

	return result		


#Create and bind a socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST,PORT))
print "Listening"
s.listen(4)  #4 specifies the amount of connections that can be queued
while 1:
	#Accept forks a new thread with a new socket for every incoming connection, so it is multi-threaded
	conn, addr = s.accept()
	data = conn.recv(BUF) #Recv data from client
	data = handleData(data) #Filter for badwords
	conn.send(data) #Send back filtered string
	conn.close()

s.close()

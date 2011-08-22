#! /usr/bin/python
import socket
import struct


s = socket.socket()
print "Connecting"
s.connect(("vortex.labs.overthewire.org", 5842))
print "Connected"
len_int = struct.calcsize('I')
data = [s.recv(len_int) for i in xrange(4)]

print data
result = sum(struct.unpack('<4I', ''.join(data)))

s.send(struct.pack('<Q', result))

print s.recv(1024)

s.close()

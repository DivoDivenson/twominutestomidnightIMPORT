"""Steven Diviney
   08462267

"""

import asyncore
import socket
import string


class ConnectionHandler (asyncore.dispatcher):

   def __init__ (self, host, path):
      asyncore.dispatcher.__init__(self)
      self.path = path
      self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
      self.connect((host, 80))

   def handle_connect (self):
		self.send ('GET %s HTTP/1.0\r\n\r\n' % self.path)

   def handle_read (self):
#Needs a loop to keep reading
      data = self.recv(8192)
      print data


   def handle_write (self):
      pass


if __name__ == '__main__':
   import sys
   import urlparse
   for url in sys.argv[1:]:
      parts = urlparse.urlparse(url)
      if parts[0] != 'http':
         raise ValueError, "HTTP requests only please" #Force an exception if
      else:
         host = parts[1]
         path = parts[2]
         ConnectionHandler(host, path)
   asyncore.loop()




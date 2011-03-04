"""Steven Diviney
   08462267

"""
import asynchat
import asyncore
import socket
import string

class proxy_server (asyncore.dispatcher):

   def __init__ (self, host, port)
      asyncore.dispatcher.__init__ (self) #FIND a way to close a socket on ^C. This is getting annoying
      self.create_socket (socket.AF_INET, socket.SOCK_STREAM) 
      self.set_reuse_addr() #Done. Or at least better
      self.there = (host, port) 
      self.bind ('', 8080)
      self.listen (5)
      


import sys, string
import socket, thread, select
import cPickle
import pickle
import threading, os
import time
import gtk
HOST = ''
PORT = 8080
BUF = 4098 #Recv buffer size

dns_cache = {'test' : 1}
dns_family_cache = {'test' : 1} #Not really need but just in case
blacklist = ['www.example.com'] 
web_cache = {'test' : 1}


class ConnectionHandler:
   
   def __init__(self , connection, addr, id):
      self.connection = connection
      self.id = id
      print 'Thread ', id
      print 'Connected ' , addr
      self.request = self.get_request() #See what the client wants
      self.original = self.request
   
      self.header = self.parse_request(self.request) #Extract host and path from request packet
      if self.host in blacklist: 
      	 self.post_blacklisted()
      elif self.host + self.path in web_cache:
         print "Address seen, using cached page", self.host
	 cache = web_cache[self.host + self.path]
	 for i in cache:
	    if i[1] == 'c': #Important to send the right packets in the right direction
               self.connection.send(i[0])
	    else:
	       self.destination.send(i[0])
      else: #URL never seen before, start new connection
         if self.code == 'CONNECT':
            self.handle_connect()
         else:
            self.handle_else()
            self.destination.send(self.original) #Forward on original message
            self.response_select()
            print 'Response gotten'
            self.connection.close()
            self.destination.close()
            print 'Sockets closed ', self.id

   def handle_connect(self):
      self.handle_else()
      self.connection.send('HTTPVER/1.1 200 Connection established\n')

   def post_blacklisted(self): #Have this send a 403 or something
	print "Blaklisted address"
   
   #UNUSED
   def response (self):  
      buffer = ''
      while 1:
         data = self.destination.recv(BUF)
         if len(data) < BUF: 
            print 'All data got'
            break
         buffer = buffer + data
      buffer = buffer + data
#      print 'Final Buffer :', buffer
      print 'Sending back to browser'
      self.connection.send(buffer)

   def response_select (self): #Get response from server using select()
      print 'Awaiting response'
      buffer = ''
      sockets = [self.connection, self.destination]
      breakout = 0
      count = 0
      cache = []
      direction = []
      while 1:
         count += 1
         if breakout == 1:
            break
         print 'Waiting on select'
         (recv, send, error) = select.select(sockets, [], sockets)
         if error:
            print 'Error'
            break
         if recv:
            print 'Getting '
            for i in recv: #recv is a list of socket objects
               data = i.recv(BUF)
            if i is self.connection:
               out = self.destination
               temp = 'd'
	    else:
               out = self.connection
               temp = 'c'
	    if data:
#How to stop? Try looking for a 204 aswell as a null buffer
               print 'Sending...'
               #print repr(data)
	       cache.append((data, temp)) #Store packet into a list
	       out.send(data)
               count = 0
               print 'Sent'
               end = '204'
               temp = data.find(end, 0, 12)
               if temp > 0:
                  breakout = 1 #Some webservers send a 204 and then silence, this makes select wait until timeout 
		  	       #on the next loop iteration. This is to stop that
         if count == 10: #Sort of a timeout
            break
      web_cache[self.host + self.path] = cache #Store list of packets and index them by URL. This is the cache
      print "Attempting to write ", self.host + self.path #Flush cache to disk, not thread safe
      output = open('cache.pkl', 'wb')
      pickle.dump(web_cache, output)
      output.close()
      
      print 'Sentall'

#Take incomming request and break it up
   def parse_request (self, request):
      print 'Parsing request'
      header_delm = self.request.find('\n') #All we want is the request, not the payload
      header = self.request[:header_delm] 
      self.request = self.request[header_delm:] #Payload
      #print 'Header: ' ,header
#      self.path = header[11:] #Get rif of http. HTTPS will break this, must find better way
      temp = header.find('/')
      temp += 2 #Gets the position of x in http://x
      self.path = header[temp:]
      self.code = header[:temp] #Get the http request type. Need to see if "connect" packet
      i = self.code.find(' ')
      self.code = self.code[:i]
      #print self.code
      url = self.path.find('/')
      protocal = self.path.find(' ')
      self.host = self.path[:url] #Finaly split into host and path
      self.path = self.path[url:protocal]
      print 'Host ', self.host
      print 'ID ', self.id
      print 'Path ', self.path
      return header 

  
   # Get client request
   def get_request (self):
      buffer = ''
      while 1:
         data = self.connection.recv(BUF)
         if len(data) < BUF: break #Assumption: If the buffer is not full then the client has run out of stuff to send. Avoids needless waiting
         buffer = buffer + data
#The last chunk sent will cause the if to fail, so we need to append the data.
      buffer = buffer + data
      print 'Got request'
      return buffer

   def handle_else(self):
#Not really sure if its better to pass or just use class vars. Python is so loose it makes no diff
#Connect first
      print 'Connecting'
      port_index = self.host.find(':')
      if port_index == -1:
         self.port = 80
      else:
         self.port = int(self.host[port_index+1:])
         self.host = self.host[:port_index]
#print 'Doing DNS lookup'
      if self.host in dns_cache: #If host seen before dont bother doing another DNS request. Because they are very slow
      	sockaddr = dns_cache[self.host]
	family = dns_family_cache[self.host]
      	print("Using cached address")
      else:
      	(family, socktype, protocal, name, sockaddr) = socket.getaddrinfo(self.host, self.port)[0]
     	dns_cache[self.host] = sockaddr
      	dns_family_cache[self.host] = family
      print 'Sock', sockaddr
      self.destination = socket.socket(family)
      self.destination.connect(sockaddr)
     
class gui:
   def on_window_destroy(self, widget, data=None):
   	gtk.main_quit()

   def __init__( self ):
      builder = gtk.Builder()
      builder.add_from_file("main.xml")
      builder.connect_signals({ "on_window_destroy" : gtk.main_quit})
      self.window = builder.get_object("window")
      self.window.show()
  #    thread.start_new_thread(Listener, ())	
      print "Gui done"
#      gtk.main()

class Listener(threading.Thread):
   def __init__( self, s ):
      threading.Thread.__init__(self)
      self.s = s


   def run (self):   
      print "Listenin"
      #gtk.gdk.threads_init()
      while 1:
         thread.start_new_thread(ConnectionHandler, self.s.accept()+(count,))

def main():
   count = 0
   
   #Read in web cache from disk. For some reason the keys are not preserved, so this is useless
   pickle_file = open('cache.pkl', 'rb')
   web_cache = pickle.load(pickle_file)
   pickle_file.close()
  # print web_cache
   main_gui = gui()
#   gtk.main()
   s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   s.bind((HOST, PORT))
   s.listen(3)
      

  # listener = Listener(s)
   #listener.start()
  # time.sleep(2)
   #gtk.main()
   print "Done"
 #thread.start_new_thread(gui, ())
   try:
      s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#      s.setsocketopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) #Remove after testing
      s.bind((HOST,PORT))
      print 'Serving on', PORT 
      s.listen(3)
      while 1:
   #      gtk.main_iteration_do()
	 thread.start_new_thread(ConnectionHandler,s.accept()+(count,))
         count += 1
   except KeyboardInterrupt:
      print '^C Caught'

if __name__ == '__main__':
   main()
     

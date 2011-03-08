import sys, string
import socket, thread, select
import cPickle
import pickle
import gtk
HOST = ''
PORT = 8080
BUF = 4098

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
      self.request = self.get_request()
      self.original = self.request
   
      self.header = self.parse_request(self.request)
      if self.host in blacklist:
      	 self.post_blacklisted()
      elif self.host + self.path in web_cache:
         print "Address seen, using cached page", self.host
	 cache = web_cache[self.host + self.path]
	 for i in cache:
	    if i[1] == 'c':
               self.connection.send(i[0])
	    else:
	       self.destination.send(i[0])
      else:
         if self.code == 'CONNECT':
            self.handle_connect()
         else:
            self.handle_else()
#packet = ('%s %s\n'% (self.header, self.request)) #No need to reconstruct pacet
#     print '\nSending :' , packet
            self.destination.send(self.original)
#self.destination.send(packet)
            self.response_select()
            print 'Response gotten'
            self.connection.close()
            self.destination.close()
            print 'Sockets closed ', self.id

   def handle_connect(self):
      self.handle_else()
      self.connection.send('HTTPVER/1.1 200 Connection established\n')

   def post_blacklisted(self):
	print "Blaklisted address"
   
   
   def response (self):
#     print 'Awaiting response'
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

   def response_select (self):
      print 'Awaiting response'
      buffer = ''
      sockets = [self.connection, self.destination] #Doing this the fancy pants select() way
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
	       cache.append((data, temp))
	       out.send(data)
               count = 0
               print 'Sent'
               end = '204'
               temp = data.find(end, 0, 12)
               if temp > 0:
                  breakout = 1
         if count == 10: #Sort of a timeout
            break
      web_cache[self.host + self.path] = cache
      print "Attempting to write ", self.host + self.path
      output = open('cache.pkl', 'wb')
      pickle.dump(web_cache, output)
      output.close()
      
      print 'Sentall'


#connection.close()
   def parse_request (self, request):
      print 'Parsing request'
      header_delm = self.request.find('\n')
      header = self.request[:header_delm]
      self.request = self.request[header_delm:]
      print 'Header: ' ,header
#      self.path = header[11:] #Get rif of http. HTTPS will break this, must find better way
      temp = header.find('/')
      temp += 2
      self.path = header[temp:]
      self.code = header[:temp]
      i = self.code.find(' ')
      self.code = self.code[:i]
      print self.code
      url = self.path.find('/')
      protocal = self.path.find(' ')
      self.host = self.path[:url]
      self.path = self.path[url:protocal]
      print 'Host ', self.host
      print 'ID ', self.id
      print 'Path ', self.path
      return header 

  
   def get_request (self):
      buffer = ''
      while 1:
         data = self.connection.recv(BUF)
         if len(data) < BUF: break #Assumption: If the buffer is not full then the client has run out of stuff to send
         buffer = buffer + data
# print 'Data :', data
#The last chunk sent will cause the if to fail
      buffer = buffer + data
#     print buffer
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
      if self.host in dns_cache: #If host not seen before
      	sockaddr = dns_cache[self.host]
	family = dns_family_cache[self.host]
      	print("Using cached address")
      else:
      	(family, socktype, protocal, name, sockaddr) = socket.getaddrinfo(self.host, self.port)[0]
     	dns_cache[self.host] = sockaddr
      	dns_family_cache[self.host] = family
      print 'Sock', sockaddr
      self.destination = socket.socket(family)
#self.destination.setblocking(0)
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


def main():
   count = 0
   pickle_file = open('cache.pkl', 'rb')
   web_cache = pickle.load(pickle_file)
   pickle_file.close()
   #print web_cache
   main_gui = gui()
   gtk.main()
   try:
      s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#      s.setsocketopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) #Remove after testing
      s.bind((HOST,string.atoi(sys.argv[1])))
      print 'Serving on', sys.argv[1]
      s.listen(3)
      while 1:
   #      gtk.main_iteration_do()
	 thread.start_new_thread(ConnectionHandler,s.accept()+(count,))
         count += 1
   except KeyboardInterrupt:
      print '^C Caught'

if __name__ == '__main__':
   main()
     

import socket, thread, select
BUF = 4096
class ConnectionHandler:
   def __init__(self, connection, addr, timeout):
      self.client = connection
      self.buffer = ''
      self.timeout = timeout
      self.method, self.path, self.protocol = self.get_header()
      if self.method == 'CONNECT':
         self.connect_()
      else:
         self.forward()
      self.client.close()
      self.destination.close()

   #push traffic through to its destination
   def forward(self):
      self.path = self.path[7:] #Get rid of "http://"
      url = self.path.find('/') #Split URL up into site and file
      site = self.path[:url] #www.example.com
      file = self.path[url:] #/dir/subdir/resource.html
      self.connect_dest(site)
      #Reconstruct the packet and send it to the original destination
      send_ = ('%s %s %s\n'%(self.method, self.path, self.protocol)+self.buffer) 
      print 'Sending ' + send_ 
      self.destination.send(send_) 
      self.buffer = ''
      self.response()

   def connect_(self):
         self.connect_dest(self.path)
         self.client.send('HTTPVER/1.1 200 Connection established\n')
         self.buffer = ''
         self.response()

   def connect_dest(self, site):
      port_index = site.find(':')
      if port_index == -1:
         port = 80 #Presume port 80 if not specfied
      else:
         port = int(site[port_index+1:])
         site = site[:port_index]
      (family, socktype, proto, canonname, sockaddr) =  socket.getaddrinfo(site, port)[0] #I have no idea why this returns 5 tuples. Because I'm leaving out 3 arguments? Python is strange
      self.destination = socket.socket(family)
      self.destination.connect(sockaddr)
      
   def response(self):
      print 'Waiting...'
      sockets = [self.client, self.destination]
      count = 0
      while 1:
         count += 1
         print 'Reading... ' 
#print sockets[0] 
#        print sockets[1]
         (recv, send, error) = select.select(sockets, [], sockets, 30) #Wait on channel
         print 'Got'
         print recv
         if error:
            break
         if recv:
            print 'recv'
            for i in recv:
               data = i.recv(BUF)
               print 'Data ' + data
               if i is self.client:
                  out = self.destination
               else:
                  out = self.client
               if data:
                  out.send(data)
                  count = 0
         if count == self.timeout:
            break

   #Returns a tuple containing "Request type" "Resource" "Version"
   def get_header(self):
      while 1:
         self.buffer += self.client.recv(BUF)
         end = self.buffer.find('\n') # get position of end marker
         if end != -1: #Keeping going until end of transmission
            break

      print '%s'%self.buffer[:end+1]
      data = (self.buffer[:end+1]).split()#Split request up (using whitespace) 
      self.buffer = self.buffer[end+1] #Remove the request from the buffer, leaving additional data
      return data


def main():
   try:
      s = socket.socket(socket.AF_INET)
      s.bind(('', 8080))
      print "Serving..."
      s.listen(0)
      while 1:
         thread.start_new_thread(ConnectionHandler, s.accept()+(60,)) #60 second timeout. socket.accept returns a tuple, add the timeout into that tuple
   except KeyboardInterrupt:
      print '^C caught'
      s.close()

if __name__ == '__main__':
   main()






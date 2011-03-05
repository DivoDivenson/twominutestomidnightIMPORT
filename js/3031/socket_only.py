import sys, string
import socket, thread, select

HOST = ''
PORT = 8080
BUF = 4098

class ConnectionHandler:
   
   def __init__(self , connection, addr, id):
      self.connection = connection
      self.id = id
      print 'Thread ', id
      print 'Connected ' , addr
      self.request = self.get_request()
      self.original = self.request
      self.header = self.parse_request(self.request)
      self.handle_connect()
#packet = ('%s %s\n'% (self.header, self.request)) #No need to reconstruct pacet
#     print '\nSending :' , packet
      self.destination.send(self.original)
#self.destination.send(packet)
      self.response_select()
      print 'Response gotten'
      self.connection.close()
      self.destination.close()
      print 'Sockets closed ', self.id

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
            else:
               out = self.connection
            if data:
#How to stop? Try looking for a 204 aswell as a null buffer
               print 'Sending...'
#              print repr(data)
               out.send(data)
               count = 0
               print 'Sent'
               end = '204'
               temp = data.find(end, 0, 12)
               if temp > 0:
                  breakout = 1
         if count == 10: #Sort of a timeout
            break
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

   def handle_connect(self):
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
      (family, socktype, protocal, name, sockaddr) = socket.getaddrinfo(self.host, self.port)[0]
      print 'Sock', sockaddr
      self.destination = socket.socket(family)
#self.destination.setblocking(0)
      self.destination.connect(sockaddr)
      

def main():
   count = 0
   try:
      s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#      s.setsocketopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) #Remove after testing
      s.bind((HOST,string.atoi(sys.argv[1])))
      print 'Serving on', sys.argv[1]
      s.listen(3)
      while 1:
         thread.start_new_thread(ConnectionHandler,s.accept()+(count,))
         count += 1
   except KeyboardInterrupt:
      print '^C Caught'

if __name__ == '__main__':
   main()
     

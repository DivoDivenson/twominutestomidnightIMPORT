import string, cgi, time
from os import curdir, sep
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer

class MyHandler(BaseHTTPRequestHandler):

	def do_GET(self):
		try:
			if self.path.endswith(".html"):
				print self.end_headers()
				f = open(curdir + sep + self.path)
				self.send_response(200)
				self.send_header('Content-type',	'text/html')
				self.end_headers()
				self.wfile.write(f.read())
				f.close()
				return
			return
		except IOError:
			self.send_error(404,'File not found :( %s' % self.path)

	def do_POST(self):
		global rootnode
		try:
			ctype, pdict = cgi.parse_header(self.headers.getheader('content-type'))
			if ctype == 'multipart/form-data':
				query = cgi.parse_multipart(self.rfile, pdict)
			self.send_request(301)

			self.end_headers()
			upfilecontent = query.get('upfile')
			print "filecontent", upfilcontent[0]
			self.wfile.write("<HTML>POST OK.<BR><BR>");
			self.wfile.write(upfilecontent[0]);

		except:
			pass

def main():
	try:
		server = HTTPServer(('', 8080), MyHandler)
		print 'started httpserver...'
		server.serve_forever()
	except KeyboardInterrupt:
		print '^C caught'
		server.socket.close()

if __name__ == '__main__':
	main()

import smtpd
import asyncore

class CustomServer(smtpd.SMTPServer):
	def process_message(self, sender, sender_addr, rec, data):
		print "Saving message addressed to ", rec
		mails = "./mail/%s" %rec
		f = open(mails, 'w')
		print f
		print "From ", sender_addr
		f.write(sender_addr)
		print "To ", rec
		f.write(rec[0]) #Assume only one receiver for the moment
		print "Data ", repr(data)
		f.write(data)
		f.close
		return

try:
	server = CustomServer(('127.0.0.1', 1026), None)

	print("Serving...")
	asyncore.loop()
except KeyboardInterrupt:
	print "Interupt caught"
	server.close()	



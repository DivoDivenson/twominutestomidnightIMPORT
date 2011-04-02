import smtpd
import asyncore

class CustomServer(smtpd.SMTPServer):
	def process_message(self, sender, sender_addr, rec, data):
		print("From: \t", sender)
		print("From: \t", sender_addr)
		print("To:\t", rec)
		print("Body:\t", data)
		return

try:
	server = CustomServer(('127.0.0.1', 1025), None)

	print("Serving...")
	asyncore.loop()
except KeyboardInterrupt:
	print "Interupt caught"
	server.close()	



import smtplib
import email.utils
from email.mime.text import MIMEText

msg = MIMEText("Test body")
msg["To"] = email.utils.formataddr(("Recipient", "assign2.test@gmail.com"))
msg["From"] = email.utils.formataddr(("Author", "divoapple@gmail.com"))
msg["Subject"] = "Simple test"

server = smtplib.SMTP('127.0.0.1', 1025)
server.set_debuglevel(True)
try:
	#Me, them, msg
	server.sendmail("divoapple@gmail.com", ["assign2.test@gmail.com"], msg.as_string())
finally:
	server.quit()

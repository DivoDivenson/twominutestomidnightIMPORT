
import smtplib

from email.MIMEText import MIMEText

def main():

	fp = open(textfile, 'rb')
	msg = MIMEText(fp.read())
	fp.close

	msg['Subject'] = 'Sending : %s' % textfile
	msg['From'] =  'divines@tcd.ie'
	msg['To'] = 'divoapple@gmail.com'

	s = smtplib.SMTP()
	s.sendmail(me, [you], msg.as_string())
	s.quit()

if __name__ == '__main__':
	main()

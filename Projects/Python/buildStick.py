#!/usr/bin/python
#Should be /dev/input/js0, run cat to find out
#Messages seem to be 8 bytes, first two bytes (msb) are shite, rest seems like
#valid data

#http://www.stealth-x.com/programming/driver-writing-with-python.php 
import sys
import time
import os
pipe = open('/dev/input/js0','r')
action = []
spacing = 0
i = 0
#Get rid of setup crap
while i < 56:
	pipe.read(1)
	i += 1

print 'Init done'
moving = 0
while 1:
		for c in pipe.read(1):		
			action += ['%02X'% ord(c)] #convert to int and print hex
			if len(action) == 8:
				num = int(action[5], 16) #Translate back to int, use for joystick movment
#following for buttons
				print num
				if num != 0:
					moving = 1
				elif num == 0 and moving == 1:
					print 'Build\n'
					moving = 0
				action = []
				#os.system('ls')
					
'''
			if action[5] == '00': #if button press

				if action[7] == '01':
					if action[4] == '01':
						print 'Button 1 press'
					elif action[4] == '00':
						print 'Button 1 release'

				if action[7] == '02':
					if action[4] == '01':
						print 'Button 2 press'
					elif action[4] == '00':
						print 'Button 2 release'

				if action[7] == '03':
					if action[4] == '01':
						print 'Button 3 press'
					elif action[4] == '00':
						print 'Button 3 release'

				if action[7] == '00':
					if action[4] == '01':
						print 'Trigger  press'
					elif action[4] == '00':
						print 'Trigger release'
			#print action
			action = []
'''


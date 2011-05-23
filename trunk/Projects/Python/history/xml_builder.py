import re
per = 0 #Keep track of time period
million = 1000000
billion = 1000000000

def build(filename):
	f_read = open(filename, 'r')
	f_write = open(filename + ".xml", 'w')
	#f_write.write('<history title=' + filename + '>\n')
	for line in f_read:
		edit = line.find("[edit]")
		if(edit == 0): #Yet more laziness. Start of time period
			line = line[7:]
			sep = line.find(':')
			event = line[:sep]
			text = line[sep+1:len(line) -1]
	#		write_period(f_write, event, text)
		
		else:
			#Exact same logic as above, but gatting a usable date will take a little work
			sep = line.find(':')
			time = line[:sep]
			time_num = get_time(time)
			text = line[sep+1:len(line)-1]
	#		write_event(f_write, time, text)
			
	#f_write.write("</history>")
	f_read.close()
	f_write.close()

def get_time(string): #Very clunky, I'm sure there is a nice library to do this for me somewhere
	m = re.search("[0-9]+(?:(\.|,)[0-9]*)?", string)
	bonus = 1.0
	if(',' in m.group(0)):
		p = re.compile(',')
		number = p.sub('', m.group(0))
	elif('.' in m.group(0)):
		p = re.compile("\.")
		number = p.sub('', m.group(0))
		bonus = 0.1 #Account for the decimal point
	else:
		number = m.group(0)
	
	number = float(number)
	if("million" in string):
		print bonus
		bonus *=  million
	elif("billion" in string):
		bonus *= billion
	
	print number * bonus
	return number * bonus

	

def write_event(out, time, text):
	out.write("\t\t\t<event>\n")
	out.write("\t\t\t\t<time>" + time + "</time>\n")
	out.write("\t\t\t\t<text>" + text + "</text>\n")
	out.write("\t\t\t<event>\n")

def write_period(out, event, text):
	global per
	if(per != 0):
		out.write("\t\t</events>\n")
		out.write("\t</period>\n")
	
	per = per +  1
	out.write("\t<period>\n")
	out.write("\t\t<name>" + event + "</name>\n")
	out.write("\t\t<text>" + text + "</text>\n")
	out.write("\t\t<events>\n")


if __name__ == '__main__':
	from sys import argv
	if(argv[1]):
		build(argv[1])
	else:
		print "Need file name"
	

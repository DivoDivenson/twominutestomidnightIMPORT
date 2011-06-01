
f_read = open("middleages", 'r')
f_write = open("middleages_proper", 'w')

temp = ""
flag = 0

for line in f_read:
	if("[edit]" in line):
		f_write.write(line)
	else:
		if(flag == 0):
			temp = line[:len(line)-1]
			flag = 1
		else:
			linef = temp + ":" + line
			flag = 0
			f_write.write( linef)


f_write.close()
f_read.close()	

import re
import math

L = 0
N = 0
K = 0

def open_instructions(file):
#	file = open('instructions.txt')
	temp = file.readline()
	p = re.compile('\d+')
	print temp
	m = p.findall(temp)
	global L
	L = int(m[0])
	global N
	N = int(m[1])
	global K
	K = int(m[2])

def parse_ins(file, set_mask, offset, set_size):
	tlb = [-1] * (set_size + offset +1)
	for line in file:
		if line == '\n':
			break
		print line[:4], #[:4] Lazy way to get rid of the \n
		line = int(line, 16) #Convert string to value it represents
		set_= set_mask & line
		set_ = set_ >> offset
		tag = line >> offset + set_size
		print "Set: ", set_,
		if tlb[set_] == tag:
			print "Hit ",
		else:
			tlb[set_] = tag
			print "Miss ",
		print ""	

def main():
#Open instruction file and read in L, N, K
	file = open('instructions.txt')
	open_instructions(file)
	print "L=", L, " N=" , N, " K=", K
	offset = int(math.log(L,2))
	print "Offset ", offset
	set_ = int(math.log(N,2))
	print "Set ", set_
	mask = 1
	temp = 0
	while temp < set_ -1:
		mask = mask << 1	
		mask = mask +1
		temp = temp + 1
	mask = mask << offset
	print "Mask is ", mask
	parse_ins(file, mask, offset, set_)


	file.close()


if __name__ == '__main__':
	main()

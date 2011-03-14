import re
import math

L = 0
N = 0
K = 0

#Not really a stack :)
class Stackish:
	
	def __init__(self, size, k):
		self.size = size
		self.k = k
		self.items = [[-1] * (k + 1)] * (size  +1)
		#self.item = [-1] * (size + 1)
	def contains(self, item, tag):
		print self.items[item]
		if tag in self.items[item]:
			return True
	 	else:
			self.items[item].insert(0, tag)
			self.items[item][self.k] = -1
			return False


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

def parse_ins(file, set_mask, offset, set_size, stackish):
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
		if stackish.contains(set_, tag):
			print "Hit ",
		else:
			print "Miss ",
		#	tlb[set_] = tag
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
	stack_kinda = Stackish(set_+offset, K)
	mask = 1
	temp = 0
	while temp < set_ -1:
		mask = mask << 1	
		mask = mask +1
		temp = temp + 1
	mask = mask << offset
	print "Mask is ", mask
	parse_ins(file, mask, offset, set_, stack_kinda)


	file.close()


if __name__ == '__main__':
	main()

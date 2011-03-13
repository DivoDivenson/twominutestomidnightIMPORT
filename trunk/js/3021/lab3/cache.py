import re

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



def main():
#Open instruction file and read in L, N, K
	file = open('instructions.txt')
	open_instructions(file)
	print "L=", L, " N=" , N, " K=", K
	offset = 


if __name__ == '__main__':
	main()

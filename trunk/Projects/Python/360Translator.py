import sys
pipe = open('/dev/input/js0')
while 1:
	for c in pipe.read(1):
		sys.stdout.write(repr(c))
		sys.stdout.flush()

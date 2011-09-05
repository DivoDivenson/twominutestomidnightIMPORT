import sys, time
sys.stdout.write("Loading....  ")
sys.stdout.flush()
round = 0
type = 0
while round != 15:
        if type == 0: sys.stdout.write("\b/")
        if type == 1: sys.stdout.write("\b-")
        if type == 2: sys.stdout.write("\b\\")
        if type == 3: sys.stdout.write("\b|")
        type += 1
        round += 1
        if type == 4: type = 0
        sys.stdout.flush()
        time.sleep(0.2)
print "\b\b done!"


CC = g++
CFLAGS = -Wall
PROG = output
SRCS = Lab4.cpp model3DS.cpp textureBMP.cpp textureTGA.cpp  

ifeq ($(shell uname),Darwin)
	LIBS = -framework OpenGL -framework GLUT
else
	LIBS = -lglut -lGLU
endif

all: $(PROG)

$(PROG):	$(SRCS)
	$(CC) $(CFLAGS) -o $(PROG) $(SRCS) $(LIBS)

clean:
	rm -f $(PROG)

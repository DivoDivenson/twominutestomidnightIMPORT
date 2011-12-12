CC = g++
CFLAGS = -Wall -O3
PROG = output
SRCS = game_basic.cpp model3DS.cpp textureTGA.cpp hit_box.cpp  text3d.cpp

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

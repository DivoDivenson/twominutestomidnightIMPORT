CC=g++
CFLAGS=-c -Wall -ansi -fopenmp
LDFLAGS= -O3 -msse -fopenmp
SOURCES=main.cpp Screen.cpp
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=mandelbrot
LIBS=-lSDL

all: 
	$(CC) $(LDFLAGS) $(SOURCES) $(LIBS) -o mandelbrot
	
$(EXECUTABLE): $(OBJECTS) 
	$(CC) $(LDFLAGS) $(OBJECTS) $(LIBS)  -o $@

.cpp.o:
	$(CC) $(CFLAGS)  $< -o $@

screen:
	$(CC) $(LDFLAGS) $(SOURCES) -DSCREEN $(LIBS) -o mandelbrot

noomp:
	$(CC) -O3 -msse -disable-openmp $(SOURCES) -DSCREEN $(LIBS) -o mandelbrot

clean:
	rm -f *.o $(EXECUTABLE)

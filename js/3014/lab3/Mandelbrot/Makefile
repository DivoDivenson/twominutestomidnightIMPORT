CC=g++
CFLAGS=-c -Wall -ansi
LDFLAGS=
SOURCES=main.cpp Screen.cpp
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=mandelbrot
LIBS=-lSDL

all: $(SOURCES) $(EXECUTABLE)
	
$(EXECUTABLE): $(OBJECTS) 
	$(CC) $(LDFLAGS) $(OBJECTS) $(LIBS)  -o $@

.cpp.o:
	$(CC) $(CFLAGS) $< -o $@

screen:
	$(CC) $(SOURCES) -DSCREEN $(LIBS) -o mandelbrot

clean:
	rm -f *.o $(EXECUTABLE)

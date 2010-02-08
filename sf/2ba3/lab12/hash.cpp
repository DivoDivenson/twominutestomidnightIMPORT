#include <iostream>
using namespace std;

class intListElement {
public:
  int num;
  intListElement * next;
};

class intHashTable {
private:
  int size;
  intListElement ** table;
public:
  intHashTable(int size);  // construct a new hash table with size elements
  ~intHashTable();	   // delete the memory for all internal components
  void insert(int num);    // insert num into the hash table, no effect
			   // if num is already in table
  void remove(int num);    // remove num from the hash table, no effect if not in table
  int lookup(int num);     // return 1 if num is already in table, 0 otherwise
  void print(void);        // print the elements of the hash table to the screen
};

// construct a new hash table with nelements elements
intHashTable::intHashTable(int nelements)
{
  size = nelements;
  table = new intListElement*[size];
  for ( int i = 0; i < size; i++ ) {
    table[i] = NULL;
  }
}

void insert(int num){
   cout << "Hello\n";
}

// .... and so on.....


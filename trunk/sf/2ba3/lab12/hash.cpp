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
  int getKey(int data);
public:
  intHashTable(int size);  // construct a new hash table with size elements
  ~intHashTable();	   // delete the memory for all internal components
  int insert(int num);    // insert num into the hash table, no effect
			   // if num is already in table. Return 1 if failed, 0 if success
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

int intHashTable::insert(int num){
  int key = getKey(num);
  if(lookup(key)){
   return(1); 
  }
  intListElement * tempElement = new intListElement;
  tempElement = this->table[key];
  while(tempElement->next != NULL){
    tempElement = tempElement->next;
  }
  tempElement->next = new intListElement;
  tempElement->next->num = num;
  return(0);

}

int intHashTable::lookup(int num){
  int key = getKey(num);
  intListElement * tempElement = new intListElement;
  tempElement = this->table[key];
  while(tempElement != NULL){
    if(table[key]->num == num){
      return(1);
    }
    tempElement = tempElement->next;

  }
  return(0);
}

int intHashTable::getKey(int data){
  return (data % this->size);
}

int main(){
  
  intHashTable * table = new intHashTable(10);
  table->insert(12);

  
  return(0);
}

// .... and so on.....


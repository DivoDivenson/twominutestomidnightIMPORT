//Maybe implement count later if arsed

#include <iostream>
using namespace std;  //Don't do this for some reason

class intListElement {
public:
  int num;
  int count;
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

intHashTable::~intHashTable(){
  delete[] table; //Have to delete arrays like this. delete(table) deletes only the first element
}

int intHashTable::insert(int num){
 
  int key = getKey(num);
  intListElement * tempElement;// = new intListElement;
  tempElement = table[key];

  if(lookup(num)){
    while(tempElement != NULL){
      if(tempElement->num == num){
        tempElement->count++;
      }
      tempElement = tempElement->next;
    }
  }else{
    if(tempElement != NULL){
      while(tempElement->next != NULL){
        tempElement = tempElement->next;
      }
        tempElement->next = new intListElement;
        tempElement->next->num = num;
        tempElement->next->count = 1;
    }else{ //If element is first in that position
      //tempElement = new intListElement;  //Had some trouble with pointers here, now it seems to work.
      //tempElement->num = num;
      this->table[key] = new intListElement;
      this->table[key]->num = num;
      this->table[key]->count = 1;
    }
      
  }
    return(0);

}

int intHashTable::lookup(int num){
  int key = getKey(num);
  intListElement * tempElement;// = new intListElement;
  tempElement = this->table[key];
  while(tempElement != NULL){
    if(tempElement->num == num){
      return(tempElement->count);
    }
    tempElement = tempElement->next;

  }
  return(0);
}

void intHashTable::remove(int num){
   int key = getKey(num);
   if(lookup(num)){
      intListElement * tempElement = this->table[key];
      if(this->table[key]->num == num){  //If element to delete is first
        if(this->table[key]->count > 1){
          this->table[key]->count--;
          return;
        }else{
          delete(table[key]);
          this->table[key] = table[key]->next;
          return;
        }
      }
      //If element is in middle of list
      intListElement *prevElement;
      prevElement = this->table[key];
      tempElement = tempElement->next;
      while(tempElement->next != NULL){
         if(tempElement->num ==num){
           if(tempElement->count > 1){
              tempElement->count--;
              return;
           }else{
              prevElement->next = tempElement->next;
              delete(tempElement);
              return;
           }
         }
         tempElement = tempElement->next;
         prevElement = prevElement->next;
      }
      //If element is at the end of the list
      if(tempElement->count > 1){
        tempElement->count--;
      }else{
        prevElement->next = NULL;
        delete(tempElement);
      }

   }
}

void intHashTable::print(){
   int index = 0;
   cout << "Index\tValue : Count\n";
   while(index < this->size){
      if(this->table[index] == NULL){
         index++;
      }else{
         cout << index << "\t " <<table[index]->num << " : " << table[index]->count;
         if(table[index]->next != NULL){
            intListElement * tempElement;
            tempElement = table[index]->next;
            while(tempElement != NULL){
               cout << " -> " << tempElement->num << " : " << tempElement->count;
               tempElement = tempElement->next;
            }
         }
         index++;
         cout << "\n";
      }
   }
}

int intHashTable::getKey(int data){
  return (data % this->size);
}

int main(){
  
  intHashTable * hashTable = new intHashTable(2);
  hashTable->insert(123);
  //cout << hashTable->lookup(12);
  hashTable->insert(24);
  hashTable->insert(24);
  //cout << hashTable->lookup(12);
  hashTable->print();
  hashTable->remove(24);
  hashTable->remove(12);
  hashTable->print();

  
  return(0);
}



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
 
  if(lookup(num)){
   return(1); 
  }
  int key = getKey(num);
  intListElement * tempElement;// = new intListElement;
  tempElement = this->table[key];

  if(tempElement != NULL){
     while(tempElement->next != NULL){
       tempElement = tempElement->next;
     }
       tempElement->next = new intListElement;
       tempElement->next->num = num;
  }else{ //If element is first in that position
     //tempElement = new intListElement;  //Had some trouble with pointers here, now it seems to work.
     //tempElement->num = num;
     this->table[key] = new intListElement;
     this->table[key]->num = num;
      
  }
    return(0);

}

int intHashTable::lookup(int num){
  int key = getKey(num);
  intListElement * tempElement;// = new intListElement;
  tempElement = this->table[key];
  while(tempElement != NULL){
    if(tempElement->num == num){
      return(1);
    }
    tempElement = tempElement->next;

  }
  return(0);
}

void intHashTable::remove(int num){
   int key = getKey(num);
   if(lookup(num)){
      intListElement *tempElement,*prevElement;
      tempElement = this->table[key];
      prevElement = this->table[key];
      //Should have a fancy while to do this, but meh
/*      if(tempElement->num = num){ //Start of list
         table[key] = tempElement->next;
         return;
      }else{
         tempElement = tempElement->next;
         while(tempElement != NULL){
            if(tempElement->num = num){
               prevElement->next = tempElement->next;
               delete(tempElement);
               return;
            }
            tempElement = tempElement->next;
            prevElement = prevElement->next;
         }
      }
   }else{
      return;
   } KEEPS REMOVING FIRST ELEMENT*/ 
}

void intHashTable::print(){
   int index = 0;
   cout << "Index\tValues\n";
   while(index < this->size){
      if(this->table[index] == NULL){
         index++;
      }else{
         cout << index << "\t " <<table[index]->num;
         if(table[index]->next != NULL){
            intListElement * tempElement;
            tempElement = table[index]->next;
            while(tempElement != NULL){
               cout << " -> " << tempElement->num;
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
  hashTable->insert(12);
  //cout << hashTable->lookup(12);
  hashTable->insert(22);
  hashTable->insert(24);
  //cout << hashTable->lookup(12);
  hashTable->print();
  hashTable->remove(24);
  hashTable->print();

  
  return(0);
}

// .... and so on.....


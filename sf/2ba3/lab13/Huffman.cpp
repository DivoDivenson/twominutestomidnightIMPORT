#include <iostream>
#include <fstream>
#include <string>
#include <typeinfo>

using std::cout;

class node{
  public:  //Simple containers. Use public instead of setter?
    int freq;
    bool parsed;
    virtual bool getType() {} //true == compund_node. Yes, this is a hack
};

class compound_node : public node{
   public:
      node * left;
      node * right;
      bool getType(){
        return true;
      }
};

class simple_node : public node{
   public:
      char c;
      bool getType(){
        return false;
      }
};

class Huffman{
  private:
     int size;
     node ** table;
     node * merge(node * node1, node * node2);
     void genTree();
     void sortTable(int left, int right);
  public:
    Huffman();
    void print(); //Debug method
    //int * genEncoding();

};

void Huffman::print(){
   int x = 0;
   for(int i = 0; i < size; i++){
      cout << table[i]->freq;
      x += table[i]->freq;
   }
   cout << "\n" << x << "\n";

}

/*int * Huffman::genEncoding(){
  int * result;
  int sz = size;
  int encoding;
  result = new int;//[sz--];
  node * tempNode = table[0];
  node * top = table[0];
  node * prevNode = table[0];
  for(int i = 0; i <= sz; i++){
    while((tempNode->getType())){
      if(prevNode->parsed){
        //tempNode->parsed = true;
        tempNode = prevNode;
        tempNode = ((compound_node*)tempNode)->right;
        encoding++;
      }else{
        tempNode = ((compound_node*)tempNode)->left;
      }
    }
      cout << tempNode->freq << "\n";
      tempNode = ((compound_node*)tempNode)->left;
      cout << tempNode->freq;
    prevNode->parsed = true;
    tempNode->parsed = true;
    cout << ((simple_node*)tempNode)->c;
    tempNode = table[0];
    //cout << tempNode->freq;
    prevNode = table[0];
    result[i] = encoding;
    cout << result[i];
  }


  return result;
  
}*/
Huffman::Huffman(){
   size = 256; //Full ascii character set
   table = new node*[size];
   for(int i = 0; i < size; i++){
      table[i] = new simple_node;
      table[i]->freq = 1;
      ((simple_node*)table[i])->c = (char) i;
      table[i]->parsed = false;
   }

   std::ifstream file;
   file.open("input.txt");
   char c;
   while((c = file.get()) != -1){
      //cout <<  c << " ";
      table[(int) c]->freq++;
   }
   file.close();
   print();
   //Generate the tree here
   genTree();
   cout << ((compound_node*) table[0])->freq;
   //genEncoding();
}
/* Sort the array,
 * the first two nodes in the array are of lowest freq, so take
 * these and merge them into a compund node.
 * Then write this new node into position 0 and the 
 * value at the end of the array into position 1. Then
 * null the last element. Go until one node remains in the array */

void Huffman::genTree(){
   int sz = size;
   sz -= 1; //Compensate for array
   while(table[1] != NULL){
      sortTable(0, sz);
      table[0] = merge(table[0], table[1]);
      table[1] = table[sz];
      table[sz--] = NULL;
   }
}
//node1 <= node2
node * Huffman::merge(node * node1, node * node2){
   compound_node * result = new compound_node();
   result->parsed = false;
   result->left = node1;
   result->right = node2;
   result->freq = node1->freq + node2->freq;
   return result;
}


//Quick sort the array. Should probably pass the array as a param
void Huffman::sortTable(int left, int right){
   int i = left, j = right;
   node * temp;
   int pivot = table[(left + right) / 2]->freq;

   while(i <= j){
      while(table[i]->freq < pivot)
         i++;
      while (table[j]->freq > pivot)
         j--;
      if( i <= j ){
         temp = table[i];
         table[i] = table[j];
         table[j] = temp;
         i++;
         j--;
      }
   }

   if(left < j)
      sortTable(left,j);
   if(i < right)
      sortTable(i, right);

}




int main(int args, char ** argv){
   Huffman * huffman = new Huffman();
   //huffman->sortTable(0,255);
   //huffman->print();
   //huffman->genTree();

}

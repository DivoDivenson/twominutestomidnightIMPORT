#include <iostream>
#include <fstream>
#include <string>

using std::cout;

class node{
  public:  //Simple containers. Use public instead of setter?
    int freq;
    virtual ~node() {}
};

class compound_node : public node{
   public:
      node * left;
      node * right;
};

class simple_node : public node{
   public:
      char c;
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
    //open_file(); To hell with it, open it in constructor

};

void Huffman::print(){
   int x = 0;
   for(int i = 0; i < size; i++){
      cout << table[i]->freq;
      x += table[i]->freq;
   }
   cout << "\n" << x << "\n";

}

Huffman::Huffman(){
   size = 256; //Full ascii character set
   table = new node*[size];
   for(int i = 0; i < size; i++){
      table[i] = new node;
      //simple_node * tempNode = static_cast<simple_node*>(table[i]);
      table[i] = dynamic_cast<simple_node*>(table[i]);
      //table[i] = tempNode;
      table[i]->freq = 1;
      table[i]->c = 'i';
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
}

void Huffman::genTree(){
   size -= 1; //Compensate for array
   while(table[1] != NULL){
      sortTable(0, size);
      table[0] = merge(table[0], table[1]);
      table[1] = table[size];
      table[size--] = NULL;
   }
}
//node1 <= node2
node * Huffman::merge(node * node1, node * node2){
   compound_node * result = new compound_node();
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

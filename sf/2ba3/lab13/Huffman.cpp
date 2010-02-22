#include <iostream>
#include <fstream>
#include <string>


class node{
  public:  //Simple containers. Use public instead of setter?
    int freq;
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
  public:
    Huffman();
    //open_file(); To hell with it, open it in constructor

};

Huffman::Huffman(){
   size = 256; //Full ascii character set
   table = new node*[size];
   for(int i = 0; i < size; i++){
      table[i] = new node;
      table[i]->freq = 1;
   }
   std::ifstream file;
   file.open("input.txt");
   char c;
   while((c = file.get()) != -1){
      //std::cout <<  c << " ";
      table[(int) c]->freq++;
   }
   file.close();
}




int main(int args, char ** argv){
   Huffman * huffman = new Huffman();

}

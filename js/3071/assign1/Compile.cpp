#include "Parser.h"
#include "Scanner.h"
#include <wchar.h>
#include <iostream>

int main(int argc, char *argv[]){
   string input;
   
   cout << "Enter values and stuff:";
   getline(cin, input);
//   wchar_t *fileName = coco_string_create(argv[1]);
   Scanner *scanner = new Scanner((unsigned char*)input.c_str(), input.length());
   Parser *parser = new Parser(scanner);
   parser->tab = new map<string, int>;
   parser->Parse();
  /* scanner = new Scanner((unsigned char*)input.c_str(), input.length());
   parser = new Parser(scanner);
   parser->Parse();*/

}


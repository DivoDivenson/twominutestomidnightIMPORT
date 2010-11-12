#include "Parser.h"
#include "Scanner.h"
#include <wchar.h>
#include <iostream>

int main(int argc, char *argv[]){
   
   cout << "Enter values and stuff:\n";
   wchar_t *fileName = coco_string_create(argv[1]);
   Scanner *scanner = new Scanner(fileName);
   //Scanner *scanner = new Scanner(stdin);
   Parser *parser = new Parser(scanner);
   parser->tab = new map<string, int>;
   parser->Parse();
  /* scanner = new Scanner((unsigned char*)input.c_str(), input.length());
   parser = new Parser(scanner);
   parser->Parse();*/

}


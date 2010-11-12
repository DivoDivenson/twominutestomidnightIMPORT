#include "Parser.h"
#include "Scanner.h"
#include <wchar.h>
#include <iostream>
#include <map>
#include <string>

int main(int argc, char *argv[]){
   
   cout << "Enter values and stuff:\n";
   /*wchar_t *fileName = coco_string_create(argv[1]);
   Scanner *scanner = new Scanner(fileName);
   //Scanner *scanner = new Scanner(stdin);
   Parser *parser = new Parser(scanner);
   parser->tab = new map<string, int>;
   int i;

  /* scanner = new Scanner((unsigned char*)input.c_str(), input.length());
   parser = new Parser(scanner);
   parser->Parse();*/

   char buffer[100];
   //string buffer; Was passing buffer.c_str() to the scanner but it was not working. No '\0', doing it the "C" way works fine.
   map<string, int> * store = new map<string, int>;
   while(fgets(buffer, 100, stdin) != NULL){
      Scanner * scanner = new Scanner((unsigned char *) buffer, strlen(buf)); 
      Parser * parser = new Parser(scanner);
      parser->tab = store;
      parser->Parse();
      delete parser;
      delete scanner;
   }

}


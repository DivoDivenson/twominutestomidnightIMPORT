#include<stdio.h>
#include "Parser.h"
#include "Scanner.h"
#include<wchar.h>

int main(int argc, char *argv[]){
   wchar_t *fileName = coco_string_create(argv[1]); //Don't even need this but what the hell.
   Scanner *scanner = new Scanner(fileName);
   Parser *parser = new Parser(scanner);
   parser->tab = new map<string, int>;
   parser->Parse();
}

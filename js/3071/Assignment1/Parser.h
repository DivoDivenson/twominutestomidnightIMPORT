

#if !defined(COCO_PARSER_H__)
#define COCO_PARSER_H__

#include<stdio.h>
#include<map>
#include<string>
using namespace std;


#include "Scanner.h"



class Errors {
public:
	int count;			// number of errors detected

	Errors();
	void SynErr(int line, int col, int n);
	void Error(int line, int col, const wchar_t *s);
	void Warning(int line, int col, const wchar_t *s);
	void Warning(const wchar_t *s);
	void Exception(const wchar_t *s);

}; // Errors

class Parser {
private:
	enum {
		_EOF=0,
		_ident=1,
		_number=2
	};
	int maxT;

	Token *dummyToken;
	int errDist;
	int minErrDist;

	void SynErr(int n);
	void Get();
	void Expect(int n);
	bool StartOf(int s);
	void ExpectWeak(int n, int follow);
	bool WeakSeparator(int n, int syFol, int repFol);

public:
	Scanner *scanner;
	Errors  *errors;

	Token *t;			// last recognized token
	Token *la;			// lookahead token

map<string, int> * tab;
  


	Parser(Scanner *scanner);
	~Parser();
	void SemErr(const wchar_t* msg);

	void Simple();
	void Stat();
	void Expr(int &n);
	void Term(int &n);
	void Factor(int &n);

	void Parse();

}; // end Parser



#endif // !defined(COCO_PARSER_H__)


/* Simple program to test new numeric classes, by making sure that
   their interface of overloaded operators is compatible with the
   interfaces of existing numeric types. The base program shows
   the test working for int and double types. Plug in other types
   to test them. The aim of the program is to expose errors in
   the interface, which will be reported by the compiler. The program
   won't tell you anything about whether your class works. */

#include <iostream>
using namespace std;

// Perform some operations on a numeric to
// test its compatability with existing
// numeric types.
template <class T>
void numberTest(T num1, T num2)
{
  // read in the numbers
  // this is really tricky to get right
  cout << "Please enter first number" << endl;
  cin >> num1;
  cout << "Please enter second number" << endl;
  cin >> num2;

  // show the operations in action
  cout << "Add: " << (num1 + num2) << endl;
  cout << "Sub: " << (num1 - num2) << endl;
  cout << "Mul: "  << (num1 * num2) << endl;
  cout << "Div: "  << (num1 / num2) << endl;
  cout << "Eq : "  << (num1 == num2) << endl;
  cout << "NEq: "  << (num1 != num2) << endl;
  cout << "GT : "  << (num1 > num2) << endl;
  cout << "LT : "  << (num1 < num2) << endl;
  cout << "GE : "  << (num1 >= num2) << endl;
  cout << "LE : "  << (num1 > num2) << endl;
}

// invoke the test
int main()
{
  double d1, d2;
  int i1, i2;

  numberTest(d1, d2);
  numberTest(i1, i2);
}

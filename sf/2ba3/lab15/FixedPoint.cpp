#include <iostream>
//#include <iomanip>
using std::cout;
using std::ostream;
using std::cin;
using std::endl;

class FixedPoint{
   private:
      int bin_point;// = 3;
      //int value;
   public:
      FixedPoint(int input);
      int value;
      FixedPoint operator+(FixedPoint other);
      friend ostream &operator<<( ostream&, FixedPoint &);
};

FixedPoint::FixedPoint(int input){
   bin_point = 3;
   value = input;

}

FixedPoint FixedPoint::operator+(FixedPoint other){
   FixedPoint result(value + other.value);
   return result;
}

ostream &operator<<( ostream &out, FixedPoint &num){
   double result = num.value;
   int count;
   for(count = 0; count < num.bin_point; count++){
      result = result / 2.0;
   }
   out << result;
   return out;
  
}

//istream &operator>>( istream &input, FixedPoint &num){
   
//}





template <class T>
void numberTest(T num1, T num2)
{
  // read in the numbers
  // this is really tricky to get right
  //cout << "Please enter first number" << endl;
  //cin >> num1;
  //cout << "Please enter second number" << endl;
  //cin >> num2;

  // show the operations in action
  cout << "Add: " << (num1 + num2) << endl;
  //cout << "Sub: " << (num1 - num2) << endl;
  //cout << "Mul: "  << (num1 * num2) << endl;
  //cout << "Div: "  << (num1 / num2) << endl;
  //cout << "Eq : "  << (num1 == num2) << endl;
  //cout << "NEq: "  << (num1 != num2) << endl;
  //cout << "GT : "  << (num1 > num2) << endl;
  //cout << "LT : "  << (num1 < num2) << endl;
  //cout << "GE : "  << (num1 >= num2) << endl;
  //cout << "LE : "  << (num1 > num2) << endl;
}

// invoke the test
int main()
{
  double d1, d2;
  int i1, i2;
  FixedPoint f1 = new FixedPoint(12);
  FixedPoint f2 = new FixedPoint(13);


  numberTest(f1, f2);
  //numberTest(d1, d2);
  //numberTest(i1, i2);
}

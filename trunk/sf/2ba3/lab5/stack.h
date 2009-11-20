struct stack{
   double *value;
   int size;
   int sp;
};

void push(struct stack * this, double item);
struct stack * newStack(int size);
double pop(struct stack * this);


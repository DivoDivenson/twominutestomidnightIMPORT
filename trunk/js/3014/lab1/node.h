/*typedef struct {
  double x;
  double y;
} point;*/


struct node{
   point cord;
   struct node * parent;
   struct node * less;
   struct node * greater;
};

//struct node * newTree(const point cities[]);


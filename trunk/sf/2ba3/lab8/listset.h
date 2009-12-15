/* Appaneds stuff to start of the list. FIFO structure. Is this win/fail? */

struct listset{
   struct listelement * current;
   struct listelement * head;
};

struct listelement{
   int value;
   struct listelement * next;
};

/* Intialize the header */
struct listset * listset_new();

/* Creats new element, inserts it to top of list */
void listset_add(struct listset * this, int item);

/*Parses list while printing out values */
void printlist(struct listset * this);

/* 1 if there, 0 if not*/
int listset_lookup(struct listset * this,int item);

/* Removes all instances of item from the list */
void listset_remove(struct listset * this, int item);

/* return the union of the two argument as a new set */
struct listset * set_union(struct listset * one, struct listset * two);

/* Return the intersection of the twp arguments as a new set */
struct listset * set_intersect(struct listset * one, struct listset * two);


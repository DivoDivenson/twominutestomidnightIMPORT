#include "listset.h"
#include <stdlib.h>
#include <stdio.h>

struct listset * listset_new(){
   struct listset * result;
   result = malloc(sizeof(struct listelement));
   result->head = NULL;
   
   return result;
   
}

void listset_add(struct listset *this, int item){
   
   struct listelement * new;
   new = malloc(sizeof(struct listelement));
   new->value = item;
   new->next = this->head;
   this->current = new;
   this->head = this->current;
}

void printlist(struct listset * this){
   while((this->current)){
      printf("%d\n",this->current->value);
      this->current = this->current->next;
   }
   this->current = this->head;  /*Reset the listset. Point current 
                                  back to the "top"(head)*/
}

int listset_lookup(struct listset * this,int item){
   int result;
   while((this->current)){
      if(this->current->value == item){
         result = 1;
      }
      this->current = this->current->next;
   }
   this->current = this->head;  //Reset the list set
   return result;
}

void listset_remove(struct listset * this, int item){ 
   struct listelement * prev = this->head;
   while(this->current){
      if(this->current->value == item){
         prev->next = this->current->next; //Unlink
         /* The following if is true if the item to be 
            removed is the head */
         if(this->current == this->head){
            this->head = this->current->next;
         }
         free(this->current);  //Free 
      }
      prev = this->current;
      this->current = this->current->next;
   }  
   
   this->current = this->head; //Reset
}

struct listset * set_union(struct listset * one, struct listset * two){
   struct listset * result = listset_new();
   while(one->current){
     listset_add(result,one->current->value); 
     one->current = one->current->next;
   }
   while(two->current){
      /* If statement would prevent duplicate elements from
         the different sets from being inserted into the result*/
      //if(!listset_lookup(result,two->current->value)){
      listset_add(result,two->current->value);
      two->current = two->current->next;
      //}
   }
   one->current = one->head;  //Reset the sets
   two->current = two->head;
   return result;
}

struct listset * set_intersect(struct listset * one, struct listset * two){
  struct listset * result = listset_new();
  struct listset * n;
  while(one->current && two->current){ 
      if(listset_lookup(one,two->current->value)){
         listset_add(result,two->current->value);
      }
      two->current = two->current->next;
      one->current = one->current->next;
  }
  one->current = one->head; //Reset the sets
  two->current = two->head;
  return result;
}


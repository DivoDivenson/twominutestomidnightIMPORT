struct listset{
   struct listelement * nextelement;
}

struct listelement{
   int value;
   struct listelement * nextelement;
}

struct listset * listset_new(){
   struct listset * result;
   result = malloc(sizeof(struct listset));

   return result;
   
}

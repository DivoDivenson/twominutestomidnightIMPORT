


#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>

#define PORT 5842
#define IP "69.55.233.89"

int main(){

   int a,b,c,d,x,sockfd;
   char answer[1024];
   struct sockaddr_in s;


   sockfd = socket(PF_INET, SOCK_STREAM, 0);

   s.sin_family = AF_INET;
   s.sin_port = htons(PORT);
   s.sin_addr.s_addr = inet_addr(IP);

   connect(sockfd, &s, sizeof(s));

}


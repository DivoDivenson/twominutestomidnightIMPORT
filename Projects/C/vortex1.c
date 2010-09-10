/*
** showip.c -- show IP addresses for a host given on the command line
*/

#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>

int main(int argc, char *argv[])
{
    struct addrinfo hints, *res, *p;
    int status;
    char ipstr[INET6_ADDRSTRLEN];

    if (argc != 2) {
        fprintf(stderr,"usage: showip hostname\n");
        return 1;
    }

    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC; // AF_INET or AF_INET6 to force version
    hints.ai_socktype = SOCK_STREAM;
      
    printf("Attempting to resolve address\n");
    if ((status = getaddrinfo(argv[1], "5842", &hints, &res)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(status));
        return 2;
    }

    printf("IP addresses for %s:\n\n", argv[1]);

    for(p = res;p != NULL; p = p->ai_next) {
        void *addr;
        char *ipver;

        // get the pointer to the address itself,
        // different fields in IPv4 and IPv6:
        if (p->ai_family == AF_INET) { // IPv4
            struct sockaddr_in *ipv4 = (struct sockaddr_in *)p->ai_addr;
            addr = &(ipv4->sin_addr);
            ipver = "IPv4";
        } else { // IPv6
            struct sockaddr_in6 *ipv6 = (struct sockaddr_in6 *)p->ai_addr;
            addr = &(ipv6->sin6_addr);
            ipver = "IPv6";
        }

        // convert the IP to a string and print it:
        inet_ntop(p->ai_family, addr, ipstr, sizeof ipstr);
        printf("  %s: %s\n", ipver, ipstr);
    }

    char * buffer;
    int * inBuf;
    inBuf = malloc(sizeof(int));
    buffer = malloc(4*sizeof(int));
    memset(&inBuf, 0, sizeof(inBuf));
    printf("Attempting to get sockfd\n");
    int sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    printf("sockfd: %d\nConnecting...\n", sockfd);

    printf("Connection: %d\n",connect(sockfd, res->ai_addr, res->ai_addrlen));
    printf(" %d\n", buffer);
    recv(sockfd, inBuf, sizeof(inBuf), 0);
    printf("Got:\n%d\n",buffer);

    printf("Connection closed?: %d\n",close(sockfd));

   
    
    


    freeaddrinfo(res); // free the linked list

    return 0;
}

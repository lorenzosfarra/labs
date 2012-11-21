/* Socket programming - Server with fork()s
 * Copyright (C) 2012 Lorenzo Sfarra <lorenzosfarra@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include "common.h"

int main(int argc, char **argv) {
  int addrlen;
  int srvsd, currentsd;
  
  struct sockaddr_in src_addr;
  struct sockaddr_in dst_addr;

  char input[BUFFERSIZE];
  char hostname[BUFFERSIZE];
  
  strcpy(hostname, "127.0.0.1");

  /* Get a socket */
  if ((srvsd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
    perror("socket");
    exit(1);
  }

  /* complete socket structure */
  //memset(&src_addr, 0, sizeof(src_addr));
  /* Get a socket: IPv4, reliable, IP PROTOCOL (you can set TCP or UDP). */
  src_addr.sin_family = AF_INET;                // IPv4 Protocol
  src_addr.sin_addr.s_addr = inet_addr(hostname);
  src_addr.sin_port = htons(6001);              // host -> network byte order

  /* Bind the socket to the port */
  if (bind(srvsd, (struct sockaddr *) &src_addr, sizeof(src_addr)) == -1) {
    perror("bind");
    exit(2);
  }

  /* listen */
  if (listen(srvsd, MAXCONNECTIONS) == -1) {
    perror("listen");
    exit(3);
  }

  /* wait for a client */
  addrlen = sizeof(dst_addr);
  
  // "for ever" listening for connections
  while (1) {
    if ((currentsd = accept(srvsd, 
                        (struct sockaddr *) &dst_addr, &addrlen)) == -1) {
      perror("accept");
      exit(4);
    }
    // Create a child process
    int pid = fork();

    if (pid < 0) {
      perror("Unable to create child process.");
      exit(5);
    } else if (pid == 0) {
      /* CHILD */
      // Close father socket, it's useless for the child
      close(srvsd);
      /* Get a message from the client */
      if (recv(currentsd, input, sizeof(input), 0) == -1) {
        perror("recv");
        exit(6);
      }
    
      printf("Client said: %s\n", input);
      
      /* Send a feedback back to the client */
      char *ack = "ACK";
      if (send(currentsd, ack, sizeof(ack), 0) == -1) {
        perror("send");
        exit(7);
      }
      exit(0);
    } else {
      // Father
      printf("Created child with PID: %d\n", pid);
      // Close the "client" socket
      close(currentsd);
    }
  }
  return 0;
}

/* Socket programming - Client
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
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include "common.h"

int main(int argc, char **argv) {
  int addrlen;
  char hostname[HOSTNAMESIZE];

  int sd;
  
  struct sockaddr_in srvip;

  char input[BUFFERSIZE];

  // SET UP hostname
  strcpy(hostname, "127.0.0.1");
  
  /* SET UP socket structure */
  srvip.sin_family = AF_INET;                   // IPv4 Protocol
  srvip.sin_addr.s_addr = inet_addr(hostname);
  srvip.sin_port = htons(6001);                 // host -> network byte order

  /* Get a socket: IPv4, reliable, IP PROTOCOL (you can set TCP or UDP). */
  if ((sd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
    perror("socket");
    exit(1);
  }

  /* Connect the socket to the host:port */
  if (connect(sd, (struct sockaddr *) &srvip, sizeof(srvip)) == -1) {
    perror("connect");
    exit(2);
  }

  /* Send a message to server:port */
  char *msg = "test msg";
  if (send(sd, msg, strlen(msg), 0) == -1) {
    perror("send");
    exit(3);
  }

  /* Get the reply from the server */
  if (recv(sd, input, sizeof(input), 0) == -1) {
    perror("recv");
    exit(4);
  }

  printf("Server: %s\n", input);

  close(sd);

  return 0;
}

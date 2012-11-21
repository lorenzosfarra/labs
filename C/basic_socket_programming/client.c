/*
 * Copyright (C) 2012 Lorenzo Sfarra <lorenzosfarra@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * Alternatively, the contents of this file may be used under the
 * GNU Lesser General Public License Version 2.1 (the "LGPL"), in
 * which case the following provisions apply instead of the ones
 * mentioned above:
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
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

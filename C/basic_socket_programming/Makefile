CLIENT := client
SERVER := server
SERVERFORK := server_fork

ALL: $(CLIENT) $(SERVER) $(SERVERFORK)

$(CLIENT): client.c
	$(CC) -o $(CLIENT) $(CLIENT).c

$(SERVER): server.c
	$(CC) -o $(SERVER) $(SERVER).c

$(SERVERFORK): server_fork.c
	$(CC) -o $(SERVERFORK) $(SERVERFORK).c

clean:
	rm -f $(CLIENT) $(SERVER) $(SERVERFORK)



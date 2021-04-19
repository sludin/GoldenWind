PLUGIN=$(shell basename `pwd`)
OBJ_DIR=target
SRC_DIR=src
JAR=$(PLUGIN)-1.0.jar
SERVER=minecraft
SERVER_DEST=/var/minecraft/plugins
SERVER_RESTART=sudo service minecraft restart

push: 
	scp $(OBJ_DIR)/$(JAR) $(SERVER):$(SERVER_DEST)

lagg: 
	ssh $(SERVER) mcrcon -p $(MCRCON_PASS) lagg check

list: 
	ssh $(SERVER) mcrcon -p $(MCRCON_PASS) list

restart: 
	ssh $(SERVER) $(SERVER_RESTART)

tail:
	ssh $(SERVER) tail -F /var/minecraft/logs/latest.log

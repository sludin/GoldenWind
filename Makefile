PLUGIN=$(shell basename `pwd`)
OBJ_DIR=target
SRC_DIR=src
JAR=$(PLUGIN)-0.2.jar
SERVER=minecraft
SERVER_DEST=/var/minecraft/plugins
SERVER_RESTART=sudo service minecraft restart

push: 
	ssh $(SERVER) mv -f $(SERVER_DEST)/$(PLUGIN)*.jar /home/ubuntu
	scp $(OBJ_DIR)/$(JAR) $(SERVER):$(SERVER_DEST)

lagg: 
	ssh $(SERVER) mcrcon -p $(MCRCON_PASS) lagg check

list: 
	ssh $(SERVER) mcrcon -p $(MCRCON_PASS) list

restart: 
	ssh $(SERVER) $(SERVER_RESTART)

tail:
	ssh $(SERVER) tail -F /var/minecraft/logs/latest.log

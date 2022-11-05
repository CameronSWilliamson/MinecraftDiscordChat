.PHONY=clean
.PHONY=compile
.PHONY=rmdb
.PHONY=deploy
.PHONY=run
.PHONY=cleanrun

build/libs/MinecraftDiscordChat.jar:
	gradle shadowJar

compile:
	gradle shadowJar

clean:
	gradle clean
	rm -rf spigot

spigot:
	bash ./setup_env.sh

run: spigot compile
	cp build/libs/* spigot/plugins/
	bash run_server.sh

doc:
	gradle javadoc
	cp -r build/docs/javadoc docs

cleanrun: clean run

rmdb:
	rm -f ./spigot/plugins/MinecraftDiscordChat/mcdc.sqlite

deploy:
	./deploy.sh
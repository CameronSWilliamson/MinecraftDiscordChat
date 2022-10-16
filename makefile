.PHONY=clean
.PHONY=compile

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

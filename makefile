.PHONY=clean
.PHONY=compile

build/libs/MinecraftDiscordChat.jar: src/java/
	gradle shadowJar

compile:
	gradle shadowJar

clean:
	gradle clean

setup:
	bash ./setup_env.sh
	bash ./run_server.sh

run: compile
	cp build/libs/* spigot/plugins/
	bash run_server.sh

doc:
	gradle javadoc

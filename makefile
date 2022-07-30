compile:
	gradle shadowJar

clean:
	gradle clean

setup:
	bash ./setup_env.sh
	bash run_server.sh

run: clean compile
	mv build/libs/* spigot/plugins/
	bash run_server.sh

doc:
	gradle javadoc

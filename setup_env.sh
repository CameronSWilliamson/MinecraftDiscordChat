#!/bin/bash

function testCopy {
    if test -f $1; then
        cp $1 $2
    fi
}

mkdir -p spigot/build
cd spigot/build
curl https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar -o BuildTools.jar
java -jar BuildTools.jar
mv spigot* ..
cd ..
echo eula=true >> eula.txt
rm -rf build
cd ..
mkdir -p spigot/plugins/MinecraftDiscordChat
cp config.yml spigot/plugins/MinecraftDiscordChat

testCopy config/config.yml spigot/plugins/MinecraftDiscordChat
testCopy config/server.properties spigot/
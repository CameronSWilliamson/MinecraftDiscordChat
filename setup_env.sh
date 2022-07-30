#!/bin/bash
mkdir -p spigot/build
cd spigot/build
curl https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar -o BuildTools.jar
java -jar BuildTools.jar
mv spigot* ..
cd ..
echo eula=true >> eula.txt
rm -rf build
cd ..

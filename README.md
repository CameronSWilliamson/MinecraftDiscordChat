# MinecraftDiscordChat

A spigot minecraft plugin that allows players to create zones on the map which are linked to discord voice channels. If player's enter the `default_voice` chat channel which is set in `config.yml` they will be moved to the voice channel of the zone they are in. If they leave the zone they will be moved to the `default_voice` channel.

Version 1.0.0 of this plugin was written during the 2022 Gonzaga University Hackathon.

## Installation

1. Download the latest release from the [Releases page](https://github.com/CameronSWilliamson/MinecraftDiscordChat/releases) on github or compile it yourself with the source code.
2. Place the jar file in the `plugins` folder of your server.
3. Start the server to allow the file at `<server folder>/plugins/MinecraftDiscordChat/config.yml` to be created. Once the file is created fill in your discord bot token (you can get one from [here](https://discord.com/developers/applications)) and the id of the default voice channel you want players to be moved to when they leave a zone. In order to get the ID of a voice channel you need to set discord to developer mode.
4. Start the server and join the voice channel.

## Compilation

To compile this repo you will need to have [gradle](https://gradle.org/) installed. Once you have gradle installed you can run `gradle build` in the root directory of the repo to compile the plugin. The compiled jar file will be located at `<repo root>/build/libs/MinecraftDiscordChat-<version>-all.jar`.

## Usage

While in game you must use the `/link <discordname>#<id>` to map your Minecraft account to your Discord account. Then while in game you can use `/voicearea <voice area name>` to create a new voice channel. Now if you are in the `default_voice` channel then as you enter different zones you will be moved to the voice channel of the zone you are in.
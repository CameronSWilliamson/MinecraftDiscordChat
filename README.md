<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <!-- <a href="https://github.com/CameronSWilliamson/MinecraftDiscordChat">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a> -->

<h3 align="center">Minecraft Discord Chat</h3>

  <p align="center">
    A Spigot plugin with a focus on Discord integration.
    <br />
    <a href="https://cameronswilliamson.github.io/MinecraftDiscordChat"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://youtu.be/1xus9lLWWhA">View Demo</a>
    ·
    <a href="https://github.com/CameronSWilliamson/MinecraftDiscordChat/issues">Report Bug</a>
    ·
    <a href="https://github.com/CameronSWilliamson/MinecraftDiscordChat/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
        <li><a href="#build-from-source">Build from source</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <!-- <li><a href="#roadmap">Roadmap</a></li> -->
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#dev-tools">Dev Tools</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<!-- [![Product Name Screen Shot][product-screenshot]](https://example.com) -->

This Spigot server plugin unifies the world of Minecraft with the complexities of a Discord server. Users can create areas in specific Minecraft servers that link to a Discord voice channel. When players enter a new area in-game, they are moved to the corresponding voice channel and return to the default voice channel after exiting. In addition, user’s chat messages in Minecraft are redirected to a discord text channel and vice versa. This allows for a more immersive and seamless experience for users of the server.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites

- Java 17
- Gradle (from source)
- Minecraft 1.19+

### Installation

1. Set up a Discord Bot at <https://discord.com/developers/applications> and invite it to your server. Save your tokenID from the api to put in the config file.
2. Create a spigot server. This can be done by running the following bash commands or by downloading the server from <https://www.spigotmc.org/>

    ```bash
    mkdir -p minecraft_server/build
    cd minecraft_server/build
    curl https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar -o BuildTools.jar
    java -jar BuildTools.jar
    mv spigot-*.jar ../spigot.jar
    cd ..
    rm -rf build
    echo eula=true > eula.txt
    ```

3. Either clone this repo and [build from source](###build-from-source) or download the latest release from [Releases](https://github.com/cameronswilliamson/minecraftDiscordchat/releases)
4. Place this file in `minecraft_server/plugins/` and open `minecraft_server/plugins/MinecraftDiscordChat/config.yml` and fill in the required fields:

    ```yaml
    api_token: "YOUR_Discord_BOT_TOKEN"
    default_category_name: "The name of the Discord category to use"
    default_server_id: "The server ID of the Discord server to use"
    # The following are optional
    default_text_channel_id: "The default text channel ID to use"
    default_voice_channel_id: "The default voice channel ID to use"
    ```

5. Run your minecraft server by running `java -jar spigot.jar` in `minecraft_server/` and join the voice channel you specified in the config file. You should see a message in the console saying that the plugin has been enabled.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Build From Source

1. Clone the repo

   ```sh
   git clone https://github.com/CameronSWilliamson/MinecraftDiscordChat.git
   ```

2. Build the jar file

   ```sh
   make compile
   ```

3. Copy the jar file to your minecraft server

   ```sh
    cp build/libs/MinecraftDiscordChat.jar /path/to/minecraft/server/plugins/
    ```

<!-- USAGE EXAMPLES -->
## Usage

Getting started with the plugin is easy. 

- Join the minecraft server and run `/link <username>#<numbers>` to link your minecraft username to your Discord username.
- In the Minecraft server you can run `/voicearea <area name>` to recieve a stick named `"AreaDefiner <area name>"`. Right click in one corner of the area you want to define as a voice area and then right click in the opposite corner. This will create a voice area with the name you specified linked to a new voice channel in Discord that players will be moved to when they enter the voice area.
- In the Minecraft server you can also run `/request <feature / bug>` to request a feature or report a bug. This will send a mesage to the developer of the plugin on Discord.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->
<!-- ## Roadmap

- [ ] Feature 1
- [ ] Feature 2
- [ ] Feature 3
    - [ ] Nested Feature

See the [open issues](https://github.com/CameronSWilliamson/MinecraftDiscordChat/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p> -->



<!-- CONTRIBUTING -->
## Contributing

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch off of the `dev` branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Dev Tools

Many development tools are included in the `Makefile` to make development easier. These include:

- `make compile` - Compiles the plugin into a jar file
- `make clean` - Cleans the build directory and removes the test server
- `make spigot` - Downloads the latest spigot server jar file and runs spigot build tools
- `make run` - Runs the plugin in a test server
- `make doc` - Generates javadoc for the plugin
- `make rmdb` - Removes the database file for the test server
- `make cleanrun` - Cleans the build directory, removes the test server, and runs the plugin in a test server


<!-- LICENSE -->
## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Cameron Williamson - [@therealkeyisme](https://twitter.com/twitter_handle) - therealkey@outlook.com

Project Link: [https://github.com/CameronSWilliamson/MinecraftDiscordChat](https://github.com/github_username/repo_name)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/CameronSWilliamson/MinecraftDiscordChat.svg?style=for-the-badge
[contributors-url]: https://github.com/CameronSWilliamson/MinecraftDiscordChat/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/CameronSWilliamson/MinecraftDiscordChat.svg?style=for-the-badge
[forks-url]: https://github.com/CameronSWilliamson/MinecraftDiscordChat/network/members
[stars-shield]: https://img.shields.io/github/stars/CameronSWilliamson/MinecraftDiscordChat.svg?style=for-the-badge
[stars-url]: https://github.com/CameronSWilliamson/MinecraftDiscordChat/stargazers
[issues-shield]: https://img.shields.io/github/issues/CameronSWilliamson/MinecraftDiscordChat.svg?style=for-the-badge
[issues-url]: https://github.com/CameronSWilliamson/MinecraftDiscordChat/issues
[license-shield]: https://img.shields.io/github/license/CameronSWilliamson/MinecraftDiscordChat.svg?style=for-the-badge
[license-url]: https://github.com/CameronSWilliamson/MinecraftDiscordChat/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/cameron-williamson-925576201

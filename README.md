Bomberman Game

This repository contains an implementation of the classic Bomberman game, featuring a client-server architecture achieved through socket programming.
Features


1- Background Music:
    Enjoy an immersive gaming experience with background music that enhances the atmosphere.

2- [save](https://github.com/pm78p/bomberman-game/blob/main/src/SaveFrame.java) and [load](https://github.com/pm78p/bomberman-game/blob/main/src/LoadFrame.java):
    Save your progress and resume gameplay later with the implemented save and load feature using JSON.

3- Multiplayer Functionality:
    Play with friends! The game is designed as a client-server application, allowing multiple clients to connect to the server and play together. Simply access the server using the appropriate IP and port.

4- Server Status Panel:
    A dedicated panel displays essential information about different working servers. Stay informed about the server's status.

5- In-Game [Communication](https://github.com/pm78p/bomberman-game/blob/main/src/ChatFrame.java):
    Communicate and strategize with other players via an in-game chat system. Players can also interact with the server, enhancing the social aspect of the gaming experience.

6- various enemies:
In this game, you are a bomberman who tries to find its way to the salvation door at the bottom right of the panel by bombing bricks and enemies. There are four different enemies, each with a unique heuristic to catch you. There is a dummy enemy who tries to catch you in a random way 🤔, the one that sometimes tries to catch you and sometimes wanders randomly, the one that tries to minimize its distance to you by calculating Manhattan distance, and the one who uses the Manhattan distance but also has no wall that could stop it. You can imagine how chaotic the world could be in higher levels because with each level you progress, the higher the percentage of more intelligent enemies the bomberman faces.

?How to run?:

1- you can easily run [this](https://github.com/pm78p/bomberman-game/blob/main/src/BoardPSingle.java) and exprience the game without any complexity.
    
2- you can easily run [this](https://github.com/pm78p/bomberman-game/blob/main/src/BoardPClient.java) and exprience the whole game.
    
3- if you want to play it on client server way, first you need to launche a [server](https://github.com/pm78p/bomberman-game/blob/main/src/BoardPServer.java), then go to the game from [client](https://github.com/pm78p/bomberman-game/blob/main/src/BoardPClient.java) and type the correct IP and port to connect to the server.

Preview

Gameplay Preview
![results](https://github.com/pm78p/bomberman-game/blob/main/src/Media5gif.gif)

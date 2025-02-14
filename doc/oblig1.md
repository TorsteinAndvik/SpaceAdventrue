# Submission 1
## A0
**Team name**: *Sprites*

**Team members + Skill set:**
* Tor Erik Wollmann: OOP in python, using git in a team enviorement
* Torstein Andvik: Gamedevelopment, knowledge of java|, UI/UX
* Flemming Bjørdal: OOP in Java, interest in games og and gamegraphics
* Yasmina Madi: organizing, visionary, quality assurance and encouragement 
* Henrik Dalstø: Experience writing in various programming languages, CI/CD

## A2 Concept 
### In a nutshell

Fly around in space and fight enemy ships, aliens and meteors. Collect XP to improve abilities and gain higher levels to upgrade your ship. Survive until the end of the game.

The player controls a spaceship travelling through space. At the start it will be a small, poor ship. By shooting down enemy ships and meteors, you will gain XP that you can use to improve the ship's characteristics. 

Parts are collected throughout the game and the ship is built by combining modules into a coherent vessel. Weapons, shields and engines can be placed on the modules. 

### Space
Large area with sporadic obstacles such as collections of rocks and lone comets that fly through space and restrict the player's movements.

A space station can be a safe harbour where you can upgrade and repair your ship or buy new parts.


### Portals
Portals open up scattered around space. They send out hostile ships or ‘aliens’. The portals will initially come from a planet/area with enemies that are easy to fight (low level), but the level of the portal gradually increases.

The portal will, if not obstructed, send out enemies until it is finished.

### The end of the game
**Options:**

* The game becomes too difficult and you give up 🙁
* A final boss fight 
* You have managed to collect all of X, on behalf of Y
* You have collected enough Z to open a portal and go home

## A3 
Our project methodology will be a juxtaposition of Kanban with TDD. In group session we will do extensive
pair programming. 

### Meetings 
We will be meeting at the seminar each week. When we get closer to the submissions deadline we will be meeting more often. 
Blom will be our creative hub.

### Communication 
Our discord server will serve as the main communication tool. We track issues with the GitLab issue tracker, and
will interface in weekly standups.

### Division of labor 
* *Tor* - Game design, tester
* *Flemming* - Render engineer, texture artist, 
* *Torstein* - Game developer, tester
* *Henrik* - CI, integration, modelling, builds, project manager(?)
* *Yasmina* - UI/UX designer, sound engineer, community manager


### Follow up 
We will work through blockers communally, and in brief coding spurts. The project 
will have reasonable deliveries, corresponding to the versions in the assignment. 

### Documents 
Documents will be shared and stored via GitLab. Diagrams and charts will be written/charted in Mermaid and similar
and will follow the repo. The codebase will obviously be maintained at GitLab, and developers will merge their feature branches to main
frequently. We will also have prod branch to maintain version history and landmarks as the project moves forward.

### Overall goal for the project
A 2D bullet-hell/bullet-heaven set in Space! 

The player starts with a small ship. They fight enemy ships to gain resources. They can then use those resources to upgrade their ship: Increase size to add more cannons, thrusters, shields, etc., or upgrade previously added parts. Upgrades are added on a screen with a grid, say a 5x5 grid where the ship to begin with only fills the middle square. Parts are added by clicking a neighbouring square on the grid, and choosing which type of part to add. Clicking an already existing ship-piece will instead allow you to upgrade that part (if more upgrades are available). Game is won when the player ship is fully (or at least sufficiently) upgraded.

The enemy ships have some form of AI controlled by the game: They fly around the player ship, and shoot at the player ship. There are different types of enemy ships, with different ways of attacking (some have simple cannons, some are kamikaze-drones, some have auto-targeting missiles, etc.). More ships spawn as time passes / possibly in waves? Possibly from portals? Possibly from off-screen?

### MVP
1. Show a game board 
2. Show the Player on the game board
3. Show enemies on the board
4. Show obstacles on the board
5. Ability to move the player (keyboard inputs) 
6. Player can shoot (keyboard inputs)
7. Friendly shots interact with enemies and obstacles
8. Player gets “points” from destroying enemies and obstacles 
9. Player can spend “points” on upgrades 
10. Start screen at game launch and game over

### User stories
User stories:
As a player I need to be able to tell my ship apart from enemy ships
Acceptance criteria: Player ship is visually distinct from enemy ships
Implementation: Use different colors in view/textures for player ship  

As a player I need to be able to tell obstacles apart from the background so that I can see how to navigate around them
Acceptance criteria:
Implementation:

As a player I want to be able to regret an incorrect upgrade
Acceptance criteria:
Implementation:

As a player with physical limitations I want to be able to remap the default keyboard controls 
Acceptance criteria:
Implementation:

As a developer I need to be able to tell ships (both friendly and enemy) apart from obstacles to determine if a collision should occur
Acceptance criteria:
Implementation:

As a developer I need to be able 
Acceptance criteria:
Implementation:


## A4 
The program compiles and works as intended. We have pair programmed.


## A5: Summary 

**Communication**

Communication within the group has been successful so far. We quickly got to know each other, started working early, clarified expectations and goals for the course, established a good dialogue, and set up a communication channel on Discord. Ideas and thoughts shared within the group are met with respect. The feedback we provide each other is constructive. So far, there have been no disagreements on the topic.


**Work Progress**

So far, we have completed the lab exercises, agreed on a game concept, and gained an overview of the mandatory requirements for the course. We use Trello, a project management tool that helps teams organize and keep track of tasks:
> https://trello.com/b/M1GIZpbu/sprites-inf112-team
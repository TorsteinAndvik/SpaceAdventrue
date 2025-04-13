# Møtereferater for gruppen: Sprites

## Referat 31.01.25 
- Alle til stedet  
- Gjennomførte Lab1 (TextAligner) i grupper på 2 og 3 stykk
- Så litt gjennom beskrivelsen av semesteroppgaven
- Satt gruppenavn til: “Sprite”
- God stemning
- Til neste gang: Gå gjennom Lab2


## Referat 14.02.25 
- Til stede: Flemming, Yasmina, Henrik, Tor
- Arbeidet med Oblig 1 Del A

## Referat 21.02.25 
- Til stede: Flemming, Torstein, Tor
- Planlegging og begynnelse på programmering av model 


## Referat 28.02.25 
- Til stede: Flemming, Torstein, Tor, Henrik
- Jobbe med å merge inn startimplementasjon av model
- Merget dette med det som er lagd av view
- Fordele programmeringsoppgaver på Trellobretteet til neste gang
#### Lage MVP til neste gang
1. Show a game board 						
2. Show the Player on the game board
3. Show enemies on the board
4. Show obstacles on the board
5. Ability to move the player (keyboard inputs) 
6. Player can shoot (keyboard inputs)
7. Friendly shots interact with enemies and obstacles
8. Start screen at game launch and game over


## Referat 07.03.25 
Til stede: Flemming, Torstein, Tor, Henrik
- Branch navngivelse (bytte til feature-branches i stedet for dev-branches)
- Diskuterer movement - ønsker orientasjonsbasert bevegelse (ikke kardinal-basert bevegelse)
- TODO: Liste med objekter som eksisterer og en enum med tilhørende typer
- TODO: Bytte fra int-basert posisjon til Vector2-basert posisjon

## Referat 14.03.25 
Til stede: Flemming
-Jobbe med implementering av å vise en beskrivelse av oppgraderingene i UpgradeScreen
-fint lite planlegging


## Referat 21.03.25 
[FØR MØTET]

Formålet med listen under er ikke at alt skal på plass i løpet av møtet, men å foreslå hvilke ting vi kan ha fokus på til neste gang for å komme et solid steg videre mot det endelige produktet. Rekkefølgen er et forslag til prioriteringsnivå (viktigste øverst).

Forslag til diskusjon / oppgaver å jobbe med / ha fokus på ved møtet + kommende uke:
Få ordnet med den åpne merge-requesten (med konflikter) fra en stund tilbake fra Tor som legger til Collideable, HitDetection etc. i Model.
Videreutvikling av disse -> objekter som er Damageable og får HP satt til 0 må også ødelegges i Model.
Få på plass at alle posisjoner og hastigheter av space objekter er float-basert
Er dette allerede på plass?
Implementer fysikkbaserte update(deltatime) metoder for alle space objekter 
Er dette allerede på plass?
Var vi enig om at vi ønsker automatisk nedbremsing av spillerskipet (“space friction”)? - få implementert dette.
Flytt rotasjon fra view til model 
Hvor bør Matrix3 og Matrix4 objektene lagres for å redusere ressursbrøk?
Ha i tankene når man implementerer update(deltaTime) at vi kan redusere antall beregninger som utføres på et SpaceShip som ikke trenger å rotere i denne oppdateringen
En metode i ShipValidator (eller ShipFactory? eller ShipStructure?) som returnerer en liste av gyldige posisjoner å plassere en ny Fuselage på, og gyldige posisjoner å plassere en ny oppgradering på (til bruk i UpgradeScreen)
Tegne spillerens faktiske skip i UpgradeScreen
Highlighte hvilke ruter som er lov å bygge videre på
Highlighte hvilke ruter som er lov å legge til en oppgradering på
Highlighte hvilke ruter som er lov å oppgradere en allerede plassert oppgradering
Implementere funksjonalitet for å legge til deler på spillerskipet basert på det spilleren gjør i UpgradeScreen

[PÅ MØTET]
Til stede: Alle.

Hver fuselage skal holde informasjon om egen posisjon og rotasjon.
Lager mange oppgaver i trello

Vi må snakke med Casper om hva kravet til nettsiden er.


## Referat 28.03.25
Oppmøte: Alle.
Diskutere release candidate:
Først og fremst la spill-loop være å dodge asteroider
fiendtlige skip med enkel AI kan være et mål for oblig4
kreve ressurser for å bygge på skipet (dev mode: infinite money $$)
å ødelegge ting gir ressurser
 - Lage startskjerm (Henrik)
 - Lage AsteroidFactory


## Referat 04.04.2025
Oppmøte : Alle

Videre arbeid:
Lage hjelpeskjerm med instruksjoner
Begrense byggemuligheter ved å la det koste ressurser
Øke test coverage til 40 % til neste fredag
Finjustere frekvens og hastighet av asteroider
Lage basic AI til EnemyShip
La masse og thruster påvirke skipets hastighet
Lage directional factory


## Referat 11.04.2025
Oppmøte: Alle

Lage flere user stories for oblig3.md
Skrive refleksjon over metodikkvalg
Rydde i read.me (hvordan spille spillet)
Oppdatere pom.xml (Henrik er på saken) *ferdig

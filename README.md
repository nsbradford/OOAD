# OOAD
### WPI CS4233 D16: Object-Oriented Analysis and Design

Nicholas S. Bradford

This is my Java 8 implementation of the Hanto game backend (no GUI), approximately 7k lines built with TDD. My code inhabits the /test and /src/hanto/studentnsbradford/ directories (OOAD/src/hanto/common and OOAD/src/hanto/tournament are pre-provided classes).

The game is played on an infinite hexagonal grid, and involves each of the two players alternating moves (either placing a piece on a board or moving its position) in an effort to surround the opponent's Butterfly piece. There are 5 different game versions (Alpha, Beta, Gamma, Delta, and Epsilon), each with slightly different rule variations. The tournament/HantoPlayer class is a simple heuristic-driven AI that can play against the AIs of other students in a final class-wide tournament.

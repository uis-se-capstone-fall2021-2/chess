# Testing the chess app

 ## JUnit

The app can be tested using Junit tests that were written to verify that game logic is working correctly.

For example, a test case would supply a pre-determined chessboard, and supply a move to `MoveValidator.validateMove(...)` and check if the value returned is correct according to chess rules. Many situations can be checked by attempting legal and illegal moves for each piece type.  
  
The app has test classes for testing `MoveValidator`, which evaluates the legality of moves according to chess rules, and for testing `BoardEvaluation`, which evaluates boards to estimate which player is winning, for use in chess AIs. 

After the tests have been run, a webpage is generated showcasing the results.

## Manual/Beta testing
---
A terminal based chess game was created to test game logic while the UI was still being developed. 

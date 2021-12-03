# Requirements

## 1. Introduction

### 1.1 Purpose of this Document

Intentionally left blank

### 1.2 Scope

See [Scope Document](./scope.md)

### 1.3 Definitions and Acronyms

For the purposes of this document, a `User` is a human who accesses the application. A `Bot` is a chess artificial intelligence (AI). A `Player` is either a `User` or a `Bot`.

### 1.4 References

- https://www.chessprogramming.org/Simplified_Evaluation_Function

### 1.5 Overview of remainder of this document

Intentionally left blank

## 2. General Description

### 2.1 Product Perspective

TODO

### 2.2 Product Functions

TODO

### 2.3 User Characteristics

A User is anyone who would like to engage in a game of chess online against another human player or an AI opponent

### 2.4 General Constraints

- host must be connected to the Internet in order use the application

### 2.5 Assumptions and Dependencies

#### Development Dependencies

See [README](../../README.md)

### Runtime Dependencies

- User must have access to a modern web browser implementing websockets

## 3. Specific Requirements

1. Users **must** be able to interact with the application via a web interface
   1. application **must** be compatible with all modern browsers
2. Multiple Users **must** be able to access the application concurrently
   1. Each User of the application **must** be a uniquely identifiable entity
   2. Each User of the application **must** have a unique, public display name
      1. User **may** be able to change their display name to another globally unique name
      2. A User's request to modify their display name **must** be accepted if there is no conflict with another user's display name
      3. A User's request to modify their display name **must** be rejected with a if the name is in use by another user
      4. A User **must not** be able to modify another user's display name
      5. A filter **should** be implemented to prohibit obscene display names (TODO)
      6. A filter **should** be implemented to prohibit extremely long display names. (TODO)
         1. A 16 character maximum is recommended
   3. Users **must** authenticate in order to access the application
      1. Users **must** authenticate via third party authentication management provider, Auth0
         1. Users **may** create an Auth0 account for our application
         2. Users **may** use their Google account
      2. Users **must** be able to log out of the application 
      3. Users **must** be able to access their auth token
3. All functions of the application **must** be available to an authenticated user via a REST API
   1. REST API **must** require authentication via the Authorization header with `bearer` scheme
   2. The application **should** expose a private Swagger UI for development testing against the API
   3. The application **may** expose a public Swagger UI
4. A Users **must** be able to engage in a game of chess via a web UI
   1. Users **must** be able to create a chess game
      1. Users **must** be able to invite another Player to a game
         1. Users **must** be able to choose from a human or bot opponent
         2. Users **must** be able to search for other human opponents by display name
         3. ~Users **must** be able to search for bot opponents by display name~
            1. ~Suggestions **should** be provided for the bot display names~
         4. UI **should** provide a pre-populated selection of available bots (TODO)
      2. Upon game creation, Users **should** be routed to the detail view of the created game
   2. Users **must** be able to view the status of pending invitations they've sent
      1. Users **must** be able to cancel a pending invitation they've sent to another user
   3. User **must** be able to view pending invitations they've received
      1. Users **must** be able to accept or decline game invitations from other users
      2. Users **may** be notified of new pending games
   4. Users **may** engage in multiple concurrent games
      1. A limit **should** be placed on the number of games a User may be active in at any given time (TODO)
         1. Users **should** not be prohibited from receiving game invites when they are at or beyond the active game limit
         2. Users **should** be prohibited from accepting game invites when they are at or beyond the active game limit
         3. Users **should** be prohibited from sending game invites when they are at or beyond the active game limit
   5. Bots **must** have the capacity to engage in multiple concurrent games
      1. Bots **must** not have an active game limit
      2. Bots **must** immediately accept game invitations
   6. Users **must** be able to view a list of their active games
      1. Users **must** be able to quit active games
         1. Quitting an active game **must** reward the User's opponent with a win
         2. User **must** not be able to quit inactive games
            1. Inactive games are defined as games with status of pending, declined, and completed, and terminated
      2. Users **must** be able to view summary details of games from the list view
         1. opponent
         2. move count
         3. player color (team)
         4. creation time
         5. last update time
         6. ~whose turn it is~
      3. Users **should** be able to download their game
         1. Game download **should** be in .pgn format
      4. Users **must** be able to access an active game from the list view
         1. Accessing an active game **must** route the User to the Game Board view
   7. The Game Board view **must** render a chess board
      1. The orientation of the board **should** have the User's team at the bottom
      2. The User **must** only be able to move their pieces on their turn
         1. An indication **must** be shown whose turn it is
         2. Moving an opponent's piece **must** result in an error
         3. Moving own piece out of turn **must** result in an error
            1. Errors **must** be displayed
      3. All moves **must** adhere to the rules of chess
         1. @bennettj12 please complete this with move validator requirements
      4. An indication **must** be shown when a player is in check
      5. Users **must** be able to resume games across UI sessions
         1. Navigating away from the Game Board view **must not** affect the game state
         2. Navigating away from the application site **must not** affect the game state 
         3. Closing the browser **must not** affect the game state
         4. User **may** engage with the same game from multiple browsing instances
   8. Game updates **must** be displayed in real time or near-realtime
   9. Users **must** only be able to interact with games they are a player in
      1.  Attempting to access an unathorized game **must** result in an error. The following constitue access:
          1.  view game
          2.  quit game
          3.  accept game invitation
          4.  decline game invitation
          5.  move (update game)
  1.  Upon game completion, User **must** be routed to the Completed Game view
      1.  Completed Game view **should** show a Game Summary (TODO)
          1.  winner
          2.  move count
          3.  option to download game
          4.  if User lost, **should** present option for rematch (sends invite to opponent)


## 4. Appendicies

Intentionally left blank




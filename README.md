# World Tournament Backend for Mobile Games

This is a World Tournament Backend implementation for mobile games.

It is implemented to satisfy the following ![requirements]().

## How to Run and Use Endpoints

Run createdb.sql file in Mysql Folder.

Set datasource values in application.properties.

Download maven dependencies.

You are free to go!

I have exported a Postman Collection v2.1 into Postman Template folder. You can import it and access to endpoints.

## Database Design

The User table includes requested fields in the requirements and additionally version field. The introduction of the version field is aimed at achieving optimistic locking, users should proceed levels and get more coins while they are already in queue. If tournament fee payment and proceeding level occur concurrently, optimistic lock error is thrown for one of them. Also, there was another field which represents rewards, but I decided to move rewards to another table. While sending rewards to user, user may proceed level and this would cause concurrency problems.

The User table establishes a OneToMany relationship with the UserRank table. To optimize performance and prevent redundant data loading, FetchType.LAZY is set for all OneToMany relations. Additionally, a custom JQuery is implemented for accessing to these relations when necessary.

The Tournament table features id and date fields and establishes a OneToMany relationship with the TournamentGroup table. Furthermore, both TournamentGroup and User tables share a OneToMany relationship with the GroupUser table, where users' tournament group and scores are stored. Finally the reward table which holds user's and their rewards.

![ER Diagram](https://github.com/gecemavisioa/multi/assets/73769340/8fade299-1d19-4225-a88b-fd628c1032df)

## Project Organization

The project structure followed the Controller -> Service -> Repository principle, with dedicated folders for each component. Two services correspond to two controllers, and these services interact with repositories as needed.

In addition to the standard controller, service, repository, and entity packages, new packages are introduced based on functional requirements, ensuring a clean and organized folder structure.

- **Config Package**: Contains configuration classes for the async task pool and local time zone.

- **DTO Package**: Contains data structures for endpoint responses, including Country Leaderboard, Group Leaderboard, and GroupRank (representing a user's historical ranks).

- **Exception Package**: Includes custom error classes and implementation of GlobalExceptionHandler @ControllerAdvice.

- **Util Package**: Helper functions for keeping code files organized. This package has 4 classes:

  - **CountryHelper**: Holds a list of five countries, providing functionality to return a country's index, a country index's for a corresponding country, and the generation of a random country during user creation. This class is used for creating user lists or country queues, eliminating the need for switch-case statements.

  - **SameScores**: Implements a function that accepts a list of GroupUser instances and returns a list of their respective ranks. For instance, in the case of two users sharing the highest score, the produced list would be [1, 1, 3, 4, 5]. This helper function is called in setting user rewards. For instance, if there are 2 first place holder then 10000 prize is shared among them.

  - **UserQueue**: Manages a set of integers and a list of integer queues, users who pass all checks and are eligible to enter the queue are sent here. The logic for queue management is implemented within this class.

  - **ProcessTournamentResults**: While the function of this class could be implemented in the tournament service, its purpose is to enable the @Async call of the process function and maintain clean code. The class is called from the tournament service everyday at 20 for processing tournament results.

Unit-tests are implemented for all units belonging to the controller, service, repository, and util packages.

## Enter-Tournament Flow

I want to share implementation of this flow in this seperate section since most of the implementation logic lies under this flow. The enter tournament request is sent from controller to tournament service. After completing join conditions, it is sent to UserQueue component. When a group formed returns to tournament service again and it is saved to database and return group leaderboard to controller. Finally response is set and returns to user.

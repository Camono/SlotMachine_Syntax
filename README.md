# Slot Machine Application

## Summary
This application is a JavaFX-based slot machine game featuring a visually appealing interface and engaging gameplay. It integrates backend logic managed by several classes, including `GameManager`, `StaticGameData`, `GameResult`, `Symbol`, and `SymbolType`. These classes work together to handle the game's mechanics, such as symbol generation, bet management, and calculation of winnings.

## Key Features
- **Spin Functionality:** Activated by `spinBtn` to simulate the slot machine reels.
- **Bet Adjustment:** `increaseBetBtn` and `decreaseBetBtn` for bet customization.
- **Symbol Display:** `symbol1` to `symbol5` ImageView elements for showing spin outcomes.
- **Balance and Bet Display:** `balanceLabel` and `betLabel` for displaying current balance and bet.
- **Gameplay Logic:** Managed by `GameManager` for result generation and winnings calculation.
- **User Interface:** Defined in an FXML file with a user-friendly design.

## Backend Classes
- **GameManager:** Core controller for backend logic, managing balance, bets, and spin results.
- **StaticGameData:** Contains symbol data with multipliers and appearance probabilities.
- **GameResult:** Represents the outcome of spins, affecting player's balance.
- **Symbol:** Defines the visible symbols in the frontend.
- **SymbolType:** An Enum class for categorizing individual symbols.

## Getting Started
To start the project, follow these steps:
- **Clone the Repository:** Obtain the project files from the repository. And checkout to `main` branch.
- **Java Version:** Ensure Java 17 is installed on your system, as it is required for running this application.
- **Open in IDE:** Open the project in a Java IDE, preferably one that supports JavaFX.
- **Install Dependencies:** Ensure JavaFX and any other dependencies are properly installed.
- **Run the Application:** Execute the `SlotMachineApplication` class. This will launch the application, displaying a slot machine interface with `spin`, `bet+`, and `bet-` buttons.
- **Initial Gameplay:** Initially, the bet is set to 1. Upon clicking `spin`, the slot machine will operate, and afterwards, you can view your current balance updated based on the spin outcome.


## To-Do List
- **StaticGameData to JSON:** Convert the `StaticGameData` class to use a JSON file for easier data management and modification.
- **Spin Sound Effects:** Implement sound effects during and after the spin for an enhanced user experience.
- **Spin Animation:** Develop an animation for the spinning action to make the gameplay more visually engaging.
- **New Symbols:** Add a variety of new symbols to the game for more diversity and excitement.
- **Symbol Information Popup:** Create an information popup detailing all symbols, their chances, and multipliers.
- **Post-Spin Popup:** Introduce a popup that appears after each spin, displaying winnings if any.


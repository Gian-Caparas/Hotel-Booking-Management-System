PROJECT OVERVIEW

🐱 WildCat Hotel Reservation System
A desktop-based hotel management application built with JavaFX 21, Maven, and Hibernate.

🛠 Prerequisites
Before running the application, ensure your environment meets the following requirements:
- JDK: Java 21 (Check with java -version)
- Build Tool: Apache Maven (Check with mvn -version)
- Database: MySQL (via XAMPP or local installation)

🚀 How to Run the System
Follow these steps to get the application up and running:
1. Start the Database
Open the XAMPP Control Panel.
Start the Apache and MySQL modules.
Ensure your database schema is imported (if applicable) and your connection strings in hibernate.cfg.xml or DataBase.java match your XAMPP settings.
2. Launch the Application
Open your terminal in the project root directory and run the following Maven command: mvn clean javafx:run

Note: The clean command ensures that any old compiled files are removed, and javafx:run handles the module-path and dependencies automatically.

📂 Project Structure Note for Contributors
This project uses the standard Maven directory layout.
- Source Code: Found in src/main/java.
- UI/FXML & Styles: Found in src/main/resources.

If you add new .fxml files, please ensure they are placed in the appropriate sub-folder within resources and updated in the Paths.java class to maintain consistency across the team.

💡 Troubleshooting
If you encounter a Fatal error compiling: invalid target release: 21, ensure your JAVA_HOME environment variable is pointing to JDK 21 and not an older version (like Java 8).

USE-CASE DIAGRAM:
<img width="1074" height="600" alt="image" src="https://github.com/user-attachments/assets/5e590872-405a-4995-b1b1-973279bace72" />

CASE DIAGRAM:
<img width="631" height="672" alt="image" src="https://github.com/user-attachments/assets/9d6ef1ef-a0c2-40b6-861f-f9fe33d0edca" />

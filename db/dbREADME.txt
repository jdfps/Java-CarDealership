1. Install Requirements

Java 17+ (OpenJDK recommended)

MySQL 8+ (make sure mysql and mysqldump commands work in your terminal)

JavaFX SDK (download from https://openjfx.io
)

iText 7 (download from https://itextpdf.com
)

MySQL Connector/J (download from https://dev.mysql.com/downloads/connector/j/
)

Put .jar files (JavaFX, iText, MySQL connector) into a lib/ folder and add them to your project libraries in IntelliJ.

2. Database Setup

We provide a SQL dump in db/schema.sql
. This contains both the table structure and sample data.

Open a terminal (not inside the MySQL shell).

Navigate to the project folder.

Run:

mysql -u root -p < db/schema.sql


Replace root with your MySQL username if different.

Enter your MySQL password when prompted.

This will create a database called cars_db with a table called cars and load sample rows.

3. Verify Database

Check that everything worked:

mysql -u root -p
mysql> USE cars_db;
mysql> SELECT * FROM cars;


You should see car records already populated.

4. Images

All images are stored in the images/ folder of this project.

The database picture_path column uses relative paths (e.g., images/2018_subaru_brz.jpeg).

Do not hardcode absolute file system paths.

5. Code Changes (If Needed)

Database connection is in Controller.java:

Connection con = DriverManager.getConnection(
    "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root"
);


Change "root", "root" to your MySQL username and password if different.

Do not change the database name (cars_db) unless you created it differently.

6. Running the Project

In IntelliJ:

Mark src/main/java as Sources Root and src/main/resources as Resources Root.

Add your JavaFX SDK and other .jars under Project Structure → Libraries.

Add VM options to your run configuration (replace /path/to/javafx/lib with your SDK path):

--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml


Run Main.java.
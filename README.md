# Java-CarDealership
Creating a car dealership program that uses mySQL. We implement GUI with javaFX. Connect it to SQL with a connector. Generate PDF's with iText

## Dependencies
- JavaFX SDK (24.0.2)(https://openjfx.io/)
- iText 7 (9.3.0) (watch youtube video on it) ([https://itextpdf.com/](https://github.com/itext/itext-java/releases/tag/9.3.0))
- MySQL Connector/J (9.4.0) (https://dev.mysql.com/downloads/connector/j/)

## Setup
1. Download each library and place the `.jar` files in the `lib/` folder.
2. In IntelliJ (or your IDE), go to **Project Structure > Libraries** and add the jars from `lib/`.
3. Make sure your run configuration includes: --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
4. Its version controled so download the correct ones

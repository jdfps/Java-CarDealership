import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.itextpdf.layout.properties.UnitValue;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Controller
{

    private Car selectedCar;


    @FXML
    private TextField search_bar;

    @FXML
    private Button search_button;
    @FXML
    private ImageView car_image;
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car, String> colMake, colModel, colCondition, colVIN;
    @FXML
    private TableColumn<Car, Integer> colYear, colPrice, colMileage;
    @FXML
    private Button add;
    @FXML
    private Button remove;

    @FXML
    public void initialize()
    {
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("condition"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colVIN.setCellValueFactory(new PropertyValueFactory<>("VIN"));
        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));

        // going to make it to where when you double-click  a vin it searches for picture
        table.setOnMouseClicked(event ->
        {
            if (event.getClickCount() == 2 && !table.getSelectionModel().isEmpty())
            {
                Car selected = table.getSelectionModel().getSelectedItem();
                selectedCar = table.getSelectionModel().getSelectedItem();

                if (selected != null && selected.getPicture_path() != null && !selected.getPicture_path().isEmpty())
                {
                    File file = new File(selected.getPicture_path());
                    if (file.exists())
                    {
                        Image image = new Image(file.toURI().toString());
                        car_image.setFitWidth(285);
                        car_image.setFitHeight(164);
                        car_image.setPreserveRatio(true);
                        car_image.setImage(image);
                        System.out.println("Loaded image for: " + selected.getVIN());
                    } else
                    {
                        System.err.println("Image file not found: " + selected.getPicture_path());
                    }
                } else
                {
                    System.err.println("No picture path found for this car.");
                }
            }
        });


        if (LoginScreen.isAdmin)
        {
            add.setVisible(true);   // show for admins
            remove.setVisible(true);
        } else
        {
            add.setVisible(false);  // hide for normal users
            remove.setVisible(false);
        }
    }

    public void admin_add(ActionEvent e) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin_add_window.fxml")));
        Stage addStage = new Stage(); // create a new stage
        addStage.setTitle("Add Vehicle");
        addStage.setScene(new Scene(root));
        addStage.setTitle("Add Car");
        addStage.show(); // opens alongside existing window
    }

    public void admin_remove(ActionEvent e) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin_remove_window.fxml")));
        Stage removeStage = new Stage(); // create a new stage
        removeStage.setTitle("Remove Vehicle");
        removeStage.setScene(new Scene(root));
        removeStage.setTitle("Remove Car");
        removeStage.show(); // opens alongside existing window
    }

    public void _logout(ActionEvent e) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login_resources.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void _list(ActionEvent e) throws SQLException
    {
        List<Car> out = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT make, model, year, car_condition, price, picture_path, VIN, Mileage FROM cars");
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                out.add(new Car(
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("car_condition"),
                        rs.getInt("price"),
                        rs.getString("picture_path"),
                        rs.getString("VIN"),
                        rs.getInt("Mileage")
                ));
            }
        }

        ObservableList<Car> cars = FXCollections.observableArrayList(out);


        search_button.setOnAction(c ->
        {
            ObservableList<Car> filtered_cars = FXCollections.observableArrayList();
            String input = search_bar.getText().toLowerCase().trim();

            for (Car car : cars)
            {
                if (car.getMake().toLowerCase().contains(input) ||
                        car.getModel().toLowerCase().contains(input) ||
                        car.getCondition().toLowerCase().contains(input) ||
                        car.getVIN().toLowerCase().contains(input) ||
                        String.valueOf(car.getYear()).contains(input) ||
                        String.valueOf(car.getMileage()).contains(input) ||
                        String.valueOf(car.getPrice()).contains(input))
                {
                    filtered_cars.add(car);
                }
            }

            table.setItems(filtered_cars);
        });


        // Bind it to the table
        table.setItems(cars);
    }


    @FXML
    private void _purchase(ActionEvent event)
    {

        System.out.println("BUYING " + selectedCar.getModel());

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM cars WHERE VIN = ?")
        )
        {
            ps.setString(1, selectedCar.getVIN());
            ResultSet rs = ps.executeQuery();

            Car car_of_purchase = new Car();
            while (rs.next())
            {
                car_of_purchase.setMake(rs.getString("make"));
                car_of_purchase.setModel(rs.getString("model"));
                car_of_purchase.setYear(rs.getInt("year"));
                car_of_purchase.setCondition(rs.getString("car_condition"));
                car_of_purchase.setPrice(rs.getInt("price"));
                car_of_purchase.setPicture_path(rs.getString("picture_path"));
                car_of_purchase.setVIN(rs.getString("VIN"));
                car_of_purchase.setMileage(rs.getInt("Mileage"));
            }
            System.out.println("Purchasing: " + car_of_purchase.getMake() + ", " + car_of_purchase.getModel() + ", " + car_of_purchase.getVIN());

            try
            {
                setupPDF(car_of_purchase);
            } catch (Exception e)
            {
                e.printStackTrace();
            }


        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("_purchase: " + event);
    }

    private void setupPDF(Car car) throws IOException, SQLException
    {

        String destination = "bill_of_sale.pdf";

        PdfWriter writer = new PdfWriter(destination);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD); // just makes a bold variable we can use that is bold lol
        // ******************************************************************************************************************
        Paragraph title = new Paragraph("CSCI 3033 Motor Vehicles Bill of Sale")
                .setFontSize(20)
                .setFont(bold)
                .setUnderline()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(title);
        // ******************************************************************************************************************
        Paragraph first_section = new Paragraph("VEHICLE INFORMATION")
                .setFontSize(15)
                .setFont(bold)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(10);
        document.add(first_section);
        // ******************************************************************************************************************
        Table vehicle_info = new Table(2);
        vehicle_info.setWidth(UnitValue.createPercentValue(100));

        vehicle_info.addCell(new Cell().add(new Paragraph("Make: \t\t" + car.getMake())));
        vehicle_info.addCell(new Cell().add(new Paragraph("Model: \t\t" + car.getModel())));

        vehicle_info.addCell(new Cell().add(new Paragraph("Condition: \t\t" + car.getCondition().toUpperCase())));
        vehicle_info.addCell(new Cell().add(new Paragraph("year: \t\t" + car.getYear()))
                .setPaddingRight(55));


        double _price = .9075 * (Double.parseDouble(String.valueOf(car.getPrice())));
        double price = _price;
        String formattedPrice = NumberFormat.getNumberInstance().format(price);

        vehicle_info.addCell(new Cell().add(new Paragraph("Price: \t\t" + "$" + formattedPrice + "\t(Inc. TN sales tax)")));

        String formattedMiles = NumberFormat.getNumberInstance().format(car.getMileage());
        vehicle_info.addCell(new Cell().add(new Paragraph("Mileage:\t\t" + formattedMiles)));

        vehicle_info.addCell(new Cell(1, 2).add(new Paragraph("VIN #: \t\t" + car.getVIN())));
        vehicle_info.setMarginBottom(25);
        document.add(vehicle_info);
        // ******************************************************************************************************************

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = today.format(formatter);


        Paragraph second_section = new Paragraph("Date of sale: " + formattedDate)
                .setFontSize(13)
                .setFont(bold)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(10);
        document.add(second_section);
        // ******************************************************************************************************************

        String firstName = "";
        String lastName = "";
        String email = "";
        String phone = "";
        String password = "";
        boolean admin = false;

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT FirstName, LastName, email, phone_number, Password, is_admin FROM login_info WHERE email=?"
             ))
        {
            ps.setString(1, LoginScreen.loggedInEmail);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                firstName = rs.getString("FirstName");
                lastName = rs.getString("LastName");
                email = rs.getString("email");
                phone = rs.getString("phone_number");
                password = rs.getString("Password");
                admin = rs.getBoolean("is_admin");

                System.out.println(firstName + " " + lastName + " " + phone);
            } else
            {
                System.out.println("No user found.");
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }


// ============================
//        PARAGRAPHS
// ============================

        if (!admin)   // ❗ NOT ADMIN → BUYER is filled in
        {
            Paragraph buyerSection = new Paragraph()
                    .add(new Text("Buyer").setFont(bold).setFontSize(15).setUnderline())
                    .add("\n\nName(s): ")
                    .add(new Text("\t" + firstName + " " + lastName))  // FIXED
                    .add("\n\nAddress: ")
                    .add(new Text("______________________________"))
                    .add("\n\nPhone #: ")
                    .add(new Text("\t" + phone))
                    .add("\n\nEmail: ")
                    .add(new Text("\t" + email));
            buyerSection.setMarginBottom(25);
            document.add(buyerSection);

            Paragraph sellerSection = new Paragraph()
                    .add(new Text("Seller").setFont(bold).setFontSize(15).setUnderline())
                    .add("\n\nSeller Name(s): ")
                    .add(new Text("______________________________"))
                    .add("\n\nAddress: ")
                    .add(new Text("______________________________"))
                    .add("\n\nPhone #: ")
                    .add(new Text("______________________________"))
                    .add("\n\nEmail: ")
                    .add(new Text("______________________________"));
            sellerSection.setMarginBottom(25);
            document.add(sellerSection);
        } else
        {
            Paragraph buyerSection = new Paragraph()
                    .add(new Text("Buyer").setFont(bold).setFontSize(15).setUnderline())
                    .add("\n\nName(s): ")
                    .add(new Text("______________________________"))
                    .add("\n\nAddress: ")
                    .add(new Text("______________________________"))
                    .add("\n\nPhone #: ")
                    .add(new Text("______________________________"))
                    .add("\n\nEmail: ")
                    .add(new Text("______________________________"));
            buyerSection.setMarginBottom(25);
            document.add(buyerSection);

            Paragraph sellerSection = new Paragraph()
                    .add(new Text("Seller").setFont(bold).setFontSize(15).setUnderline())
                    .add("\n\nSeller Name(s): ")
                    .add(new Text("\t" + firstName + " " + lastName))   // FIXED
                    .add("\n\nAddress: ")
                    .add(new Text("______________________________"))
                    .add("\n\nPhone #: ")
                    .add(new Text("\t" + phone))
                    .add("\n\nEmail: ")
                    .add(new Text("\t" + email));
            sellerSection.setMarginBottom(25);
            document.add(sellerSection);
        }

        // ******************************************************************************************************************
        document.close();
        System.out.println("PDF created at " + destination);
    }

    public void _exit(ActionEvent e)
    {
        System.out.println("_exit: " + e);
        // exit the program
        System.exit(1);
    }


}

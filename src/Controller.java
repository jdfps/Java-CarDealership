import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Controller
{
    @FXML
    private ImageView car_image;
    @FXML
    private TextArea text_box;
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car, String> colMake, colModel, colCondition, colVIN;
    @FXML
    private TableColumn<Car, Integer> colYear, colPrice, colMileage;

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
            if (event.getClickCount() == 2)
            { // double-click
                Car selected = table.getSelectionModel().getSelectedItem();
                if (selected != null)
                {
                    text_box.setText(selected.getVIN()); // put VIN into the search box
                }
            }
        });
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
        // Bind it to the table
        table.setItems(cars);


        System.out.println("_list: " + e);
    }

    public void _enter(ActionEvent e) throws SQLException
    {
        String picture_path = "";
        String input_make = text_box.getText();
        System.out.println("Searching for model: " + input_make);

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT picture_path FROM cars WHERE VIN = ?")
        )
        {
            ps.setString(1, input_make);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                picture_path = rs.getString("picture_path");
            }

            if (picture_path != null && !picture_path.isEmpty())
            {
                File file = new File(picture_path);
                if (file.exists())
                {
                    System.out.println("File exists and is ready to load: " + picture_path);
                    Image image = new Image(file.toURI().toString());
                    car_image.setFitWidth(285);
                    car_image.setFitHeight(164);
                    car_image.setPreserveRatio(true); // keeps the aspect ratio the same so it doesn't stretch it
                    car_image.setImage(image);
                } else
                {
                    System.err.println("error...file not found at path: " + picture_path);
                }
            } else
            {
                System.err.println("No picture path found in DB for model: " + input_make);
            }
        }

        System.out.println("_enter: " + e);
    }

    @FXML
    private void _purchase(ActionEvent event)
    {
        // create a downloadable PDF of that car selected form the textbox with the VIN selected
        String input_make = text_box.getText();
        System.out.println("Searching for model: " + input_make);

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM cars WHERE VIN = ?")
        )
        {
            ps.setString(1, input_make);
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
                setupPDF();
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

    private void setupPDF() throws IOException
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

        vehicle_info.addCell(new Cell().add(new Paragraph("Make: ")));
        vehicle_info.addCell(new Cell().add(new Paragraph("Model: ")));

        vehicle_info.addCell(new Cell().add(new Paragraph("Style / Body Type: ")));
        vehicle_info.addCell(new Cell().add(new Paragraph("year: "))
                .setPaddingRight(55));


        vehicle_info.addCell(new Cell().add(new Paragraph("Color:")));
        vehicle_info.addCell(new Cell().add(new Paragraph("Mileage:")));

        vehicle_info.addCell(new Cell(1, 2).add(new Paragraph("VIN #:")));
        vehicle_info.setMarginBottom(25);
        document.add(vehicle_info);
        // ******************************************************************************************************************
        Paragraph second_section = new Paragraph("Date of sale ___________________ (mm/dd/yyyy)")
                .setFontSize(13)
                .setFont(bold)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(10);
        document.add(second_section);
        // ******************************************************************************************************************
        Paragraph buyerSection = new Paragraph()
                .add(new Text("Buyer").setFont(bold).setFontSize(15).setUnderline())   // header
                .add("\n\nName(s): ")
                .add(new Text("______________________________"))   // blank
                .add("\n\nAddress: ")
                .add(new Text("______________________________"))
                .add("\n\nPhone #: ")
                .add(new Text("______________________________"))
                .add("\n\nEmail: ")
                .add(new Text("________________________________"));
        buyerSection.setMarginBottom(25);
        document.add(buyerSection);
        // ******************************************************************************************************************
        Paragraph sellerSection = new Paragraph()
                .add(new Text("Seller").setFont(bold).setFontSize(15).setUnderline())
                .add("\n\nSeller Name(s): ")
                .add(new Text("______________________________"))
                .add(new Text("\n\nAddress: "))
                .add(new Text("______________________________"))
                .add(new Text("\n\nPhone #: "))
                .add(new Text("______________________________"))
                .add(new Text("\n\nEmail: "))
                .add(new Text("______________________________"));
        sellerSection.setMarginBottom(25);
        document.add(sellerSection);
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

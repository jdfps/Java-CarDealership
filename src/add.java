import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class add
{
    @FXML
    private TextField make;
    @FXML
    private TextField model;
    @FXML
    private TextField year;
    @FXML
    private TextField VIN;
    @FXML
    private TextField condition;
    @FXML
    private TextField price;
    @FXML
    private TextField mileage;

    public void _back(ActionEvent e)
    {
        // Close only the current Add window
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    public void _clear(ActionEvent e)
    {
        make.clear();
        model.clear();
        year.clear();
        VIN.clear();
        condition.clear();
        price.clear();
        mileage.clear();
    }

    public void _add(ActionEvent e) throws SQLException
    {
        String _make = make.getText();
        String _model = model.getText();
        int _year = Integer.parseInt(year.getText());
        String _VIN = VIN.getText();
        String _condition = condition.getText();
        int _price = Integer.parseInt(price.getText());
        int _mileage = Integer.parseInt(mileage.getText());

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO cars (make, model, year, car_condition, price, picture_path, VIN, Mileage) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            ps.setString(1, _make);
            ps.setString(2, _model);
            ps.setInt(3, _year);
            ps.setString(4, _condition);
            ps.setInt(5, _price);
            ps.setNull(6, java.sql.Types.VARCHAR); // picture_path = NULL
            ps.setString(7, _VIN);
            ps.setInt(8, _mileage);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0)
            {
                System.out.println("Car added successfully!");
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

}

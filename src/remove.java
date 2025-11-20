import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class remove
{
    @FXML
    private TextField make;
    @FXML
    private TextField model;
    @FXML
    private TextField year;
    @FXML
    private TextField VIN;

    public void _back(ActionEvent e) throws IOException
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
    }

    public void _remove(ActionEvent e) throws SQLException
    {
        String _VIN = VIN.getText(); // VIN is unique, so we'll use it to delete the car

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM cars WHERE VIN = ?"))
        {
            ps.setString(1, _VIN);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0)
            {
                System.out.println("Car removed successfully!");
            } else
            {
                System.out.println("No car found with that VIN.");
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

}

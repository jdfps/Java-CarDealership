import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
public class ForgotPassword
{
    @FXML
    private TextField FirstName_Field;
    @FXML
    private TextField LastName_Field;
    @FXML
    private TextField Email_Field;
    @FXML
    private TextField Phone_Field;
    @FXML
    private Label show_password;
    public void _back(ActionEvent e) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login_resources.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void _enter(ActionEvent e)
    {
        String firstName = FirstName_Field.getText().trim();
        String lastName = LastName_Field.getText().trim();
        String email = Email_Field.getText().trim();
        String phone = Phone_Field.getText().trim();

        // simple empty field check
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty())
        {
            show_password.setText("Please fill out all fields.");
            return;
        }

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT Password FROM login_info WHERE FirstName = ? AND LastName = ? AND email = ? AND phone_number = ?")
        )
        {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phone);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                // all fields matched — show password
                String password = rs.getString("Password");
                show_password.setText("Your password: " + password);
                System.out.println("Password found: " + password);
            } else
            {
                // no matching record found
                show_password.setText("No account found with those details.");
            }

        } catch (SQLException ex)
        {
            ex.printStackTrace();
            show_password.setText("Database error. Please try again later.");
        }
    }
    public void _clear(ActionEvent e)
    {
        FirstName_Field.clear();
        LastName_Field.clear();
        Email_Field.clear();
        Phone_Field.clear();
        show_password.setText("");
    }
}

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

public class NewAccount
{
    @FXML
    private TextField password_field;
    @FXML
    private TextField email_field;
    @FXML
    private TextField phone_field;
    @FXML
    private TextField firstName_field;
    @FXML
    private TextField lastName_field;
    @FXML
    private Label user_update_field;
    public void _enter(ActionEvent e) throws SQLException
    {
        String entered_fname = firstName_field.getText();
        String entered_lname = lastName_field.getText();
        String entered_email = email_field.getText();
        String entered_password = password_field.getText();
        String entered_phone = phone_field.getText();

        if (entered_fname.isEmpty() || entered_lname.isEmpty() ||
                entered_email.isEmpty() || entered_password.isEmpty() || entered_phone.isEmpty())
        {
            user_update_field.setText("One of your fields is invalid");
            return;
        }

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root"))
        {
            // 🔎 First check if email already exists
            try (PreparedStatement checkPs = con.prepareStatement(
                    "SELECT 1 FROM login_info WHERE email = ?"))
            {
                checkPs.setString(1, entered_email);
                try (ResultSet rs = checkPs.executeQuery())
                {
                    // this if statement will only execute if the ResultSet (rs) gets a request back meaning
                    // the email was found!!!!!
                    if (rs.next())
                    {
                        user_update_field.setText("This email is already registered!");
                        return;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO login_info (FirstName, LastName, email, phone_number, password) VALUES (?, ?, ?, ?, ?)"))
            {
                ps.setString(1, entered_fname);
                ps.setString(2, entered_lname);
                ps.setString(3, entered_email);
                ps.setString(4, entered_phone);
                ps.setString(5, entered_password);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0)
                {
                    user_update_field.setText("Account created successfully!");
                } else
                {
                    user_update_field.setText("Failed to create account.");
                }
            }
        }
    }
    public void _back(ActionEvent e) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login_resources.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

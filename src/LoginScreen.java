import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class LoginScreen
{
    public static boolean isAdmin = false; // store admin status globally
    @FXML
    private PasswordField password_field;
    @FXML
    private TextField username_field;
    @FXML
    private Label incorrect_info;


    public void _login(ActionEvent e)
    {

        String entered_email = username_field.getText();
        System.out.println("email entered: " + entered_email);
        String entered_password = password_field.getText();
        System.out.println("password entered: " + entered_password);

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/cars_db", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM login_info WHERE email=? AND password=?"
             ))
        {
            ps.setString(1, entered_email);
            ps.setString(2, entered_password);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    isAdmin = rs.getBoolean("is_admin");


                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("resources.fxml")));
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Used Cars");
                    stage.show();
                } else
                {
                    incorrect_info.setText("Incorrect Email or Password");
                    incorrect_info.setAlignment(Pos.CENTER);
                    System.out.println("Invalid email or password!");
                }
            }

        } catch (SQLException | IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void _create_account(ActionEvent e) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("newAccount_resources.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Create an Account");
        stage.show();

    }

    public void _forgot_password(ActionEvent e) throws IOException
    {

        incorrect_info.setText("contact us at IDONTCARE@WTF.com");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("forgotPassword.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Forgot Password");
        stage.show();
    }

    public void _exit(ActionEvent e)
    {
        System.out.println("_exit: " + e);
        // exit the program
        System.exit(1);
    }
}

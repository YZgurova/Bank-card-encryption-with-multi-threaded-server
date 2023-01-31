import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginForm {
    public final Scene scene;
    public final VBox root;
    public final Label title;
    public final TextField usernameInput;
    public final TextField passwordInput;
    public final TextField cardNum;
    public final Label info;
    public final Button login;
    public final Button register;

    public LoginForm() {
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(14));
        root.setSpacing(8);
        this.title = new Label("Login/Register");
        this.usernameInput = new TextField("username");
        this.passwordInput = new TextField("password");
        this.cardNum = new TextField("card num");
        this.info= new Label();
        this.login = new Button("Login");
        this.register = new Button("Register");
        root.getChildren().addAll(title,usernameInput,passwordInput,info,login, register);
        scene = new Scene(root, 450, 350, Color.web("#666970"));
    }
}

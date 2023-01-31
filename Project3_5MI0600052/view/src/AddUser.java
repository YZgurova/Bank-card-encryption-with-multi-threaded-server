import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AddUser {
    public final Scene scene;
    public final VBox root;
    public final Label title;
    public final TextField usernameInput;
    public final Label someInfo;
    public final Button login;

    public final Button addRights;

    public AddUser() {
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(14));
        root.setSpacing(8);
        this.title = new Label("Add user");
        this.usernameInput = new TextField("username");
        this.someInfo= new Label();
        this.login = new Button("Login");
        this.addRights=new Button("AddRights");
        root.getChildren().addAll(title,usernameInput,someInfo,addRights,login);
        scene = new Scene(root, 450, 350, Color.web("#666970"));
    }
}

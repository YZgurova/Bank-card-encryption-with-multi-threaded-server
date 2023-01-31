import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ServerMainForm {
    public final Scene scene;
    public final VBox root;
    public final Label actions;
    public final TextArea textArea;
    public final Button addUser;

    public ServerMainForm() {
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(14));
        root.setSpacing(8);
        this.actions=new Label("Actions");
        this.textArea=new TextArea();
        this.textArea.setEditable(false);
        this.addUser = new Button("AddUser");
        root.getChildren().addAll(actions, textArea, addUser);
        scene = new Scene(root, 450, 350, Color.web("#666970"));
    }
}

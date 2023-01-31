import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainForm {
    public final Scene scene;
    public final VBox root;
    public final Text addField;
    public final TextField addCardNumber;
    public final Button add;
    public final Text encryptField;
    public final TextField cardNumberEncrypt; // inputs message from user
    public final Label encryptRes;
    public final Button encrypt;
    public final Text decryptField;
    public final TextField cardNumberDecrypt;// display information to user
    public final Label decryptRes;
    public final Button decrypt;

    public MainForm() {
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(14));
        root.setSpacing(8);
        this.addField=new Text("Add card");
        this.addCardNumber=new TextField();
        this.add=new Button("Add");
        this.encryptField = new Text("Add/Encrypt card number");
        this.cardNumberEncrypt = new TextField();
        this.encryptRes = new Label();
        this.encrypt = new Button("Encrypt");
        this.encrypt.setDisable(true);
        this.decryptField = new Text("Decrypt card number");
        this.cardNumberDecrypt = new TextField();
        this.decryptRes = new Label();
        this.decrypt = new Button("Decrypt");
        this.decrypt.setDisable(true);
        root.getChildren().addAll(addField, addCardNumber, add, encryptField, cardNumberEncrypt, encryptRes, encrypt, decryptField,cardNumberDecrypt, decryptRes, decrypt);
        scene = new Scene(root, 450, 350, Color.web("#666970"));
    }
}

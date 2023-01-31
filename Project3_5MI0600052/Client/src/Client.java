// Set up a client that will receive a connection from a server, send
// a string to the server, and close the connection.

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
//import java.nio.
import card.CardsController;
import cipher.CipherMethod;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import user.User;
import user.UsersController;

public class Client extends Application {
    private UsersController users;
    private CardsController cards;
    public User currentUser;
    private MainForm main;
    private LoginForm login;
    private CipherMethod cipher;
    private volatile Boolean ok=false;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String chatServer;
    private Socket client;
    private String response;

    @Override
    public void start(Stage stage) throws IOException {
        cipher=new CipherMethod();
        users= new UsersController();
        cards=new CardsController(users.getUsers());
        login = new LoginForm();
        main = new MainForm();
        stage.setScene(login.scene);

        login.login.setOnAction(event -> {
            String  message = "Login "+login.usernameInput.getText()+" "+login.passwordInput.getText();
            try {
                sendLoginRegisterMessage(stage, message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        login.register.setOnAction(event -> {
            String  message = "Register "+login.usernameInput.getText()+" "+login.passwordInput.getText();
            try {
                sendLoginRegisterMessage(stage, message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        main.add.setOnAction(event -> {
            String message = "AddCard " + currentUser.getUsername() + " " + main.addCardNumber.getText();
            sendData(message);
        });

        main.encrypt.setOnAction(event -> {
            String message = "Encrypt " + currentUser.getUsername() + " " + main.cardNumberEncrypt.getText();
            sendData(message);
        });

        main.decrypt.setOnAction(event -> {
            String message = "Decrypt " + currentUser.getUsername() + " " + main.cardNumberDecrypt.getText();
            sendData(message);
        });

        stage.setOnCloseRequest(evt -> stop());
        stage.setTitle("Client");
        stage.show();
        Thread thread = new Thread(this::runClient);
        thread.start();
    }

    private void sendLoginRegisterMessage(Stage stage, String message) throws InterruptedException { //логика при влизане и регистрация
        sendData(message); //изпраща съобщението
        displayMessage(message); //показва го
        Thread.sleep(3000); //чака отговор от сървъра
        if(this.ok) { //ако отговорът е ок
            stage.setScene(main.scene); //сменя сцената
            if(currentUser!=null) { //ако потребителя вече се е логнал е != Null
                enableEncryptingAndDecrypting(currentUser.getCanEncrypt());
            }
            try {
                currentUser=new User(login.usernameInput.getText(), login.passwordInput.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void enableEncryptingAndDecrypting(boolean editable) { //докато админа не разреши потребителят не може да криптира
        main.encrypt.setDisable(!editable);
        main.decrypt.setDisable(!editable);
    }

    public void runClient() {  //стартира нишката на клиента
        try
        {
            connectToServer();
            getStreams();
            processConnection();
        }
        catch (EOFException eofException) {
            displayMessage("Client terminated connection");
        }
        catch (IOException ioException) {
            System.out.println("Client IO exception    " + ioException.getMessage());
        }
        finally {
            closeConnection();
        }
    }

    // connect to server
    private void connectToServer() throws IOException { //свързва се със сървъра чрез сокет на същия порт
        displayMessage("Attempting connection");

        client = new Socket(InetAddress.getByName(chatServer), 12345);
        if (client == null) {
            stop();
        }
        displayMessage("Connected to: "
                + client.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException { //инициализираме стриймовете чрез които сървъра и клиента ще си изпращат съобщения
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();

        input = new ObjectInputStream(client.getInputStream());

        displayMessage("Got I/O streams");
    }

    private void processConnection() throws IOException {
        String message = "Connection successful";
        sendData(message);
        do
        {
            try
            {
                message = (String) input.readObject(); //тук получаваме съобщенията от сървъра
                String[] m = message.split(" "); //сплитвам съобщението
                displayMessage(message); //извеждам го на екран
                //действия които се вършат според съобщението
                if(Objects.equals(m[0], "Login(OK)") || Objects.equals(m[0], "Register(OK)")) {
                    this.ok=true;
                    enableEncryptingAndDecrypting(Boolean.parseBoolean(m[1]));
                } else if(Objects.equals(m[0], "Rights")) {
                    enableEncryptingAndDecrypting(true);
                }
            }
            catch (ClassNotFoundException classNotFoundException) {
                displayMessage("Unknown object type received");
            }
            catch (SocketException s) {
                break;
            }
        } while (!message.toUpperCase().equals("CLIENT>>> TERMINATE"));
    }

    private void closeConnection() { //затваря сокета и стриймовете
        displayMessage("Terminating connection");
//        enableEncryptingAndDecrypting(false);

        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (client != null) {
                client.close();
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendData(String message) { //изпраща съобщение до сървъра
        try
        {
            output.writeObject(message);
            output.flush();
//            displayMessage(message);
        }
        catch (IOException ioException) {
            main.encryptRes.setText("Error writing object");
        }
    }
    private void displayMessage(final String message) { //извежда съобщение на GUI
        Platform.runLater(()
                        ->
            {
                this.login.info.setText(message);
                String[] m = message.split(" ");
                switch (m[0]) {
                    case "Login(OK)", "Register(OK)" -> {
                        try {
                            currentUser=new User(login.usernameInput.getText(), login.passwordInput.getText());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        this.currentUser.setCanEncrypt(Boolean.parseBoolean(m[1]));
                    }
                    case "Encrypt" -> this.main.encryptRes.setText(m[1]);
                    case "Decrypt" -> this.main.decryptRes.setText(m[1]);
                    case "Rights" -> enableEncryptingAndDecrypting(true);
                    default -> this.login.info.setText(message);
                }
            }
        );
    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

}

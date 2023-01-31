import card.Card;
import card.CardsController;
import cipher.CipherMethod;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import user.User;
import user.UsersController;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.InvalidPropertiesFormatException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//9563960122001999 коректен номер на карта
public class Server extends Application {
    private UsersController collection;
    private CardsController cardsController;
    private CipherMethod cipher;
    private AddUser addUser;
    private ServerMainForm main;
    private ExecutorService threadExecutor;
    private ServerSocket server;
    private int counter = 1;
    private Hashtable<Thread, ObjectOutputStream> writers;

    @Override
    public void start(Stage stage) throws Exception {
        writers = new Hashtable<>();
        threadExecutor = Executors.newCachedThreadPool();
        collection = new UsersController();
        cardsController=new CardsController(collection.getUsers());
        cipher=new CipherMethod();
        main = new ServerMainForm();
        addUser = new AddUser();
        stage.setScene(main.scene);

        addUserFormActions(stage);

        main.addUser.setOnAction(event -> {
            stage.setScene(addUser.scene);
        });


        stage.show();
        new Thread(this::runServer).start();
    }

    private void addUserFormActions(Stage stage) { //действия за бутоните на логин панела
        addUser.login.setOnAction(event -> {
            stage.setScene(main.scene);
        });

        addUser.addRights.setOnAction(event -> {
            String username = addUser.usernameInput.getText();
            sendData("Rights "+true);
            if(collection.isUserExists(username)) {
                collection.getUser(username).setCanEncrypt(true);
                try {
                    collection.saveInXMLFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                addUser.someInfo.setText("This user not exist");
            }
            clearFields();
        });
    }

    private void clearFields() { //изчиства текстовите полета
        addUser.usernameInput.setText("");
        addUser.someInfo.setText("");
    }

    public class RunClient implements Runnable {

        private ObjectOutputStream output; // output stream to client
        private ObjectInputStream input; // input stream from client
        private final Socket connection; // connection to client
        private final int counter; //брои с колко клиента е свързан

        public RunClient(Socket connection, int counter) {
            this.connection = connection;
            this.counter = counter;
        }

        // get streams to send and receive data
        private void getStreams() throws IOException {
            // set up output stream for objects
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush(); // flush output buffer to send header information

            // set up input stream for objects
            input = new ObjectInputStream(connection.getInputStream());
            // add outputStream to the table of writers
            writers.put(Thread.currentThread(), output);
            //позволява input and output streams
            displayResult("\nGot I/O streams\n");

        }

        @Override
        public void run() {

            displayResult("Connection " + counter + " received from: "
                    + connection.getInetAddress().getHostName());

            try {
                getStreams();
                processConnection();
            } catch (EOFException eofException) {
                displayResult("\nServer terminated connection");
            } // end catch
            catch (IOException ex) {
                displayResult("\nClient terminated connection");
            } finally {
                closeConnection(); //  close connection

            }
        }

        private void processConnection() throws IOException {
            String message = "\nSERVER>>> Connection successful";
            output.writeObject(message); // send connection successful message
            displayResult(message);
            displayResult("\nSERVER>>> Waiting for new connection\n");
            do // process messages sent from client
            {
                try // read message and display it
                {
                    message = (String) input.readObject(); // read new message
                    String[] m = message.split(" ");
                    String command = m[0] != "" ? m[0] : m[1]; //понякога парсването се получава като слага първия елемент ""
                    if("Register".equals(command)) { //ако клиентът иска да се регистрира
                        try {
                            User user = new User(m[1], m[2]); //създавам потребител
                            if(collection.isUserExists(m[1])) { //ако вече съществува такъв по условие се затваря връзката
                                closeConnection();
                            }
                            collection.addUser(user);
                            collection.saveInXMLFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if("Login".equals(command)) {
                        if(collection.isUserExists(m[2])&&collection.isCredentialsCorrect(m[2],m[3])) {
                            closeConnection();
                        }
                    } else if("AddCard".equals(command)) {
                        try {
                            collection.getUser(m[1]).addCard(new Card(m[2]));
                            collection.saveInXMLFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    displayResult("\n" + message); // display message
                }
                catch (ClassNotFoundException classNotFoundException) {
                    displayResult("\nUnknown object type received");
                }

            } while (!message.equals("CLIENT>>> TERMINATE"));
        }

        private void closeConnection() {
            displayResult("\nTerminating connection No." + this.counter + "\n");
            try {
                if (output != null) {
                    output.close();
                }
                if (output != null) {
                    writers.remove(Thread.currentThread());
                }
                if (input != null) {
                    input.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void runServer() { //стартирам сървъра и го настроивам да чака свързвания

        try
        {
            server = new ServerSocket(12345, 100); // create ServerSocket
            displayResult("Waiting for connection\n");
            while (true) {
                waitForConnection(); // wait for a connection
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {

        Socket s = server.accept();

        RunClient client = new RunClient(s, counter);
        threadExecutor.execute(client);

        counter++;
    }

    private void sendData(String text) {
        try
        {
            displayResult(text);
            Set<Thread> threads = writers.keySet();
            for (Thread thread : threads) {
                writers.get(thread).writeObject(text);
                writers.get(thread).flush();
            }
        }
        catch (IOException ioException) {
            main.textArea.appendText("Error writing object");
        }
    }

    private void displayResult(final String message) { //показва съобщение на прозореца и изпраща на клиента
        Platform.runLater(() -> {
            main.textArea.appendText(message);
            String[] s = message.split("[\\s\\n]");
            String command = s[0] != "" ? s[0] : s[1];
            User user=null;
            switch (command) {
                case "Login":
                    if(collection.isUserExists(s[2])&&collection.isCredentialsCorrect(s[2],s[3])) {
                        sendData("Login(OK) " + collection.getUser(s[2]).getCanEncrypt());
                    } else {
                        sendData("Uncorrect credentials");
                    }
                    break;
                case "Register":
                    sendData("Register(OK) "+ collection.getUser(s[2]).getCanEncrypt());
                    break;
                case "AddCard":
                    sendData("Added card "+ s[3]);
                    break;
                case "Encrypt":
                    user = collection.getUser(s[2]);
                    if(user.isCardMine(s[3])) {
                        Card card;
                        try {
                            card = new Card(s[3]);
                        } catch (InvalidPropertiesFormatException e) {
                            throw new RuntimeException(e);
                        }
                        sendData("Encrypt "+cardsController.getSearchedCard(card.getCardNumber()).cryptCard());
                    } else {
                        sendData("Sorry, this card is not your");
                    }
                    break;
                case "Decrypt":
                    user = collection.getUser(s[2]);
                    if(cardsController.isDecryptCardYour(user, s[3])) {
                        sendData("Decrypt "+cardsController.getCardYouWantToDecrypt(s[3]).decryptCard());
                    } else {
                        sendData("Sorry, this card is not your");
                    }
                    break;
                default:
                    break;
            }
        }
        );
    }

    @Override
    public void stop() {
        Platform.exit();
        threadExecutor.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}

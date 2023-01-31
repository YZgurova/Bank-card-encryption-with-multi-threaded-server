package user;

import card.Card;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

import static XMLSerialization.SerializationAndDeserialization.deserializeXML;
import static XMLSerialization.SerializationAndDeserialization.serializeToXML;

public class UsersController {
    private HashMap<String, User> collection;

    public UsersController() throws IOException {
        collection = new HashMap<>();
        setUsers();
    }

    public HashMap<String, User> getUsers() {
        return collection;
    }

    public User getUser(String username) {
        return collection.get(username);
    }

    public void setUsers() throws IOException {
        List<User> users = deserializeXML(); //взима данните от xml файла

        for (int i = 0; i < users.size(); i++) {
            collection.put(users.get(i).getUsername(), users.get(i)); //и пълни колекцията
        }
    }

    public void addUser(User user) throws IOException {
        collection.put(user.getUsername(), user);
//        serializeToXML(user);
    }

    public boolean isUserExists(String username) {
        return collection.containsKey(username);
    } //проверява дали потребителят съществува

    public boolean isCredentialsCorrect(String username, String password) {
        return Objects.equals(collection.get(username).getPassword(), password); //проверява дали има такива креденшъли
    }

    public void saveInXMLFile() throws IOException {
        serializeToXML(collection); //запазва колекцията в xml файл
    }
}

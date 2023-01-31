package user;

import card.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static XMLSerialization.SerializationAndDeserialization.serializeToXML;

public class User {
    private String username;
    private String password;
    private Boolean canEncrypt;
    private List<Card> cards;

    public User(String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        this.canEncrypt = false;
        this.cards = new ArrayList<>();
    }

    public User(String username, String password, Card card) throws IOException {
        this.username = username;
        this.password = password;
        this.cards = new ArrayList<>();
        cards.add(card);
    }

    public User() {
        username=null;
        password=null;
        this.cards = new ArrayList<>();
    }

//    public void serialize() throws IOException {
//        serializeToXML(this);
//    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Card> getCards() {
        return cards.stream().sorted().collect(Collectors.toList());
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Boolean getCanEncrypt() {
        return canEncrypt;
    }

    public void setCanEncrypt(Boolean canEncrypt) {
        this.canEncrypt = canEncrypt;
    }



    public void addCard(Card card) throws IOException {
        cards.add(card);
//        serializeToXML(this);
    }

    public boolean isCardMine(String cardNum) {
        for (Card card : cards) {
            if (Objects.equals(card.getCardNumber(), cardNum)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", canEncrypt=" + canEncrypt +
                '}';
    }
}

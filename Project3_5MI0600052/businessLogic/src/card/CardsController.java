package card;

import user.User;

import java.util.*;

public class CardsController {

    private HashMap<String, User> collection;

    public CardsController(HashMap<String, User> collection) {
        this.collection = collection;
    }

    public Card getSearchedCard(String cardNum) { //минава през колекцията от потребители и търси дали някой от тях притежава търсената карта
        for (Map.Entry<String, User> entry : collection.entrySet()) {
            List<Card> cards = entry.getValue().getCards();
            for (int i = 0; i < cards.size(); i++) {
                if(Objects.equals(cards.get(i).getCardNumber(), cardNum)) {
                    return cards.get(i);
                }
            }
        }
        return null;
    }

    public boolean isCardExist(String cardNum) { //булев метод дали картата съществува
        for (Map.Entry<String, User> entry : collection.entrySet()) {
            List<Card> cards = entry.getValue().getCards();
            for (int i = 0; i < cards.size(); i++) {
                if(Objects.equals(cards.get(i).getCardNumber(), cardNum)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isDecryptCardYour(User user, String cardNumber) { //проверява дали картата, която потребителят иска да декриптира е негова
        Card card = getCardYouWantToDecrypt(cardNumber);
        return user.isCardMine(card.getCardNumber());
    }

    public String getAsStringEncryptedCardNum(Integer[] card) { //приема криптиран резултат като масив и го връща като стринг
        StringBuilder builder = new StringBuilder();
        for (Integer integer : card) {
            builder.append(integer);
        }
        return builder.toString();
    }

    public Card getCardYouWantToDecrypt(String cryptNumber) { // подаваме криптиран номер на карта и намираме обекта му
        for (Map.Entry<String, User> entry:collection.entrySet()) {
            List<Card> cards = entry.getValue().getCards();
            for (int i = 0; i < cards.size(); i++) {
                List<Integer[]> crypts = cards.get(i).getCardCryptography();
                for (int j = 0; j < crypts.size(); j++) {
                    if(getAsStringEncryptedCardNum(crypts.get(j)).equals(cryptNumber)) {
                        return cards.get(i);
                    }
                }
            }
        }
        return null;
    }
}

package SaveCardsInFile;

import card.Card;
import card.CardsController;
import user.User;
import user.UsersController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Save {
    private UsersController collection;
    private CardsController controller;

    public Save(UsersController collection, CardsController controller) {
        this.collection = collection;
        this.controller = controller;
    }

    public void saveInFileSortedByCardNumber() { //запазва картите във файл сортирани по номер на карта
        Path filePath = Paths.get("sortedCardsByCardNum.txt");
        try {
            for (Map.Entry<String, User> entry : collection.getUsers().entrySet()) {
                List<Card> cards = entry.getValue().getCards();
                cards = cards.stream().sorted(Comparator.comparing(Card::getCardNumber)).collect(Collectors.toList());
                Files.writeString(filePath, '\n'+"------------------"+'\n', StandardOpenOption.CREATE);
                for (int i = 0; i < cards.size(); i++) {
                    Files.writeString(filePath, "Card num:\n " + cards.get(i).getCardNumber()+'\n', StandardOpenOption.APPEND);
                    Files.writeString(filePath, "Encrypts:\n ", StandardOpenOption.APPEND);
                    int countCrypts= cards.get(i).getCardCryptography()==null ? 0 : cards.get(i).getCardCryptography().size();
                    for (int j = 0; j < countCrypts; j++) {
                        Files.writeString(filePath, controller.getAsStringEncryptedCardNum(cards.get(i).getCardCryptography().get(j)) + '\n',StandardOpenOption.APPEND);
                    }
                }
                Files.writeString(filePath, "------------------"+'\n', StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Save in file "+e);
        }
    }

    public void saveInFile() { //запазва картите във файл сортирани по криптираните номера
        Path filePath = Paths.get("sortedCardsByEncryption.txt");
        try {
            for (Map.Entry<String, User> entry : collection.getUsers().entrySet()) {
                List<Card> cards = entry.getValue().getCards();
                cards = cards.stream().sorted(Comparator.comparing(Card::getAsStringLastEncryptedCardNum)).collect(Collectors.toList());
                Files.writeString(filePath, '\n'+"------------------"+'\n', StandardOpenOption.CREATE);
                for (int i = 0; i < cards.size(); i++) {
                    Files.writeString(filePath, "Card num:\n " + cards.get(i).getCardNumber()+'\n', StandardOpenOption.APPEND);
                    Files.writeString(filePath, "Encrypts:\n ", StandardOpenOption.APPEND);
                    int countCrypts= cards.get(i).getCardCryptography()==null ? 0 : cards.get(i).getCardCryptography().size();
                    for (int j = 0; j < countCrypts; j++) {
                        Files.writeString(filePath,  controller.getAsStringEncryptedCardNum(cards.get(i).getCardCryptography().get(j)) + '\n',StandardOpenOption.APPEND);
                    }
                }
                Files.writeString(filePath, "------------------"+'\n', StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Save in file "+e);
        }
    }
}

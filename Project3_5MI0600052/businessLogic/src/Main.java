import SaveCardsInFile.Save;
import card.Card;
import card.CardsController;
import user.User;
import user.UsersController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        UsersController controller = new UsersController();
        CardsController cardsController = new CardsController(controller.getUsers());
        User u = new User("user", "123");
        controller.addUser(u);
        Card c = new Card("9563960122001999");
        u.addCard(c);
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.cryptCard());
        System.out.println(c.decryptCard());
        System.out.println(c.cryptCard());

        Save s = new Save(controller, cardsController);

        s.saveInFileSortedByCardNumber();
        s.saveInFile();


        System.out.println(u.getCards());
    }
}
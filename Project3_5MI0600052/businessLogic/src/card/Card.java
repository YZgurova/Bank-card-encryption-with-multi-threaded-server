package card;

//import cipher.CipherMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import static cipher.CipherMethod.decrypt;
import static cipher.CipherMethod.encrypt;

public class Card {
    final Integer CARD_NUMBER_LEN = 16; //номерът на картата се състои от 16 цифри
    private String cardNumber; //номера на картата
    private List<Integer[]> cardCryptography; //лист от енкриптнатите номера на картата
    private int lastShift; //последният шифт, с който е била отместена картата
    private int mod; //процентното делене на 10 или на 16 е

    public Card() {}

    public Card(String cardNumberInput) throws InvalidPropertiesFormatException {
        this.cardCryptography = new ArrayList<>();
        lastShift=5;
        mod=10;
        setCardNumber(cardNumberInput);
    }

    public int getLastShift() {
        return lastShift;
    }

    public void setLastShift(int lastShift) {
        this.lastShift = lastShift;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getAsStringLastEncryptedCardNum() { //метод, който връща като стринг последния криптиран номер
        StringBuilder builder = new StringBuilder();
        Integer[] card = cardCryptography.get(cardCryptography.size()-1);
        for (Integer integer : card) {
            builder.append(integer);
        }
        return builder.toString();
    }

    public Integer[] getLastEncryptedCardNum() {
        return cardCryptography.get(cardCryptography.size()-1);
    } //метод, който връща като масив последния криптиран номер

    public void setCardNumber(String cardNumber) throws InvalidPropertiesFormatException {
        int[] num = Arrays.stream(cardNumber.split("")).mapToInt(Integer::parseInt).toArray();
        if(!isValid(num) || (num[0]==3 || num[0]==4 || num[0]==5 || num[0]==6)) { //проверява дали номерът на картата изпълнява условията за валидност
           throw new InvalidPropertiesFormatException("Invalid card number");
        }
        this.cardNumber = cardNumber;
    }

    public List<Integer[]> getCardCryptography() {
        return cardCryptography;
    }

    public void setCardCryptography(List<Integer[]> cardCryptography) {
        this.cardCryptography = cardCryptography;
    }

    private boolean isValid(int[] cardNumber) { //Luhn
        int sum = 0;
        int parity = cardNumber.length % 2;
        for (int i = 0; i < cardNumber.length; i++) {
            if (i % 2 == parity) {
                sum += cardNumber[i];
            } else if (cardNumber[i] > 4) {
                sum += 2 * cardNumber[i] - 9;
            } else {
                sum += 2 * cardNumber[i];
            }
        }
        return sum % 10 == 0;
    }


    public String cryptCard() { //криптира карта
        if(lastShift>=12) {
            return "This card was blocked";
        }
        if(lastShift>5){
            mod=16;
        }
        cardCryptography.add(encrypt(cardNumber, lastShift, mod, CARD_NUMBER_LEN)); //добавя резултата към листа с криптограмите
        lastShift++;
        return getAsStringLastEncryptedCardNum(); //връща резултата като стринг
    }

    public String decryptCard() {
        return decrypt(getLastEncryptedCardNum(), lastShift, mod, CARD_NUMBER_LEN); //декриптира карта
    }


    @Override
    public String toString() {
        return "CardNumber{" +
                "CARD_NUMBER_LEN=" + CARD_NUMBER_LEN +
                ", cardNumber=" + cardNumber +
                ", cardCryptography=" + cardCryptography +
                ", lastShift=" + lastShift +
                '}';
    }
}

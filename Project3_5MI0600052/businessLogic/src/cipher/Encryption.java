package cipher;

import java.util.Arrays;

public class Encryption {

    public Encryptable getMonoCipherMethod(){
        return new MonoEncryption();
    }

    public class MonoEncryption implements Encryptable {
        @Override
        public Integer[] encrypt(String cardNum, int shift, int mod, int CARD_NUMBER_LEN) {
            Integer[] cardNumArr = new Integer[16];
            for (int i = 0; i < CARD_NUMBER_LEN; i++) {
                cardNumArr[i] = cardNum.charAt(i) - '0';
            } //string -> Integer[]

            for (int i = 0; i < CARD_NUMBER_LEN; i++) {
                cardNumArr[i] = ((cardNumArr[i] + shift) % mod); //криптиране
            }
            return cardNumArr;
        }

        @Override
        public String decrypt(Integer[] cardNum, int shift,int mod, int CARD_NUMBER_LEN) {
            StringBuilder decryptedCardNumber = new StringBuilder();

            for (int i = 0; i < CARD_NUMBER_LEN; i++) {
                int value = cardNum[i];
                value = (value - (shift-1) + mod) % mod; //от текущия елемент изваждаме shift-1 защото след всяко криптиране го учеличаваме с едно, тоест е криптирано с предишния шифър
                decryptedCardNumber.append(value);
            }
            return decryptedCardNumber.toString();
        }
    }
}

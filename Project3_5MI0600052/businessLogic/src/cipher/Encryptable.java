package cipher;

public interface Encryptable {
    Integer[] encrypt(String cardNum, int shift, int mod, int CARD_NUMBER_LEN);

    String decrypt(Integer[] cardNum, int shift,int mod, int CARD_NUMBER_LEN);
}

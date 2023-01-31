package cipher;

public class CipherMethod {
    private static Encryptable callCipher; //помощен клас съдържащ самата логика за криптиране

    public static Integer[] encrypt(String cardNum, int shift, int mod, int CARD_NUMBER_LEN) { //криптира
        Encryption encryption = new Encryption();
        callCipher = encryption.getMonoCipherMethod();
        return callCipher.encrypt(cardNum, shift,mod, CARD_NUMBER_LEN);
    }

    public static String decrypt(Integer[] cardNum, int shift, int mod, int CARD_NUMBER_LEN) { //декриптира
        Encryption encryption = new Encryption();
        callCipher = encryption.getMonoCipherMethod();
        return callCipher.decrypt(cardNum, shift,mod, CARD_NUMBER_LEN);
    }
}

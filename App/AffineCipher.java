/**
 * This class provides a simple implementation of the Affine Cipher
 * This implementation only supports encryption of lowercase letters
 */
public class AffineCipher {
    private String password;

    /**
     * Constructor to initialize the password
     * 
     * @param password The plaintext to be encrypted
     */
    public AffineCipher(String password) {
        this.password = password;
    }

    /**
     * Encrypts the stored password using a mathematical formula
     * Non-alphabetic characters remain unchanged
     * 
     * @return The encrypted string
     */
    public String encrypt() {
        String passwordText = password;
        final char[] charPlainText = passwordText.toCharArray();
        final char[] charCipher = new char[charPlainText.length];
        int letter_value = 0;

        for (int i = 0; i < charPlainText.length; i++) {
            if ('a' <= charPlainText[i] && charPlainText[i] <= 'z') {
                letter_value = (int) charPlainText[i] - 'a';
                charCipher[i] = (char) ((5 * letter_value + 8) % 26 + 'a');

            } else {
                charCipher[i] = charPlainText[i];
            }

        }
        return new String(charCipher);
    }

}
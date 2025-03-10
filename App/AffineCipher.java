public class AffineCipher {
    private String password;

    public AffineCipher(String password) {
        this.password = password;
    }

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
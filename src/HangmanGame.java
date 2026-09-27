import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;



public class HangmanGame {

    private static final String DICTIONARY_PATH = "dictionary.txt";
    private static final int MAX_ERRORS = 7;


    public static void main(String[] args) {
        try {
            playGame();
        } catch (IOException e) {
            System.err.println("Ошибка чтения словаря: " + e.getMessage());
        }
    }

    private static void playGame() throws IOException {
        String dict = getRandomWordFromFile(DICTIONARY_PATH);
        char[] mask = createMask(dict.length());
        int errorChar = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printGameState(mask, errorChar);

                if (winGame(mask, dict)) {
                    System.out.println("И вы выйграли ААААААААААААААВТОМОБИЛЬ!!!" + dict);
                    break;
                }
                if (isLose(errorChar)) {
                    System.out.println("Иди повышай эрудицию, не угадал ты слово, а было оно: " + dict);
                    break;
                }

                Character guess = reedGuess(scanner);
                boolean open = applyGuess(dict, mask, guess);

                if (!open) {
                    errorChar++;
                    System.out.println("Не в этот раз, буква не найдена...");
                }
            }
        }
    }

    private static String getRandomWordFromFile(String fileName) throws IOException {
        List<String> words = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim().replaceAll("[,.]", "");
            if (!line.isEmpty()) {
                words.add(line);

            }
        }
        reader.close();

        if (words.isEmpty()) {
            throw new IOException("Словарь пустой");
        }

        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    private static char[] createMask(int length) {
        char[] mask = new char[length];
        for (int i = 0; i < mask.length; i++) {
            mask[i] = '*';
        }
        return mask;
    }

    private static Character reedGuess(Scanner scanner) {
        System.out.print("Введите букву: ");
        String input = scanner.next().toLowerCase();

        if (input.isEmpty()) {
            return null;
        }

        char symbol = input.charAt(0);

        if (!input.matches("[а-я]")) {
            System.out.println("ВНИМАНИЕ! вводите только одну русскую букву!");
            return reedGuess(scanner);
        }
        return symbol;
    }

    private static boolean applyGuess(String dict, char[] mask, char guess) {
        boolean found = false;

        for (int i = 0; i < dict.length(); i++) {
            if (Character.toLowerCase(dict.charAt(i)) == guess) {
                mask[i] = dict.charAt(i);
                found = true;
            }
        }
        return found;
    }

    private static void printGameState(char[] mask, int errors) {
        System.out.println("Текущее слово: " + new String(mask));
        System.out.println("Колличество ошибок: " + errors + " из " + MAX_ERRORS);
    }


    private static boolean winGame(char[] mask, String word) {
        return new String(mask).equals(word);

    }


    private static boolean isLose(int errors) {
        return errors >= MAX_ERRORS;
    }
}



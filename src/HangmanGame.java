import java.io.*;
import java.util.*;


public class HangmanGame {

    private static final String DICTIONARY_PATH = "dictionary.txt";
    private static final int MAX_ERRORS = 7;
    private static final int START = 1;
    private static final int QUIT = 2;
    private static final String YES = "y";
    private static final String NO = "n";

    private static final String[] GALLOWS = {
            """
             +---+
             |   |
                 |
                 |
                 |
                 |
            =========""",
            """
             +---+
             |   |
             O   |
                 |
                 |
                 |
            =========""",
            """
             +---+
             |   |
             O   |
             |   |
                 |
                 |
            =========""",
            """
             +---+
             |   |
             O   |
            /|   |
                 |
                 |
            =========""",
            """
             +---+
             |   |
             O   |
            /|\\  |
                 |
                 |
            =========""",
            """
             +---+
             |   |
             O   |
            /|\\  |
            /    |
                 |
            =========""",
            """
             +---+
             |   |
             O   |
            /|\\  |
            / \\  |
                 |
            =========""",
            """
             +---+
             |   |
             O   |
            /|\\  |
            / \\  |
            |    |
            =========="""
    };



    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); //globalScaner заменён на scanner.

        showWelcome();

        while (true) {
            int choise = showMenu(scanner);

            switch (choise) {
                case START -> startGameLoop(scanner);
                case QUIT -> {
                    System.out.println("Спасибо за игру!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова!");
            }
        }
    }

    private static void showWelcome() {
        System.out.println("==========================================");
        System.out.println("      ДОБРО ПОЖАЛОВАТЬ В ВИСЕЛИЦУ!        ");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("Правила игры:");
        System.out.println(START + ". Игра загадывает слово, а вы отгадываете его по буквам.");
        System.out.println(QUIT + ". Если буква есть в слове - она открывается.");
        System.out.println("3. Если буквы нет - рисуется часть виселицы.");
        System.out.println("4. У вас есть " + MAX_ERRORS + " попыток(ошибок)");
        System.out.println("5. Словарь можно изменить под себя, добавляя или удаляя нужные слова. Файл словаря лежит в корне программы");

    }

    private static int showMenu(Scanner scanner) {
        System.out.println("ГЛАВНОЕ МЕНЮ");
        System.out.println(START + ". Начать игру");
        System.out.println(QUIT + ". Выход");
        System.out.println("Выберите пункт меню");


        while (!scanner.hasNextInt()) {
            System.out.printf("Выберите именно цифру(%d или %d): \n", START, QUIT);
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // очистка буфера от символа новой строки
        return choice;

    }


    private static void startGameLoop(Scanner scanner) {
        while (true) {
            try {
                String word = getRandomWordFromFile(DICTIONARY_PATH);
                char[] mask = createMask(word.length());
                int errorCounter = 0;
                Set<Character> guessedLetters = new HashSet<>();

                while (true) {
                    printGallows(errorCounter);
                    printGameState(mask, errorCounter);


                    if (isWin(mask, word)) {
                        System.out.println("И вы выйграли ААААААААААААААВТОМОБИЛЬ!!!" + word);
                        break;
                    }
                    if (isLose(errorCounter)) {
                        printGallows(MAX_ERRORS);
                        System.out.println("Иди повышай эрудицию, не угадал ты слово, а было оно: " + word);
                        break;
                    }

                    char guess = readGuess(scanner, guessedLetters);
                    guessedLetters.add(guess); // пометка буквы как введённой

                    if (!applyGuess(word, mask, guess)) {
                        errorCounter++;
                        System.out.println("Не в этот раз, буква не найдена...");
                    }
                }


                System.out.print("\nСыграть ещё раз? (y/n): ");
                Scanner console = new Scanner(System.in);
                String answer = console.nextLine().trim().toLowerCase();

                if (!answer.equals(YES) && !answer.equals(NO)) {
                    System.out.println("Возвращаемся в главное меню...");
                    break;
                }
                System.out.println("Начинаем новую игру!");

            } catch (IllegalStateException e) {
                System.err.println("Critical error: " + e.getMessage());
                System.err.println("Check for the presence of the file ‘%s’ in the program root.  \\n\", DICTIONARY_PATH");
                break;

            }
        }
    }



    private static String getRandomWordFromFile(String fileName) {
        List<String> words = readFile(fileName);
        return getRandomWord(words);
    }

    private static List<String> readFile(String fileName) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().replaceAll("[,.]", "");
                if (!line.isEmpty()) {
                    words.add(line);

                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("\"Dictionary file not found or cannot be read\", e");
        }
        return words;
    }

    private static String getRandomWord(List<String> words) {

        if (words.isEmpty()) {
            throw new IllegalStateException("Dictionary is empty");
        }

        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    private static char[] createMask(int length) {
        char[] mask = new char[length];
        Arrays.fill(mask, '*');
        return mask;
    }

    private static char readGuess(Scanner scanner, Set<Character> guessedLetters) {

        while (true) {
            System.out.print("Введите букву: ");
            String input = scanner.nextLine().toLowerCase();

            if (input.isEmpty()) {
                continue;
            }

            char symbol = input.charAt(0);

            if (!input.matches("[а-яё]")) {
                System.out.println("ВНИМАНИЕ! вводите только одну русскую букву!");
                continue;
            }

            if(guessedLetters.contains(symbol)){
                System.out.println("Вы уже вводили эту букву. Попробуйте другую.");
                continue;
            }
            return symbol;
        }
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
        System.out.printf("Количество ошибок: %d из %d  \n", errors, MAX_ERRORS);
    }


    private static boolean isWin(char[] mask, String word) {
        return new String(mask).equals(word);

    }


    private static boolean isLose(int errors) {
        return errors >= MAX_ERRORS;
    }

    private static void printGallows(int errors) {
        System.out.println(GALLOWS[errors]);
    }
}




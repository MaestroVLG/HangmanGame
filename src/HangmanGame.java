import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class HangmanGame {

    private static final String DICTIONARY_PATH = "dictionary.txt";
    private static final int MAX_ERRORS = 7;


    static void main(String[] args)  {

        Scanner globalScanner = new Scanner(System.in);

        showWelcome();

        while (true) {
            int choise = showMenu();

            switch (choise) {
                case 1:
                    startGameLoop(globalScanner);
                    break;
                case 2:
                    System.out.println("Спасибо за игру!");
                    globalScanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова!");
            }
        }
    }

    private static void showWelcome() {
        System.out.println("==========================================");
        System.out.println("      ДОБРО ПОЖАЛОВАТЬ В ВИСЕЛИЦУ!        ");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("Правила игры:");
        System.out.println("1. Игра загадывает слово, а вы отдагываете его по буквам.");
        System.out.println("2. Если буква есть в слове - она открывается.");
        System.out.println("3. Если буквы нет - рисуется часть висилицы.");
        System.out.println("4. У вас есть " + MAX_ERRORS + " попыток(ошибок)");
        System.out.println("5. Словарь можно изменить под себя, добавляя или удаляя нужные слова. Файл словаря лежит в корне программы");

    }

    private static int showMenu() {
        System.out.println("ГЛАВНОЕ МЕНЮ");
        System.out.println("1. Начать игру");
        System.out.println("2. Выход");
        System.out.println("Выбирите пункт меню");

        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            System.out.println("Выбирите именно цифру(1 или 2): ");
            scanner.next();
        }
        return scanner.nextInt();

    }


    private static void startGameLoop(Scanner scanner)  {

        while (true) {
            try {
                String dict = getRandomWordFromFile(DICTIONARY_PATH);
                char[] mask = createMask(dict.length());
                int errorChar = 0;

                    while (true) {
                        printGameState(mask, errorChar);
                        printGallows(errorChar);


                        if (winGame(mask, dict)) {
                            System.out.println("И вы выйграли ААААААААААААААВТОМОБИЛЬ!!!" + dict);
                            break;
                        }
                        if (isLose(errorChar)) {
                            printGallows(MAX_ERRORS);
                            System.out.println("Иди повышай эрудицию, не угадал ты слово, а было оно: " + dict);
                            break;
                        }

                        Character guess = readGuess(scanner);
                        boolean open = applyGuess(dict, mask, guess);

                        if (!open) {
                            errorChar++;
                            System.out.println("Не в этот раз, буква не найдена...");
                        }
                    }


                System.out.print("\nСыграть ещё раз? (y/n): ");
                Scanner console = new Scanner(System.in);
                String answer = console.nextLine().trim().toLowerCase();

                if (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Возвращаемся в главное меню...");
                    break;
                }
                System.out.println("Начинаем новую игру!");

            } catch (IOException e) {
                System.err.println("Ошибка: " + e.getMessage());
                System.err.println("Проверьте наличие фала dictionary.txt в корне программы");
                return;

            }
        }
        }

            private static String getRandomWordFromFile (String fileName) throws IOException {
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

            private static char[] createMask ( int length){
                char[] mask = new char[length];
                for (int i = 0; i < mask.length; i++) {
                    mask[i] = '*';
                }
                return mask;
            }

            private static Character readGuess (Scanner scanner){
                System.out.print("Введите букву: ");
                String input = scanner.nextLine().toLowerCase();

                if (input.isEmpty()) {
                    return null;
                }

                char symbol = input.charAt(0);

                if (!input.matches("[а-я]")) {
                    System.out.println("ВНИМАНИЕ! вводите только одну русскую букву!");
                    return readGuess(scanner);
                }
                return symbol;
            }

            private static boolean applyGuess (String dict,char[] mask, char guess){
                boolean found = false;

                for (int i = 0; i < dict.length(); i++) {
                    if (Character.toLowerCase(dict.charAt(i)) == guess) {
                        mask[i] = dict.charAt(i);
                        found = true;
                    }
                }
                return found;
            }

            private static void printGameState ( char[] mask, int errors){
                System.out.println("Текущее слово: " + new String(mask));
                System.out.println("Колличество ошибок: " + errors + " из " + MAX_ERRORS);
            }


            private static boolean winGame ( char[] mask, String word){
                return new String(mask).equals(word);

            }


            private static boolean isLose ( int errors){
                return errors >= MAX_ERRORS;
            }

            private static void printGallows ( int errors){
                System.out.println(" +---+");
                System.out.println(" |  |");
                if (errors >= 1) {
                    System.out.println(" O  |");
                } else {
                    System.out.println(" |");
                }
                if (errors >= 3) {
                    System.out.println("/|\\ |");
                } else if (errors == 2) {
                    System.out.println(" | |");
                } else {
                    System.out.println(" |");
                }
                if (errors >= 5) {
                    System.out.println("/ \\ |");
                } else if (errors == 4) {
                    System.out.println("/ |");
                } else {
                    System.out.println(" |");
                }
                System.out.println(" |");
                System.out.println("=========");

            }
        }




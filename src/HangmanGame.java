import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class HangmanGame {

//    static String dictionary = "dictionary.txt";
//
//    static List<String> dictionarys = new ArrayList<>();


    public static void main(String[] args) {


        String test = "проверка";

        char[] maska = new char[test.length()];
        for (int i = 0; i < maska.length; i++) {
            maska[i] = '*';
        }

        int errorChar = 0;

        Scanner scanner = new Scanner(System.in);

        boolean gameStatus = false;
        boolean stopGame = false;

        while (!stopGame && !new String(maska).equals(test)) {
            System.out.println("Введите букву: ");


            String input = scanner.nextLine();
            if (input.isEmpty()) continue; {

                char symbols = Character.toLowerCase(input.charAt(0));


                for (int i = 0; i < test.length(); i++) {
                    if (Character.toLowerCase(test.charAt(i)) == symbols) {
                        maska[i] = test.charAt(i);
                        gameStatus = true;
                    }

                }

                if (gameStatus) {
                    System.out.println("Есть такая буква! Показываем! " + new String(maska));
                }if(errorChar == 7){
                    System.out.println("Иди повышай эрудицию, не угадал ты слово, а было оно: " + test);
                    break; }
                else {
                    System.out.println("Не в этот раз, буква не найдена...");
                    errorChar += 1;
                    System.out.println("Колличество ошибок: " + errorChar);
                }
            }
        } if (new String(maska).equals(test)){
            System.out.println("И вы выйграли ААААААААААААААВТОМОБИЛЬ!!!" + test);
        }
    }
}


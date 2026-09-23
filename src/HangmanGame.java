import java.util.ArrayList;
import java.util.List;


public class HangmanGame {

    static String dictionary = "dictionary.txt";

    static List<String> dictionarys = new ArrayList<>();


    public static void main(String[] args) {
        //todo запуск игры
    }

    private static void greetUser(){
        System.out.println("Добро пожаловать в игру 'Виселица'! \n " +
                "Правила игры простые, есть загаданное слово и возможность допустить не более 7 ошибок(один символ = один шаг \n " +
                "Загаданное слово является существительным, нарицательным в именительном падеже \n" +
                "на 4 ошибке выходит подсказка(одна буква имеющаяся в данном слове");
        return gameMenu();

    }

    private static void gameMenu(){

    }
}

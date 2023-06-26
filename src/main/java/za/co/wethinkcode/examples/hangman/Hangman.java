package za.co.wethinkcode.examples.hangman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;


public class Hangman {

    public static void main(String[] args) throws IOException {
        Player player = new Player();
        Answer wordToGuess =  getWords(player);
        Answer currentAnswer = start(player, wordToGuess);
        String message = run(player, wordToGuess, currentAnswer);
        System.out.print(message);
    }

    public static Answer getWords(Player player) throws IOException {
        Random random = new Random();

        System.out.println("Words file? [leave empty to use short_words.txt]");
        String fileName = player.getWordsFile();

        List<String> words = Files.readAllLines(Path.of(fileName));

        int randomIndex = random.nextInt(words.size());
        String randomWord = words.get(randomIndex).trim();
        return new Answer(randomWord);
    }

    public static Answer start(Player player, Answer wordToGuess) {
        Answer currentAnswer = wordToGuess.generateRandomHint();
        System.out.println("Guess the word: " + currentAnswer);
        return currentAnswer;
    }

    public static String run(Player player, Answer wordToGuess , Answer currentAnswer) {
        /*4*/
        while (!currentAnswer.equals(wordToGuess)) {
            String guess = player.getGuess();

            /*5*/
            if (player.wantsToQuit()) {
                System.out.println("Bye!");
                break;
            }
            /*6*/
            char guessedLetter = guess.charAt(0);
            if (currentAnswer.isGoodGuess(wordToGuess, guessedLetter)) {
                currentAnswer = wordToGuess.getHint(currentAnswer, guess.charAt(0));
                System.out.println(currentAnswer);
            } else {
                /*7*/
                player.lostChance();
                System.out.println("Wrong! Number of guesses left: " + player.getChances());
                /*8*/
                if (player.hasNoChances()) {
                    return "Sorry, you are out of guesses. The word was: " + wordToGuess;
                }
            }
        }
        /*9*/
        return "That is correct! You escaped the noose .. this time.";
    }

}
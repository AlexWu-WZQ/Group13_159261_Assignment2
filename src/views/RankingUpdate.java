package views;

import Others.EncryptFileIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the logic of ranking charaters including file IO
 */
public class RankingUpdate {
    public static Character[] characters = new Character[4];
    private String[] dataDetails;

    //initialize the ranking
    public void initRank() {
        getRankData();
    }

    //for test
    public void resetRank() {
        initializeCharacters();
        writeRankData();
    }

    //save the new rank
    public void updateRank(int characterNum, int score) {
        getRankData();
        updateRankData(characterNum, score);
        writeRankData();
    }

    //initialize the ranking original data
    public void initializeCharacters() {
        characters[0] = new Character(2, 0);
        characters[1] = new Character(1, 0);
        characters[2] = new Character(3, 0);
        characters[3] = new Character(4, 0);
    }

    //wirte the ranking data to file
    public void writeRankData() {
        EncryptFileIO myFileIO = new EncryptFileIO();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            sb.append(i).append(" ").append(characters[i - 1].toString()).append("\n");
        }
        //encrypt data
        try {
            myFileIO.set("src\\images\\rank", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //read the ranking data from file
    public void getRankData() {
        characters[0] = new Character(0, 0);
        characters[1] = new Character(0, 0);
        characters[2] = new Character(0, 0);
        characters[3] = new Character(0, 0);
        EncryptFileIO myFileIO = new EncryptFileIO();
        try {
            String str = myFileIO.load("src\\images\\rank");
            dataDetails = str.split("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //save in characters
        String[] ns;
        for (int i = 0; i < characters.length; i++) {
            ns = dataDetails[i].split(" ");
            characters[i].characterNum = Integer.parseInt(ns[1]);
            characters[i].score = Integer.parseInt(ns[2]);
        }
    }

    //update the rank
    public void updateRankData (int characterNum, int score) {

        //update the new data
        for (Character character : characters) {
            if (character.characterNum == characterNum) {
                character.score = score;
            }
        }
        //sort the data by the score
        List<Character> charactersList = new ArrayList<>(Arrays.asList(characters));
        charactersList.sort((o1, o2) -> Integer.compare(o2.score, o1.score));

        //transfer the sorted data to the original array
        for (int i = 0; i < characters.length; i++) {
            characters[i] = charactersList.get(i);
        }
    }

}
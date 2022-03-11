package com.company;

import java.io.*;
import java.util.Scanner;

public class GameWindow implements Closeable {
    private Player player1, player2;
    private boolean play = true;

    private char[][] array;
    private final Scanner sc;
    private final PrintWriter writer;


    public GameWindow() throws FileNotFoundException {
        init();
        sc = new Scanner(System.in);

        File file = new File("./scores.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer = new PrintWriter(file);
    }

    private void init() {
        System.out.println("  x\t 1   2   3");
        System.out.println('y');
        array = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < 3; j++) {
                array[i][j] = '-';
                System.out.printf("| %s ", array[i][j]);
            }
            System.out.print("|\n\n");
        }
    }

    public void start() {
        addPlayers();
        play();
    }

    private void addPlayers() {
        System.out.print("введите ваше имя: ");
        player1 = new Player(sc.nextLine());
        System.out.print("введите ваше имя: ");
        player2 = new Player(sc.nextLine());
    }

    private void play() {
        byte x, y, count = 0;
        boolean isX = true;
        while (play) {

            System.out.printf("ход %s\n", isX ? "1-го игрока" : "2-го игрока");
            System.out.print("введите координаты (x,y): ");
            x = sc.nextByte();
            y = sc.nextByte();
            if (0 < x && x <= 3 && 0 < y && y <= 3) {
                x -= 1;
                y -= 1;
                if (array[y][x] == '-') {
                    if (isX) {
                        array[y][x] = 'x';
                        isX = false;
                    } else {
                        array[y][x] = '0';
                        isX = true;
                    }
                    count++;
                    refreshConsole();
                } else {
                    System.out.println("введите заново! уже занят");
                    continue;
                }

                if (count >= 5 && count < 9) {
                    if (checkWinner()) {
                        String name;
                        Player winner;
                        if (!isX) {
                            final int score = player1.getScore() + 1;
                            player1.setScore(score);
                            winner = player1;
                        } else {
                            final int score = player2.getScore() + 1;
                            player2.setScore(score);
                            winner = player2;
                        }
                        System.out.println(winner.getName());
                        if (chooseQuiz()){
                            print(winner);
                        }
                    }
                } else if (count == 9) {
                    System.out.println("ничья!!!");
                    chooseQuiz();
                }

            } else {
                System.out.println("введите числа только в диапазоне!!!");
                continue;
            }
            //refreshConsole();
        }
    }

    private void print(Player player) {
        writer.println(player);
        writer.flush();
    }

    private boolean chooseQuiz() {
        System.out.println("хотите сыграть заново?(да/нет)");
        String var = sc.next().trim();
        if (var.equals("да")) {
            init();
            play();
            return false;
        } else if (var.equals("нет")) {
            play = false;
            return true;
        }
        return chooseQuiz();
    }


    private void refreshConsole() {
        System.out.println("  x\t 1   2   3");
        System.out.println('y');
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < 3; j++) {
                System.out.printf("| %s ", array[i][j]);
            }
            System.out.print("|\n\n");
        }
    }

    private boolean checkWinner() {
        if (array[0][0] != '-' && array[0][0] == array[0][1] && array[0][1] == array[0][2])
            return true;
        else if (array[1][0] != '-' && array[1][0] == array[1][1] && array[1][1] == array[1][2])
            return true;
        else if (array[2][0] != '-' && array[2][0] == array[2][1] && array[2][1] == array[2][2])
            return true;

        else if (array[0][0] != '-' && array[0][0] == array[1][0] && array[1][0] == array[2][0])
            return true;
        else if (array[0][1] != '-' && array[0][1] == array[1][1] && array[1][1] == array[2][1])
            return true;
        else if (array[0][2] != '-' && array[0][2] == array[1][2] && array[1][2] == array[2][2])
            return true;

        else if (array[0][0] != '-' && array[0][0] == array[1][1] && array[1][1] == array[2][2])
            return true;
        else return array[0][2] != '-' && array[0][2] == array[1][1] && array[1][1] == array[2][0];
    }

    @Override
    public void close() {
        if (sc != null) sc.close();
        if (writer != null) writer.close();
    }
}

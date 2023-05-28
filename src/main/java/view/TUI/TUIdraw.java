package view.TUI;

import model.Tile;
import model.Token;
import model.abstractModel.Message;
import modelView.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static view.TUI.TUI.*;
import static view.TUI.TUIutils.DEFAULT;
import static view.TUI.TUIutils.getColour;

public class TUIdraw {
    private static final Map<String, String[]> commonGoalRes = getCommonGoalRes();
    static int commandLineWidth = 75;

    static void drawGrid(int startX, int startY, int gridRowDim, int gridColDim, char[][] cliPixel, int[][] cliPixelColor) {
        String middle = "│   ".repeat(gridRowDim) + "│";

        for (int i = 0; i < gridColDim; i++) {
            String pattern;
            if (i == 0)
                pattern = "┬───";
            else
                pattern = "┼───";

            drawString(pattern.repeat(gridRowDim), startY + i * 2, startX, DEFAULT, 60, cliPixel, cliPixelColor);
            drawString(middle, startY + i * 2 + 1, startX, DEFAULT, 60, cliPixel, cliPixelColor);
        }

        drawString("┴───".repeat(gridRowDim), startY + gridColDim * 2, startX, DEFAULT, 60, cliPixel, cliPixelColor);

        char s;
        char t;
        for (int i = 0; i < gridColDim + 1; i++) {
            if (i == 0) {
                s = '┌';
                t = '┐';
            } else if (i == gridColDim) {
                s = '└';
                t = '┘';
            } else {
                s = '├';
                t = '┤';
            }
            cliPixel[startY + i * 2][startX] = s;
            cliPixelColor[startY + i * 2][startX] = DEFAULT;
            cliPixel[startY + i * 2][startX + gridRowDim * 4] = t;
            cliPixelColor[startY + i * 2][startX + gridRowDim * 4] = DEFAULT;
        }
    }

    static void drawGridContents(int startX, int startY, Tile[][] contents, char[][] cliPixel, int[][] cliPixelColor) {
        startX = startX + 1;
        startY = startY + 1;

        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[i].length; j++) {
                int color = getColour(contents[i][j].getColor());
                char c = '█';
                if (color == DEFAULT)
                    c = ' ';

                for (int k = 0; k < 3; k++) {
                    cliPixel[startY + i * 2][k + startX + j * 4] = c;
                    cliPixelColor[startY + i * 2][k + startX + j * 4] = color;
                }
            }
        }
    }

    //draw String from start coordinate
    static void drawString(String toDraw, int row, int startCol, int colour, int size, char[][] cliPixel, int[][] cliPixelColor) {
        if (row < 0 || row >= renderHeight || startCol < 0 || startCol >= renderWidth)
            return;
        if (toDraw.length() > size)
            toDraw = toDraw.substring(0, size);

        for (int i = 0; (i < toDraw.length() && (i + startCol) < (renderWidth - 2)); i++) {
            cliPixel[row][startCol + i] = toDraw.charAt(i);
            cliPixelColor[row][startCol + i] = colour;
        }
    }

    static void drawGameList(List<GamesManagerInfo> games, char[][] cliPixel, int[][] cliPixelColor) {
        // a game will be | game name | max players | current players | joinable |
        // a game will be | 10 chars   | 5 chars      | 5 chars          | 10 chars |
        for (int i = 0; i < games.size(); i++) {
            String toDraw = games.get(i).gameId();
            toDraw += " ".repeat(10 - toDraw.length());
            toDraw += games.get(i).maxPlayers();
            toDraw += " ".repeat(5 - toDraw.length() + 10);
            toDraw += games.get(i).connectedPlayers();
            toDraw += " ".repeat(5 - toDraw.length() + 15);
            if (games.get(i).connectedPlayers() < games.get(i).maxPlayers())
                toDraw += "joinable";
            else
                toDraw += "not joinable";
            toDraw += " ".repeat(10 - toDraw.length() + 25);
            drawString(toDraw, 10 + i, 10, DEFAULT, 50 - 2, cliPixel, cliPixelColor);
        }
    }

    static void drawGameEnd(Map<String, Integer> points, char[][] cliPixel, int[][] cliPixelColor) {
        final int gameEndY = 14;
        final int gameEndX = 50;
        final int gameEndWidth = 36;
        Map<Integer, List<Map.Entry<String, Integer>>> grouped = points.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));
        List<Map.Entry<Integer, List<Map.Entry<String, Integer>>>> sorted = new ArrayList<>(grouped.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList());
        Collections.reverse(sorted);

        drawCenteredString("LEADERBOARD", gameEndX, gameEndY, gameEndWidth, DEFAULT, cliPixel, cliPixelColor);

        String positionTitle = " POSITION ";
        String idTitle = "      ID      ";
        String pointsTitle = "  POINTS  ";

        drawCenteredString(positionTitle + "│" + idTitle + "│" + pointsTitle, gameEndX, gameEndY + 1, gameEndWidth, DEFAULT, cliPixel, cliPixelColor);

        int leaderBoardY = gameEndY + 2;
        int line = 0;
        int position = 1;
        for (Map.Entry<Integer, List<Map.Entry<String, Integer>>> group : sorted) {
            boolean firstOfTheGroup = true;
            for (Map.Entry<String, Integer> player : group.getValue()) {
                String pos;
                String Ppoints;
                if (firstOfTheGroup) {
                    pos = String.valueOf(position);
                    firstOfTheGroup = false;
                } else {
                    pos = "|";
                }
                Ppoints = String.valueOf(player.getValue());
                drawCenteredString(pos, gameEndX, leaderBoardY + line, positionTitle.length(), DEFAULT, cliPixel, cliPixelColor);
                drawCenteredString(player.getKey(), gameEndX + positionTitle.length() + 1, leaderBoardY + line, idTitle.length(), DEFAULT, cliPixel, cliPixelColor);
                drawCenteredString(Ppoints, gameEndX + positionTitle.length() + idTitle.length() + 2, leaderBoardY + line, pointsTitle.length(), DEFAULT, cliPixel, cliPixelColor);
                line++;
            }
            position++;
        }

    }

    @SuppressWarnings("SameParameterValue")
    static void drawCenteredString(String text, int startX, int startY, int spaceWidth, int colour, char[][] cliPixel, int[][] cliPixelColor) {
        StringBuilder title = new StringBuilder();
        int spaceBefore = (spaceWidth - text.length()) / 2;
        title.append(" ".repeat(spaceBefore)).append(text);
        drawString(title.toString(), startY, startX, colour, title.length(), cliPixel, cliPixelColor);
    }

    @SuppressWarnings("SameParameterValue")
    static void drawBox(int row, int col, int height, int width, int colour, char[][] cliPixel, int[][] cliPixelColor) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == 0) cliPixel[row + i][col + j] = '┌';
                else if (i == 0 && j == width - 1) cliPixel[row + i][col + j] = '┐';
                else if (i == height - 1 && j == 0) {
                    cliPixel[row + i][col + j] = '└';
                    cliPixelColor[row + i][col + j] = colour;
                } else if (i == height - 1 && j == width - 1) {
                    cliPixel[row + i][col + j] = '┘';
                    cliPixelColor[row + i][col + j] = colour;
                } else if (i == 0) {
                    cliPixel[row + i][col + j] = '─';
                    cliPixelColor[row + i][col + j] = colour;
                } else if (i == height - 1) {
                    cliPixel[row + i][col + j] = '─';
                    cliPixelColor[row + i][col + j] = colour;
                } else if (j == 0) {
                    cliPixel[row + i][col + j] = '│';
                    cliPixelColor[row + i][col + j] = colour;
                } else if (j == width - 1) {
                    cliPixel[row + i][col + j] = '│';
                    cliPixelColor[row + i][col + j] = colour;
                } else {
                    cliPixel[row + i][col + j] = ' ';
                    cliPixelColor[row + i][col + j] = DEFAULT;
                }
            }
        }
    }

    static void drawChat(PlayerChatInfo currentPlayerChat, char[][] cliPixel, int[][] cliPixelColor) {
        final int chatX = 80;
        final int chatY = 23;
        final int chatBoxWidth = 58;
        final int chatBoxHeight = 28;
        if (currentPlayerChat != null) {
            List<Message> messages = currentPlayerChat.messages();
            Collections.reverse(messages);

            drawBox(chatY + 1, chatX, chatBoxHeight, chatBoxWidth, DEFAULT, cliPixel, cliPixelColor);

            int chatContentsX = chatX + 1;
            int chatContentsY = chatY + 2;
            int chatContentsHeight = chatBoxHeight - 2;
            int chatContentsWidth = chatBoxWidth - 4;

            String[] chatBuffer = new String[chatContentsHeight];
            Arrays.fill(chatBuffer, "");

            int pointer = chatContentsHeight - 1;
            for (Message m : messages) {
                String text = m.getSender() + " to " + ((m.getSubject().isBlank()) ? "Everyone" : m.getSubject()) + ": " + m.getText();
                List<String> temp = new ArrayList<>();

                do {
                    int size = Math.min(text.length(), chatContentsWidth);

                    String s = text.substring(0, size);
                    temp.add(s);


                    text = "    " + text.substring(size);
                } while (!text.isBlank());

                for (int i = temp.size() - 1; i >= 0; i--) {
                    chatBuffer[pointer] = temp.get(i);
                    pointer--;
                    if (pointer < 0)
                        break;
                }

                if (pointer < 0)
                    break;
            }

            for (int i = 0; i < chatBuffer.length; i++) {
                drawString(chatBuffer[i], chatContentsY + i, chatContentsX + 1, DEFAULT, chatBuffer[i].length(), cliPixel, cliPixelColor);
            }

            drawCenteredString("CHAT", chatX, chatY, chatBoxWidth, DEFAULT, cliPixel, cliPixelColor);
        }
    }

    static void drawLivingRoom(LivingRoomInfo currentLivingRoom, char[][] cliPixel, int[][] cliPixelColor) {
        final int livingRoomX = 1;
        final int livingRoomY = 1;
        if (currentLivingRoom != null) {
            Tile[][] board = currentLivingRoom.board();
            drawGrid(livingRoomX + 2, livingRoomY + 2, board[0].length, board.length, cliPixel, cliPixelColor);
            drawGridContents(livingRoomX + 2, livingRoomY + 2, board, cliPixel, cliPixelColor);

            //draw numbers on the top
            for (int i = 0; i < board.length; i++) {
                String number = String.valueOf(i);
                if (i < 10) {
                    number = "  " + number + " ";
                } else {
                    number = " " + number + " ";
                }
                for (int c = 0; c < number.length(); c++) {
                    cliPixel[livingRoomY + 1][livingRoomX + 2 + i * 4 + c] = number.charAt(c);
                    cliPixelColor[livingRoomY + 1][livingRoomX + 2 + i * 4 + c] = DEFAULT;
                }
            }
            //draw numbers on the side
            for (int i = 0; i < board.length; i++) {
                String number = String.valueOf(i);
                if (i < 10) {
                    number = "0" + number;
                }
                for (int c = 0; c < number.length(); c++) {
                    cliPixel[livingRoomY + 3 + i * 2][livingRoomX] = number.charAt(c);
                    cliPixelColor[livingRoomY + 3 + i * 2][livingRoomX] = DEFAULT;
                }
            }

            drawCenteredString("LIVING ROOM BOARD", livingRoomX + 2, livingRoomY, board[0].length * 4 + 1, DEFAULT, cliPixel, cliPixelColor);
        }
    }

    static void drawCommandLine(String cursor, List<Pair> oldCmds, char[][] cliPixel, int[][] cliPixelColor) {


        drawBox(commandLineY + 1, commandLineX, commandLineHeight, commandLineWidth, DEFAULT, cliPixel, cliPixelColor);

        drawCenteredString("COMMAND LINE", commandLineX, commandLineY, commandLineWidth, DEFAULT, cliPixel, cliPixelColor);

        drawString(cursor, commandLineY + commandLineHeight - 1, commandLineX, DEFAULT, commandLineWidth - 3, cliPixel, cliPixelColor);
        drawOldCmds(oldCmds, cliPixel, cliPixelColor);
    }

    private static void drawOldCmds(List<Pair> oldCmds, char[][] cliPixel, int[][] cliPixelColor) {
        while (oldCmds.size() > (commandLineHeight - 4))
            oldCmds.remove(0);

        for (int i = 0; i < oldCmds.size(); i++) {
            drawString(oldCmds.get(i).string(), commandLineY + 2 + i, commandLineX + 1, oldCmds.get(i).colour(), commandLineWidth - 2, cliPixel, cliPixelColor);
        }
    }

    static void drawShelves(Map<String, ShelfInfo> currentShelves, String thisPlayerId, GameInfo currentGameState, char[][] cliPixel, int[][] cliPixelColor) {
        final int shelvesX = 43;

        final int shelvesY = 4;
        final int shelvesPadding = 3;
        if (!currentShelves.isEmpty()) {
            int shelvesGridY = shelvesY + 1;

            int shelvesHeight = 0;
            int shelvesWidth = 0;

            int shelfDrewed = 0;

            StringBuilder playersName = new StringBuilder();
            StringBuilder playersPoints = new StringBuilder();

            for (String playerId : currentShelves.keySet()) {
                Tile[][] shelf = currentShelves.get(playerId).shelf();

                shelvesHeight = shelf.length;
                shelvesWidth = shelf[0].length * 4 + 1;

                int shelfX = shelvesX + (shelvesWidth + shelvesPadding) * shelfDrewed;
                drawGrid(shelfX, shelvesGridY, shelf[0].length, shelf.length, cliPixel, cliPixelColor);
                drawGridContents(shelfX, shelvesGridY, shelf, cliPixel, cliPixelColor);

                String tempPlayerId = playerId;

                if (playerId.equals(thisPlayerId))
                    tempPlayerId = "YOU";

                if (tempPlayerId.length() > shelvesWidth - 4)
                    tempPlayerId = tempPlayerId.substring(0, shelvesWidth - 4);


                if (playerId.equals(currentGameState.playerOnTurn()))
                    tempPlayerId = '>' + tempPlayerId + '<';

                int spaceBefore = (shelvesWidth - tempPlayerId.length()) / 2;
                int spaceAfter = shelvesWidth - spaceBefore - tempPlayerId.length();
                playersName.append(" ".repeat(spaceBefore));
                playersName.append(tempPlayerId);
                playersName.append(" ".repeat(spaceAfter));
                playersName.append(" ".repeat(shelvesPadding));

                String points = "Points: " + currentGameState.points().getOrDefault(playerId, 0);
                spaceBefore = (shelvesWidth - points.length()) / 2;
                spaceAfter = shelvesWidth - spaceBefore - points.length();
                playersPoints.append(" ".repeat(spaceBefore));
                playersPoints.append(points);
                playersPoints.append(" ".repeat(spaceAfter));
                playersPoints.append(" ".repeat(shelvesPadding));

                shelfDrewed++;
            }

            drawString(playersName.toString(), shelvesGridY + shelvesHeight * 2 + 1, shelvesX, DEFAULT, playersName.length(), cliPixel, cliPixelColor);
            drawString(playersPoints.toString(), shelvesGridY + shelvesHeight * 2 + 2, shelvesX, DEFAULT, playersPoints.length(), cliPixel, cliPixelColor);

            int maxSize = currentShelves.size() * shelvesWidth + shelvesPadding * (currentShelves.size() - 1);
            drawCenteredString("PLAYERS SHELVES", shelvesX, shelvesY, maxSize, DEFAULT, cliPixel, cliPixelColor);
        }
    }

    static void drawCommonGoals(Map<String, CommonGoalInfo> commonGoals, Map<String, Token> achievedCommonGoals, char[][] cliPixel, int[][] cliPixelColor) {
        final int commonGoalsY = 23;
        final int commonGoalsX = 3;

        final int commonGoalsPadding = 3;
        final int commonGoalBoxWidth = 23;
        final int commonGoalBoxHeight = 15;

        if (!commonGoals.isEmpty()) {
            int boxesStartY = commonGoalsY + 1;
            StringBuilder points = new StringBuilder();
            int drewedCommonGoals = 0;
            for (String id : commonGoals.keySet()) {
                int boxStartX = commonGoalsX + drewedCommonGoals * (commonGoalBoxWidth + commonGoalsPadding);
                drawBox(boxesStartY, boxStartX, commonGoalBoxHeight, commonGoalBoxWidth, DEFAULT, cliPixel, cliPixelColor);

                String[] res = commonGoalRes.getOrDefault(id, new String[0]);
                for (int j = 0; j < res.length; j++) {
                    drawString(res[j], boxesStartY + 1 + j, boxStartX + 1, DEFAULT, 60, cliPixel, cliPixelColor);
                }

                String temp;
                if (achievedCommonGoals.containsKey(id))
                    temp = "ACHIEVED: " + achievedCommonGoals.get(id).getPoints();
                else
                    temp = "Points: " + commonGoals.get(id).tokenState().getPoints();

                int spaceBefore = (commonGoalBoxWidth - temp.length()) / 2;
                int spaceAfter = commonGoalBoxWidth - spaceBefore - temp.length();
                points.append(" ".repeat(spaceBefore));
                points.append(temp);
                points.append(" ".repeat(spaceAfter));
                points.append(" ".repeat(commonGoalsPadding));

                drewedCommonGoals++;
            }
            drawString(points.toString(), boxesStartY + commonGoalBoxHeight, commonGoalsX, DEFAULT, points.length(), cliPixel, cliPixelColor);

            int maxSize = commonGoals.size() * commonGoalBoxWidth + commonGoalsPadding * (commonGoals.size() - 1);
            drawCenteredString("COMMON GOALS", commonGoalsX, commonGoalsY, maxSize, DEFAULT, cliPixel, cliPixelColor);
        }
    }

    private static Map<String, String[]> getCommonGoalRes() {
        Map<String, String[]> ris = new HashMap<>();
        File dir = new File(Objects.requireNonNull(TUIdraw.class.getResource("/commonGoals/CLI")).getPath());
        if (dir.isDirectory()) {
            File[] res;
            if (dir.listFiles() != null)
                res = dir.listFiles();
            else
                res = new File[0];

            for (File f : Objects.requireNonNull(res)) {
                if (!f.isDirectory() && f.getName().contains(".txt")) {
                    try (FileInputStream fr = new FileInputStream(f)) {
                        String img = new String(fr.readAllBytes(), StandardCharsets.UTF_8);
                        ris.put(f.getName().replace(".txt", ""), img.split("\r\n"));
                    } catch (IOException e) {
                        System.err.println("error while reading resources");
                    }
                }
            }

        }
        return ris;
    }

    static void drawGameState(GameInfo currentGameState, char[][] cliPixel, int[][] cliPixelColor) {
        if (currentGameState != null) {
            //draw is last turn if it is
            if (currentGameState.lastTurn())
                drawString("Last Turn", renderHeight - 10, 30, DEFAULT, 50 - 2, cliPixel, cliPixelColor);
        }
    }


    static void drawPersonalGoal(List<PersonalGoalInfo> currentPersonalGoals, char[][] cliPixel, int[][] cliPixelColor) {
        final int personalGoalsX = 55;
        final int personalGoalsY = 24;
        if (currentPersonalGoals.size() == 6) {
            Tile[][] shelf = new Tile[6][5];
            Arrays.stream(shelf).forEach(tiles -> Arrays.fill(tiles, Tile.EMPTY));
            for (PersonalGoalInfo c : currentPersonalGoals) {
                for (int i = 0; i < c.description().length; i++) {
                    for (int j = 0; j < c.description()[0].length; j++) {
                        if (c.description()[i][j] != Tile.EMPTY)
                            shelf[i][j] = c.description()[i][j];
                    }
                }
            }
            drawGrid(personalGoalsX, personalGoalsY + 1, shelf[0].length, shelf.length, cliPixel, cliPixelColor);
            drawGridContents(personalGoalsX, personalGoalsY + 1, shelf, cliPixel, cliPixelColor);
            drawCenteredString("PERSONAL GOAL", personalGoalsX, personalGoalsY, shelf[0].length * 4 + 1, DEFAULT, cliPixel, cliPixelColor);
        }
    }


}

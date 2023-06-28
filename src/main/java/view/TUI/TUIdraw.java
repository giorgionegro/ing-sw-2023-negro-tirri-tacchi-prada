package view.TUI;

import model.Tile;
import model.Token;
import model.abstractModel.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static view.TUI.TUI.*;
import static view.TUI.TUIutils.*;

/**
 * This class contains all the methods used to draw the TUI
 */
final class TUIdraw {

    /**
     * width of command line
     */
    static final int commandLineWidth = 75;
    /**
     * height of command line
     */

    static final int commandLineHeight = 10;
    /**
     * horizontal coordinate of command line
     */
    static final int commandLineX = 1;
    /**
     * vertical coordinate of command line
     */
    static final int commandLineY = 41;

    /**
     * @hidden
     * Constructor is private because this class is not meant to be instantiated
     */
    private TUIdraw() {
    }

    /**
     * This method is used to draw grids of different shape and size based on its parameters
     *
     * @param startX        integer representing start horizontal coordinate
     * @param startY        integer representing start vertical coordinate
     * @param gridRowDim    integer representing row dimension of the grid
     * @param gridColDim    integer representing column dimension of the grid
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    static void drawGrid(int startX, int startY, int gridRowDim, int gridColDim, char[][] canvas, int[][] canvasColor) {
        //first we draw a template for a line of cells' middle part
        String middle = "│   ".repeat(gridRowDim) + "│";
        //then we draw the top line
        for (int i = 0; i < gridColDim; i++) {
            String pattern;
            if (i == 0)
                pattern = "┬───";
            else
                pattern = "┼───";

            //draw them to the canvas
            drawString(pattern.repeat(gridRowDim), startY + i * 2, startX, DEFAULT, 60, canvas, canvasColor);
            drawString(middle, startY + i * 2 + 1, startX, DEFAULT, 60, canvas, canvasColor);
        }
        //then we draw the bottom line
        drawString("┴───".repeat(gridRowDim), startY + gridColDim * 2, startX, DEFAULT, 60, canvas, canvasColor);

        //then we correct the corners
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
            canvas[startY + i * 2][startX] = s;
            canvasColor[startY + i * 2][startX] = DEFAULT;
            canvas[startY + i * 2][startX + gridRowDim * 4] = t;
            canvasColor[startY + i * 2][startX + gridRowDim * 4] = DEFAULT;
        }
    }

    /**
     * @param startX x coordinate of the start of the grid
     * @param startY y coordinate of the start of the grid
     * @param contents 2d array of tiles representing the contents of the grid
     * @param canvas 2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    static void drawGridContents(int startX, int startY, Tile[][] contents, char[][] canvas, int[][] canvasColor) {
        startX = startX + 1;
        startY = startY + 1;
        //draw the contents of a grid
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[i].length; j++) {
                int color = getColour(contents[i][j].getColor());
                char c = '█';
                if (color == DEFAULT)
                    c = ' ';

                for (int k = 0; k < 3; k++) {
                    canvas[startY + i * 2][k + startX + j * 4] = c;
                    canvasColor[startY + i * 2][k + startX + j * 4] = color;
                }
            }
        }
    }

    /**
     * This method draws a String from the start coordinates
     *
     * @param toDraw        String to draw
     * @param row           integer representing start row coordinate
     * @param startCol      integer representing start column coordinate
     * @param colour        integer representing color of the String to be drawn
     * @param size          size the drawn String can occupy
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    //draw String from start coordinate
    static void drawString(String toDraw, int row, int startCol, int colour, int size, char[][] canvas, int[][] canvasColor) {
        if (row < 0 || row >= renderHeight || startCol < 0 || startCol >= renderWidth)
            return;
        //if the string is too long, we cut it
        if (toDraw.length() > size)
            toDraw = toDraw.substring(0, size);
        //we draw the string up to the end of the canvas or the end of the string
        for (int i = 0; (i < toDraw.length() && (i + startCol) < (renderWidth - 2)); i++) {
            canvas[row][startCol + i] = toDraw.charAt(i);
            canvasColor[row][startCol + i] = colour;
        }
    }


    /**
     * This method draws the endgame screen
     *
     * @param points        Map that associates Player ID to its points
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    static void drawGameEnd(Map<String, Integer> points, char[][] canvas, int[][] canvasColor) {
        final int gameEndY = 14;
        final int gameEndX = 50;
        final int gameEndWidth = 36;
        //we sort and group the players by points
        Map<Integer, List<Map.Entry<String, Integer>>> grouped = points.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));
        List<Map.Entry<Integer, List<Map.Entry<String, Integer>>>> sorted = new ArrayList<>(grouped.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList());
        Collections.reverse(sorted);

        drawCenteredString("LEADERBOARD", gameEndX, gameEndY, gameEndWidth, DEFAULT, canvas, canvasColor);

        String positionTitle = " POSITION ";
        String idTitle = "      ID      ";
        String pointsTitle = "  POINTS  ";

        drawCenteredString(positionTitle + "│" + idTitle + "│" + pointsTitle, gameEndX, gameEndY + 1, gameEndWidth, DEFAULT, canvas, canvasColor);
        //we draw the leaderboard by group of players with the same points
        int leaderBoardY = gameEndY + 2;
        int line = 0;
        int position = 1;
        for (Map.Entry<Integer, List<Map.Entry<String, Integer>>> group : sorted) {
            boolean firstOfTheGroup = true;
            for (Map.Entry<String, Integer> player : group.getValue()) {
                String pos;
                String pPoints;
                if (firstOfTheGroup) {
                    pos = String.valueOf(position);
                    firstOfTheGroup = false;
                } else {
                    pos = "|";
                }
                pPoints = String.valueOf(player.getValue());
                drawCenteredString(pos, gameEndX, leaderBoardY + line, positionTitle.length(), DEFAULT, canvas, canvasColor);
                drawCenteredString(player.getKey(), gameEndX + positionTitle.length() + 1, leaderBoardY + line, idTitle.length(), DEFAULT, canvas, canvasColor);
                drawCenteredString(pPoints, gameEndX + positionTitle.length() + idTitle.length() + 2, leaderBoardY + line, pointsTitle.length(), DEFAULT, canvas, canvasColor);
                line++;
            }
            position++;
        }

    }

    /**
     * This method draws a String centered in a certain space
     *
     * @param text          String to draw centered
     * @param startX        integer representing start horizontal coordinate
     * @param startY        integer representing start vertical coordinate
     * @param spaceWidth    integer representing width of space that the String can occupy
     * @param colour        colour of the String to draw
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    @SuppressWarnings("SameParameterValue")
    static void drawCenteredString(String text, int startX, int startY, int spaceWidth, int colour, char[][] canvas, int[][] canvasColor) {
        StringBuilder title = new StringBuilder();
        int spaceBefore = (spaceWidth - text.length()) / 2;
        title.append(" ".repeat(spaceBefore)).append(text);
        drawString(title.toString(), startY, startX, colour, title.length(), canvas, canvasColor);
    }

    /**
     * This method draws a box based on the parameters
     *
     * @param row           integer representing start row of the box
     * @param col           integer representing start column of the box
     * @param height        integer representing height of the box
     * @param width         integer representing width of the box
     * @param colour        integer representing color of the box
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    @SuppressWarnings("SameParameterValue")
    static void drawBox(int row, int col, int height, int width, int colour, char[][] canvas, int[][] canvasColor) {
        //we draw the box pixel by pixel
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == 0) canvas[row + i][col + j] = '┌';
                else if (i == 0 && j == width - 1) canvas[row + i][col + j] = '┐';
                else if (i == height - 1 && j == 0) {
                    canvas[row + i][col + j] = '└';
                    canvasColor[row + i][col + j] = colour;
                } else if (i == height - 1 && j == width - 1) {
                    canvas[row + i][col + j] = '┘';
                    canvasColor[row + i][col + j] = colour;
                } else if (i == 0) {
                    canvas[row + i][col + j] = '─';
                    canvasColor[row + i][col + j] = colour;
                } else if (i == height - 1) {
                    canvas[row + i][col + j] = '─';
                    canvasColor[row + i][col + j] = colour;
                } else if (j == 0) {
                    canvas[row + i][col + j] = '│';
                    canvasColor[row + i][col + j] = colour;
                } else if (j == width - 1) {
                    canvas[row + i][col + j] = '│';
                    canvasColor[row + i][col + j] = colour;
                }
                //if not in the border we leave the pixel unchanged
            }
        }
    }

    /**
     * this method draws the player chat
     *
     * @param thisPlayerId      String representing the id of the player
     * @param currentPlayerChat object containing List of messages of one player
     * @param canvas          2d array of characters representing the pixels
     * @param canvasColor     2d array of integers representing the color of the pixels
     */
    static void drawChat(String thisPlayerId, List<? extends Message> currentPlayerChat, char[][] canvas, int[][] canvasColor) {
        final int chatX = 80;
        final int chatY = 23;
        final int chatBoxWidth = 58;
        final int chatBoxHeight = 28;
        if (currentPlayerChat != null) {
            //we reverse the list so that the last message is the first to be drawn
            Collections.reverse(currentPlayerChat);
            //we draw the box
            drawBox(chatY + 1, chatX, chatBoxHeight, chatBoxWidth, DEFAULT, canvas, canvasColor);
            int chatContentsX = chatX + 1;
            int chatContentsY = chatY + 2;
            int chatContentsHeight = chatBoxHeight - 2;
            int chatContentsWidth = chatBoxWidth - 4;
            String[] chatBuffer = new String[chatContentsHeight];
            Arrays.fill(chatBuffer, "");
            //we fill the buffer with the messages to be subsequently drawn
            int pointer = chatContentsHeight - 1;
            for (Message m : currentPlayerChat) {
                String sender = m.getSender();
                String receiver = m.getReceiver();
                String text = (sender.equals(thisPlayerId) ? "YOU" : sender) + " to " + (receiver.isBlank() ? "Everyone" : receiver.equals(thisPlayerId) ? "YOU" : receiver) + ": " + m.getText();
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
            //we draw the buffer
            for (int i = 0; i < chatBuffer.length; i++) {
                drawString(chatBuffer[i], chatContentsY + i, chatContentsX + 1, DEFAULT, chatBuffer[i].length(), canvas, canvasColor);
            }

            drawCenteredString("CHAT", chatX, chatY, chatBoxWidth, DEFAULT, canvas, canvasColor);
        }
    }

    /**
     * this method draws the living room board
     *
     * @param currentLivingRoom object containing the living room game board
     * @param canvas          2d array of characters representing the pixels
     * @param canvasColor     2d array of integers representing the color of the pixels
     */
    static void drawLivingRoom(Tile[][] currentLivingRoom, char[][] canvas, int[][] canvasColor) {
        final int livingRoomX = 1;
        final int livingRoomY = 1;
        if (currentLivingRoom != null) {
            //we draw the grid
            drawGrid(livingRoomX + 2, livingRoomY + 2, currentLivingRoom[0].length, currentLivingRoom.length, canvas, canvasColor);
            //we draw the contents
            drawGridContents(livingRoomX + 2, livingRoomY + 2, currentLivingRoom, canvas, canvasColor);

            //draw numbers on the top
            for (int i = 0; i < currentLivingRoom.length; i++) {
                String number = String.valueOf(i);
                if (i < 10) {
                    number = "  " + number + " ";
                } else {
                    number = " " + number + " ";
                }
                for (int c = 0; c < number.length(); c++) {
                    canvas[livingRoomY + 1][livingRoomX + 2 + i * 4 + c] = number.charAt(c);
                    canvasColor[livingRoomY + 1][livingRoomX + 2 + i * 4 + c] = DEFAULT;
                }
            }
            //draw numbers on the side
            for (int i = 0; i < currentLivingRoom.length; i++) {
                String number = String.valueOf(i);
                if (i < 10) {
                    number = "0" + number;
                }
                for (int c = 0; c < number.length(); c++) {
                    canvas[livingRoomY + 3 + i * 2][livingRoomX] = number.charAt(c);
                    canvasColor[livingRoomY + 3 + i * 2][livingRoomX] = DEFAULT;
                }
            }

            drawCenteredString("LIVING ROOM BOARD", livingRoomX + 2, livingRoomY, currentLivingRoom[0].length * 4 + 1, DEFAULT, canvas, canvasColor);
        }
    }

    /**
     * this method draws the command line
     *
     * @param cursor        starting string of the command line
     * @param oldCommands       list of old commands
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    static void drawCommandLine(String cursor, List<Pair> oldCommands, char[][] canvas, int[][] canvasColor) {


        drawBox(commandLineY + 1, commandLineX, commandLineHeight, commandLineWidth, DEFAULT, canvas, canvasColor);
        drawCenteredString("COMMAND LINE", commandLineX, commandLineY, commandLineWidth, DEFAULT, canvas, canvasColor);
        drawString(cursor, commandLineY + commandLineHeight - 1, commandLineX + 2, DEFAULT, commandLineWidth - 3, canvas, canvasColor);
        //we draw the commands history
        drawCommandsHistory(oldCommands, canvas, canvasColor);
    }

    /**
     * this method draws the old commands
     *
     * @param oldCmds       list of old commands
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    private static void drawCommandsHistory(List<Pair> oldCmds, char[][] canvas, int[][] canvasColor) {
        //we remove the old commands that don't fit in the command line starting from the oldest
        while (oldCmds.size() > (commandLineHeight - 4))
            oldCmds.remove(0);

        for (int i = 0; i < oldCmds.size(); i++) {
            drawString(oldCmds.get(i).string(), commandLineY + 2 + i, commandLineX + 1, oldCmds.get(i).colour(), commandLineWidth - 2, canvas, canvasColor);
        }
    }

    /**
     * this method draws the shelves of the players in the game
     *
     * @param currentShelves map that associates each playerId to its shelf
     * @param firstPlayerId  ID of the first player
     * @param thisPlayerId   ID of the player who is
     * @param playerOnTurn   ID of the player on turn
     * @param pointsValue    map that associates each playerId to its points
     * @param canvas       2d array of characters representing the pixels
     * @param canvasColor  2d array of integers representing the color of the pixels
     */
    static void drawShelves(Map<String, Tile[][]> currentShelves, String firstPlayerId, String thisPlayerId, String playerOnTurn, Map<String, ? super Integer> pointsValue, char[][] canvas, int[][] canvasColor) {
        final int shelvesX = 43;

        final int shelvesY = 3;
        final int shelvesPadding = 3;
        if (!currentShelves.isEmpty()) {
            int shelvesGridY = shelvesY + 2;

            int shelvesHeight = 0;
            int shelvesWidth = 0;

            int shelfDrawn = 0;

            StringBuilder playersName = new StringBuilder();
            StringBuilder playersPoints = new StringBuilder();

            for (String playerId : currentShelves.keySet()) {
                Tile[][] shelf = currentShelves.get(playerId);

                shelvesHeight = shelf.length;
                shelvesWidth = shelf[0].length * 4 + 1;

                int shelfX = shelvesX + (shelvesWidth + shelvesPadding) * shelfDrawn;
                drawGrid(shelfX, shelvesGridY, shelf[0].length, shelf.length, canvas, canvasColor);
                drawGridContents(shelfX, shelvesGridY, shelf, canvas, canvasColor);

                String tempPlayerId = playerId;
                //if the player is the one who is playing we write YOU instead of the ID
                if (playerId.equals(thisPlayerId)) {
                    tempPlayerId = "YOU";
                    drawString("  0   1   2   3   4  ", shelvesY + 1, shelfX, DEFAULT, 21, canvas, canvasColor);
                }

                if (tempPlayerId.length() > shelvesWidth - 4)
                    tempPlayerId = tempPlayerId.substring(0, shelvesWidth - 4);


                //if the player is the one who is playing we write <ID> instead of ID
                if (playerId.equals(playerOnTurn))
                    tempPlayerId = '>' + tempPlayerId + '<';

                //we need to equalize the length of the string to the length of the shelf to not leave old characters
                int spaceBefore = (shelvesWidth - tempPlayerId.length()) / 2;
                int spaceAfter = shelvesWidth - spaceBefore - tempPlayerId.length();

                if (playerId.equals(firstPlayerId)) {
                    spaceBefore -= 2;
                    playersName.append(" ■");
                }
                playersName.append(" ".repeat(spaceBefore));
                playersName.append(tempPlayerId);
                playersName.append(" ".repeat(spaceAfter));
                playersName.append(" ".repeat(shelvesPadding));

                String points = "Points: " + pointsValue.getOrDefault(playerId, 0);
                spaceBefore = (shelvesWidth - points.length()) / 2;
                spaceAfter = shelvesWidth - spaceBefore - points.length();
                playersPoints.append(" ".repeat(spaceBefore));
                playersPoints.append(points);
                playersPoints.append(" ".repeat(spaceAfter));
                playersPoints.append(" ".repeat(shelvesPadding));

                shelfDrawn++;
            }

            drawString(playersName.toString(), shelvesGridY + shelvesHeight * 2 + 1, shelvesX, DEFAULT, playersName.length(), canvas, canvasColor);
            drawString(playersPoints.toString(), shelvesGridY + shelvesHeight * 2 + 2, shelvesX, DEFAULT, playersPoints.length(), canvas, canvasColor);

            int maxSize = currentShelves.size() * shelvesWidth + shelvesPadding * (currentShelves.size() - 1);
            drawCenteredString("PLAYERS SHELVES", shelvesX, shelvesY, maxSize, DEFAULT, canvas, canvasColor);
        }
    }

    /**
     * this method draws the common goal
     *
     * @param commonGoals         map containing common goal id and common goal info
     * @param achievedCommonGoals map containing achieved common goals id and token
     * @param canvas            2d array of characters representing the pixels
     * @param canvasColor       2d array of integers representing the color of the pixels
     */
    static void drawCommonGoals(Map<String, Token> commonGoals, Map<String, Token> achievedCommonGoals, char[][] canvas, int[][] canvasColor) {
        final int commonGoalsY = 23;
        final int commonGoalsX = 3;

        final int commonGoalsPadding = 3;
        final int commonGoalBoxWidth = 23;
        final int commonGoalBoxHeight = 15;

        if (!commonGoals.isEmpty()) {
            int boxesStartY = commonGoalsY + 1;
            StringBuilder points = new StringBuilder();
            int drawnCommonGoals = 0;
            for (String id : commonGoals.keySet()) {
                int boxStartX = commonGoalsX + drawnCommonGoals * (commonGoalBoxWidth + commonGoalsPadding);
                drawBox(boxesStartY, boxStartX, commonGoalBoxHeight, commonGoalBoxWidth, DEFAULT, canvas, canvasColor);
                //we draw the common goal from the resources file
                String[] res = getCommonGoalRes(id);
                for (int j = 0; j < res.length; j++) {
                    drawString(res[j], boxesStartY + 1 + j, boxStartX + 1, DEFAULT, 60, canvas, canvasColor);
                }
                //we write the points of the common goal or if it is achieved we write ACHIEVED
                String temp;
                if (achievedCommonGoals.containsKey(id))
                    temp = "ACHIEVED: " + achievedCommonGoals.get(id).getPoints();
                else
                    temp = "Points: " + commonGoals.get(id).getPoints();

                //we need to equalize the length of the string to the length of the box to not leave old characters
                int spaceBefore = (commonGoalBoxWidth - temp.length()) / 2;
                int spaceAfter = commonGoalBoxWidth - spaceBefore - temp.length();
                points.append(" ".repeat(spaceBefore));
                points.append(temp);
                points.append(" ".repeat(spaceAfter));
                points.append(" ".repeat(commonGoalsPadding));

                drawnCommonGoals++;
            }
            drawString(points.toString(), boxesStartY + commonGoalBoxHeight, commonGoalsX, DEFAULT, points.length(), canvas, canvasColor);

            int maxSize = commonGoals.size() * commonGoalBoxWidth + commonGoalsPadding * (commonGoals.size() - 1);
            drawCenteredString("COMMON GOALS", commonGoalsX, commonGoalsY, maxSize, DEFAULT, canvas, canvasColor);
        }
    }

    /**
     * this method retrieves the resource of the common goal
     *  @param id   id of the common goal
     * @return   array of strings representing the resource
     */
    private static String[] getCommonGoalRes(String id) {
        String[] ris = new String[0];
        URL p = Objects.requireNonNull(Objects.requireNonNull(TUIdraw.class).getResource("/CommonGoals/CLI/" + id + ".txt"));
        try (InputStream stream = p.openStream()) {
            String img = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            ris = img.split("\r\n");
        } catch (IOException e) {
            System.err.println("error while reading resources");
        }
        return ris;
    }

    /**
     * this method draws that it is the last turn if it is the last turn
     *
     * @param isLastTurn    is this the last turn?
     * @param canvas      2d array of characters representing the pixels
     * @param canvasColor 2d array of integers representing the color of the pixels
     */
    static void drawLastTurn(boolean isLastTurn, char[][] canvas, int[][] canvasColor) {
        if (isLastTurn) {
            drawString("LAST TURN", 21, 43, GREEN, 50 - 2, canvas, canvasColor);
        }
    }


    /**
     * this method draws the personal goal
     *
     * @param currentPersonalGoals list of personal goals info
     * @param canvas             2d array of characters representing the pixels
     * @param canvasColor        2d array of integers representing the color of the pixels
     */
    static void drawPersonalGoal(Tile[][] currentPersonalGoals, char[][] canvas, int[][] canvasColor) {
        final int personalGoalsX = 55;
        final int personalGoalsY = 24;
        drawGrid(personalGoalsX, personalGoalsY + 1, currentPersonalGoals[0].length, currentPersonalGoals.length, canvas, canvasColor);
        drawGridContents(personalGoalsX, personalGoalsY + 1, currentPersonalGoals, canvas, canvasColor);
        drawCenteredString("PERSONAL GOAL", personalGoalsX, personalGoalsY, currentPersonalGoals[0].length * 4 + 1, DEFAULT, canvas, canvasColor);
    }
}

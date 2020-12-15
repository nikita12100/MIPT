/**
 * Store checkers position in int[][] table
 *
 * @version 1.0
 */
public class Table {
    int[][] table = new int[8][8];

    /**
     * Build position table[][]
     *
     * @param set_white -- white position
     * @param set_black -- black position
     */
    public Table(String set_white, String set_black) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; j += 2) {
                table[i][i % 2 + j] = 8;    // white cell
            }
        }
        String[] posWhite = set_white.split(" ");
        String[] posBlack = set_black.split(" ");
        for (String s : posWhite) {
            int x = s.charAt(0) - 'a';
            int y = 8 - Character.getNumericValue(s.charAt(1));
            table[x][y] = 1;    // white figure
        }
        for (String s : posBlack) {
            int x = s.charAt(0) - 'a';
            int y = 8 - Character.getNumericValue(s.charAt(1));
            table[x][y] = 2;    // black figure
        }
    }

    /**
     * Parse line and call make2Step(stepWhite, stepBlack)
     *
     * @param line -- line with moves
     * @throws MoveException -- exceptions: busy cell, wrong step, need to hit
     */
    public void make2Step(String line) throws MoveException {
        String[] step = line.split(" ");
        make2Step(step[0], step[1]);
    }


    /**
     * Make one white and one black step
     *
     * @param stepWhite -- step white, 'from-to': 'a3-b4'
     * @param stepBlack -- step black, 'from-to': 'a3-b4'
     * @throws MoveException -- exceptions: busy cell, wrong step, need to hit
     */
    public void make2Step(String stepWhite, String stepBlack) throws MoveException {
        makeStep(stepWhite, true);
        makeStep(stepBlack, false);
    }

    /**
     * Check if it is possible to hit in left top direction
     *
     * @param xFrom -- current position x
     * @param yFrom -- current position y
     * @param white -- is it white figure
     * @return -- true if it possible
     */
    private boolean tryHitLeftTop(int xFrom, int yFrom, boolean white) {
        if (xFrom <= 1 || yFrom <= 1) {
            return false;
        } else {
            if (white && table[xFrom - 1][yFrom - 1] == 2 && table[xFrom - 2][yFrom - 2] == 0) {
                table[xFrom][yFrom] = 0;
                table[xFrom - 1][yFrom - 1] = 0;
                table[xFrom - 2][yFrom - 2] = 1;
                return true;
            } else {
                if (!white && table[xFrom - 1][yFrom - 1] == 1 && table[xFrom - 2][yFrom - 2] == 0) {
                    table[xFrom][yFrom] = 0;
                    table[xFrom - 1][yFrom - 1] = 0;
                    table[xFrom - 2][yFrom - 2] = 2;
                    return true;
                } else return false;
            }
        }
    }

    private boolean tryHitLeftDown(int xFrom, int yFrom, boolean white) {
        if (xFrom <= 1 || yFrom >= 6) {
            return false;
        } else {
            if (white && table[xFrom - 1][yFrom + 1] == 2 && table[xFrom - 2][yFrom + 2] == 0) {
                table[xFrom][yFrom] = 0;
                table[xFrom - 1][yFrom + 1] = 0;
                table[xFrom - 2][yFrom + 2] = 1;
                return true;
            } else {
                if (!white && table[xFrom - 1][yFrom + 1] == 1 && table[xFrom - 2][yFrom + 2] == 0) {
                    table[xFrom][yFrom] = 0;
                    table[xFrom - 1][yFrom + 1] = 0;
                    table[xFrom - 2][yFrom + 2] = 2;
                    return true;
                } else return false;
            }
        }
    }

    private boolean tryHitRightTop(int xFrom, int yFrom, boolean white) {
        if (xFrom >= 6 || yFrom <= 1) {
            return false;
        } else {
            if (white && table[xFrom + 1][yFrom - 1] == 2 && table[xFrom + 2][yFrom - 2] == 0) {
                table[xFrom][yFrom] = 0;
                table[xFrom + 1][yFrom - 1] = 0;
                table[xFrom + 2][yFrom - 2] = 1;
                return true;
            } else {
                if (!white && table[xFrom - 1][yFrom - 1] == 1 && table[xFrom + 2][yFrom - 2] == 0) {
                    table[xFrom][yFrom] = 0;
                    table[xFrom + 1][yFrom - 1] = 0;
                    table[xFrom + 2][yFrom - 2] = 2;
                    return true;
                } else return false;
            }
        }
    }

    private boolean tryHitRightDown(int xFrom, int yFrom, boolean white) {
        if (xFrom >= 6 || yFrom >= 6) {
            return false;
        } else {
            if (white && table[xFrom + 1][yFrom + 1] == 2 && table[xFrom + 2][yFrom + 2] == 0) {
                table[xFrom][yFrom] = 0;
                table[xFrom + 1][yFrom + 1] = 0;
                table[xFrom + 2][yFrom + 2] = 1;
                return true;
            } else {
                if (!white && table[xFrom - 1][yFrom + 1] == 1 && table[xFrom + 2][yFrom + 2] == 0) {
                    table[xFrom][yFrom] = 0;
                    table[xFrom + 1][yFrom + 1] = 0;
                    table[xFrom + 2][yFrom + 2] = 2;
                    return true;
                } else return false;
            }
        }
    }

    /**
     * Try to hit in any direction
     *
     * @param xFrom -- current position x
     * @param yFrom -- current position y
     * @param white -- is it white figure
     * @return -- true if it possible
     */
    private boolean tryHit(int xFrom, int yFrom, boolean white) {
        if (tryHitLeftTop(xFrom, yFrom, white)) {
            tryHit(xFrom - 1, yFrom - 1, white);
            return true;
        } else if (tryHitLeftDown(xFrom, yFrom, white)) {
            tryHit(xFrom - 1, yFrom + 1, white);
            return true;
        } else if (tryHitRightTop(xFrom, yFrom, white)) {
            tryHit(xFrom + 1, yFrom - 1, white);
            return true;
        } else if (tryHitRightDown(xFrom, yFrom, white)) {
            tryHit(xFrom + 1, yFrom + 1, white);
            return true;
        } else return false;
    }

    /**
     * Make one step
     *
     * @param step  -- step 'from-to': 'a3-b4'
     * @param white -- is it white figure
     * @throws MoveException -- exceptions: busy cell, wrong step, need to hit
     */
    public void makeStep(String step, boolean white) throws MoveException {
        String[] moves = step.split("[-:]");

        int xFrom = moves[0].charAt(0) - 'a';
        int yFrom = 8 - Character.getNumericValue(moves[0].charAt(1));
        int xTo = moves[1].charAt(0) - 'a';
        int yTo = 8 - Character.getNumericValue(moves[1].charAt(1));
        if (tryHit(xFrom, yFrom, white)) {
            if (Math.abs(xFrom - xTo) == 1) { // need to hit
                throw new MoveException("invalid move");
            } else return;
        }
        if ((white && table[xTo][yTo] == 1) || (!white && table[xTo][yTo] == 2)) {
            throw new MoveException("busy cell");
        }
        if (table[xTo][yTo] == 8) {
            throw new MoveException("white cell");
        }

        table[xTo][yTo] = (white) ? 1 : 2;

        table[xFrom][yFrom] = 0;
    }

    /**
     * Draw table in console
     */
    public void printTable() {
        System.out.println();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Write positions of figures in console
     */
    public void printSetTable() {
        StringBuilder posWhite = new StringBuilder();
        StringBuilder posBlack = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            for (int j = 7; j >= 0; --j) {
                if (table[i][j] == 1) {
                    char x = (char) ('a' + i);
                    posWhite.append(x);
                    posWhite.append(8 - j);
                    posWhite.append(" ");
                } else if (table[i][j] == 2) {
                    char x = (char) ('a' + i);
                    posBlack.append(x);
                    posBlack.append(8 - j);
                    posBlack.append(" ");
                }
            }
        }
        System.out.println(posWhite);
        System.out.println(posBlack);
    }
}

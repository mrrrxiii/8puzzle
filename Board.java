import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;


public class Board {
    private final short[][] board;
    private final boolean isGoal;
    private final int hamming;
    private final int manhattan;
    private final Stack<Board> neighbors = new Stack<>();
    private final short zerorow;
    private final short zerocol;


    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        int h = 0;
        int m = 0;
        short row = 0;
        short col = 0;
        board = new short[tiles.length][tiles.length];
        boolean isGoalAux = true;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                board[i][j] = (short) tiles[i][j];
                if (board[i][j] != 0 && board[i][j] != (short) (i * dimension() + j + 1)) {
                    if (isGoalAux) {
                        isGoalAux = false;
                    }
                    h++;
                    int goali = (board[i][j] - 1) / dimension();
                    int goalj = (board[i][j] - 1) % dimension();
                    int di = goali - i;
                    int dj = goalj - j;
                    m += Math.abs(di) + Math.abs(dj);
                }


                if (board[i][j] == 0) {
                    row = (short) i;
                    col = (short) j;
                }

            }

        }


        isGoal = isGoalAux;
        hamming = h;
        manhattan = m;

        zerorow = row;
        zerocol = col;


    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension());
        str.append("\n");
        for (short[] i : board) {
            for (short j : i) {
                str.append(String.format("%2d ", j));
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int dimension() {
        return board.length;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.deepEquals(this.board, that.board);
    }

    public Iterable<Board> neighbors() {
        int[][] exlist = {
                {zerorow - 1, zerocol},
                {zerorow, zerocol - 1},
                {zerorow + 1, zerocol},
                {zerorow, zerocol + 1}
        };

        while (!neighbors.isEmpty()) {
            neighbors.pop();
        }
        for (int[] i : exlist) {

            int[][] aux = new int[dimension()][dimension()];
            for (int x = 0; x < dimension(); x++) {
                for (int y = 0; y < dimension(); y++) {
                    aux[x][y] = board[x][y];
                }
            }
            if (i[0] >= 0 && i[0] < dimension() && i[1] >= 0 && i[1] < dimension()) {

                aux[i[0]][i[1]] = 0;
                aux[zerorow][zerocol] = board[i[0]][i[1]];
                neighbors.push(new Board(aux));
            }


        }
        return neighbors;
    }

    public Board twin() {
        int[][] twin = new int[dimension()][dimension()];
        for (int x = 0; x < dimension(); x++) {
            for (int y = 0; y < dimension(); y++) {
                twin[x][y] = board[x][y];
            }
        }
        int i = 0;
        while (true) {
            if (twin[i][0] != 0 && twin[i][1] != 0) {
                int swap = twin[i][0];
                twin[i][0] = twin[i][1];
                twin[i][1] = swap;
                break;
            }
            i++;
        }


        return new Board(twin);
    }

    public static void main(String[] args) {


        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);
        System.out.println(initial);
        System.out.println(initial.manhattan());


    }
}


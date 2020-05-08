import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final int moves;
    private final Stack<Board> solution;
    private final boolean isSolvable;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        int mv = 0;
        int mvtwin = 0;
        int minmv = -1;

        Node goal = null;
        Node root = new Node(initial, mv, null);
        Node roottwin = new Node(initial.twin(), mv, null);
        MinPQ<Node> pq = new MinPQ<>(new Priority());
        MinPQ<Node> pqtwin = new MinPQ<>(new Priority());
        boolean solvableAux = false;


        pq.insert(root);
        pqtwin.insert(roottwin);


        while (true) {


            Node next = pq.delMin();


            Node pre = next.getPrevious();


            mv = next.getMove();


            Node nexttwin = pqtwin.delMin();
            Node pretwin = nexttwin.getPrevious();
            mvtwin = nexttwin.getMove();


            if (next.getBoard().isGoal()) {

                solvableAux = true;


                minmv = next.getMove();
                goal = next;
                break;


            }

            if (nexttwin.getBoard().isGoal()) {

                solvableAux = false;


                break;


            }
            mv++;
            for (Board i : next.getBoard().neighbors()) {
                if (pre == null || !i.equals(pre.getBoard())) {
                    pq.insert(new Node(i, mv, next));

                }

            }

            mvtwin++;
            for (Board i : nexttwin.getBoard().neighbors()) {
                if (pretwin == null || !i.equals(pretwin.getBoard())) {
                    pqtwin.insert(new Node(i, mvtwin, nexttwin));

                }

            }


        }
        isSolvable = solvableAux;
        if (isSolvable()) {
            solution = new Stack<>();
            while (goal != null) {
                solution.push(goal.getBoard());
                goal = goal.getPrevious();
            }
        } else {
            solution = null;
        }

        moves = minmv;

    }

    public boolean isSolvable() {
        return isSolvable;

    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    private class Priority implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            if (o1.getPriority() == o2.getPriority()) {
                return o1.getMan() - o2.getMan();
            }
            return o1.getPriority() - o2.getPriority();
        }
    }

    private class Node {
        private final Board board;
        private final int move;
        private final Node previous;
        private final int priority;
        private final int man;

        public Node(Board board, int move, Node previous) {
            this.board = board;
            this.move = move;
            this.previous = previous;
            this.man = board.manhattan();
            this.priority = board.manhattan() + move;
        }

        public Board getBoard() {
            return this.board;
        }

        public int getMove() {
            return this.move;
        }

        public Node getPrevious() {
            return this.previous;
        }

        public int getPriority() {
            return priority;
        }

        public int getMan() {
            return man;
        }
    }


    public static void main(String[] args) {


        // create initial board from file

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

public class KnightsTourOptimized {
    private int[][] board;
    private int boardSize;
    private int[] dx = {-2, -1, 1, 2, 2, 1, -1, -2};
    private int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};
    public int recursiveCalls = 0;
    public int backtrackCount = 0;

    public KnightsTourOptimized(int size) {
        boardSize = size;
        board = new int[boardSize][boardSize];
        for (int[] row : board)
            java.util.Arrays.fill(row, -1);
    }

    public boolean solve(int x, int y) {
        board[x][y] = 0;
        boolean result = solveUtil(x, y, 1);
        return result;
    }

    private boolean solveUtil(int x, int y, int movei) {
        recursiveCalls++;
        if (movei == boardSize * boardSize)
            return true;

        int[] nextMoves = getNextMoves(x, y);

        for (int i = 0; i < 8; i++) {
            int nx = nextMoves[2 * i];
            int ny = nextMoves[2 * i + 1];

            if (isSafe(nx, ny)) {
                board[nx][ny] = movei;
                if (solveUtil(nx, ny, movei + 1))
                    return true;
                board[nx][ny] = -1;
                backtrackCount++;
            }
        }
        return false;
    }

    private int[] getNextMoves(int x, int y) {
        int[] moves = new int[16];
        int[] degree = new int[8];
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            moves[2 * i] = nx;
            moves[2 * i + 1] = ny;
            degree[i] = countOnwardMoves(nx, ny);
        }

        // Sortir berdasarkan aturan Warnsdorff: gerak dengan degree paling kecil
        for (int i = 0; i < 8 - 1; i++) {
            for (int j = i + 1; j < 8; j++) {
                if (degree[j] < degree[i]) {
                    // Swap moves
                    int tmpx = moves[2 * i], tmpy = moves[2 * i + 1];
                    moves[2 * i] = moves[2 * j];
                    moves[2 * i + 1] = moves[2 * j + 1];
                    moves[2 * j] = tmpx;
                    moves[2 * j + 1] = tmpy;

                    int tmpd = degree[i];
                    degree[i] = degree[j];
                    degree[j] = tmpd;
                }
            }
        }
        return moves;
    }

    private int countOnwardMoves(int x, int y) {
        if (!isSafe(x, y)) return 9; // max + 1
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isSafe(nx, ny)) count++;
        }
        return count;
    }

    private boolean isSafe(int x, int y) {
        return (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[x][y] == -1);
    }

    public void printBoard() {
        for (int[] row : board) {
            for (int val : row) {
                System.out.printf("%4s", val == -1 ? "." : val);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int size = 8;
        int startX = 0;
        int startY = 0;
        KnightsTourOptimized kt = new KnightsTourOptimized(size);

        if (kt.solve(startX, startY)) {
            System.out.println("Knight's tour successful!");
            System.out.println("Recursive calls: " + kt.recursiveCalls);
            System.out.println("Backtracks: " + kt.backtrackCount);
            kt.printBoard();
        } else {
            System.out.println("No solution found.");
        }
    }
}

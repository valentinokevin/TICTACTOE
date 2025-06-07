import java.util.Scanner;

public class game {
    private static int[][] board = new int[3][3]; // +1 untuk Player, -1 untuk Computer
    private static final int PLAYER = 1;
    private static final int COMPUTER = -1;
    private static final int EMPTY = 0;

    public static void main(String[] args) {
        initializeBoard();
        printBoard();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Anda bermain sebagai 'X'. Komputer bermain sebagai 'O'.");

        while (true) {
            // Player's turn
            playerMove(scanner);

            if (checkWin(PLAYER)) {
                printBoard();
                System.out.println("Anda menang! (Ini seharusnya tidak mungkin terjadi dengan AI yang sempurna)");
                break;
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("Seri!");
                break;
            }

            // Computer's turn
            computerMove();
            printBoard();

            if (checkWin(COMPUTER)) {
                System.out.println("Komputer menang!");
                break;
            }

            if (isBoardFull()) {
                System.out.println("Seri!");
                break;
            }
        }
        scanner.close();
    }

    private static void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private static void printBoard() {
        System.out.println("\nPapan:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char symbol = ' ';
                if (board[i][j] == PLAYER) symbol = 'X';
                else if (board[i][j] == COMPUTER) symbol = 'O';
                System.out.print(" " + symbol + " ");
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("-----------");
        }
        System.out.println();
    }

    private static void playerMove(Scanner scanner) {
        while (true) {
            System.out.print("Masukkan baris (1-3): ");
            int row = scanner.nextInt() - 1;
            System.out.print("Masukkan kolom (1-3): ");
            int col = scanner.nextInt() - 1;

            if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY) {
                board[row][col] = PLAYER;
                break;
            } else {
                System.out.println("Langkah tidak valid. Masukkan baris dan kolom (1-3): .");
            }
        }
    }

    private static void computerMove() {
        System.out.println("Komputer bergerak");
        
        // Cari langkah terbaik dengan Minimax
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = COMPUTER;
                    int score = minimax(board, 0, false);
                    board[i][j] = EMPTY;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        board[bestMove[0]][bestMove[1]] = COMPUTER;
    }

    private static boolean checkWin(int player) {
        // Cek baris dan kolom
        for (int i = 0; i < 3; i++) {
            int rowSum = board[i][0] + board[i][1] + board[i][2];
            int colSum = board[0][i] + board[1][i] + board[2][i];
            if (rowSum == 3 * player || colSum == 3 * player) {
                return true;
            }
        }
        
        // Cek diagonal
        int diag1 = board[0][0] + board[1][1] + board[2][2];
        int diag2 = board[0][2] + board[1][1] + board[2][0];
        return diag1 == 3 * player || diag2 == 3 * player;
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int minimax(int[][] board, int depth, boolean isMaximizing) {
        if (checkWin(COMPUTER)) return 3;
        if (checkWin(PLAYER)) return -3;
        if (isBoardFull()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = COMPUTER;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER;
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}

import javax.swing.*;

public class app {
    public static void main(String args[]) {
        int boardW = 1000;
        int boardH = 700;

        JFrame window = new JFrame("Ping Pong Game - Mega Edition");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        PP pingPongGame = new PP(boardW, boardH);
        window.add(pingPongGame);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        pingPongGame.requestFocus();
    }
}
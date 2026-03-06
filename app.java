import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
public class app {

    //should implement that game is over if snakeHead is in the same place as snakeBody
    //made it 
     
    public static void main (String args[])
    { 
        int boardW= 600; 
        int boardH = 600;


        JFrame frame = new JFrame("Snake game !!!");
        frame.setVisible(true); 
        frame.setSize(boardW,boardH) ; 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setResizable(false);

        snakeGame snake = new snakeGame(boardW,boardH); 
        frame.add(snake); 
        frame.pack();
        snake.requestFocus();
       // frame.show();   - nepotrebno
    }
}

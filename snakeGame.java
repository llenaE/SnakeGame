import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.util.ArrayList; 
//import java.util.random.*;
public class snakeGame extends JPanel implements ActionListener, KeyListener
{

    int tileSize;
    int boardW; 
    int boardH; 
    int moveX; 
    int moveY; 
    int score ; 
    JLabel text; 
    boolean gameOver; 
    private class Tile
    {
        int x; 
        int y; 
        Tile(int x, int y)
        {
             this.x = x; 
             this.y = y; 
        }
    }
    Timer loop ; 
    //Snake head
    Tile snakeHead; 
    ArrayList<Tile> SnakeBody; 

    Tile food;  
    //Constructor: 
    snakeGame(int x, int y)
    {
        tileSize = 20; 
        score =0;   
        boardW = x; 
        boardH = y; 
        moveX = 0; 
        moveY = 0; 
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(boardW,boardH));
        addKeyListener(this);
        setFocusable(true); 
        snakeHead =  new Tile(4,4);
        food = new Tile((int)(Math.random()*(boardW/tileSize)),(int)(Math.random()*(boardH/tileSize))); 
        loop = new Timer(100,this); 
        SnakeBody = new ArrayList<Tile>(); 
      //  SnakeBody.add(snakeHead); 

        loop.start(); 
        text = new JLabel("Score " + score); 
        text.setForeground(Color.white);
        this.add(text); 
       
        gameOver = false; 
        
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g); 
    }
    public void draw(Graphics g)
    {
        g.setColor(Color.darkGray);
        for(int i = 0; i < boardH/tileSize; i ++)
         {
            g.drawLine(0, i*tileSize, boardW, i*tileSize);
            g.drawLine(i*tileSize, 0, i*tileSize,boardH);
         }
         for(int i = 0; i < SnakeBody.size(); i ++)
         {
            g.setColor(Color.MAGENTA);    
            g.fillRect(SnakeBody.get(i).x *tileSize,SnakeBody.get(i).y *tileSize,tileSize,tileSize); 
         }
        g.setColor(Color.orange); 
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        g.setColor(Color.MAGENTA);    
        g.fillRect(snakeHead.x *tileSize,snakeHead.y *tileSize,tileSize,tileSize); 
    }
    //Check that food doesnt generate where snake is ->
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
       // System.out.println("key pressed");
        if(e.getKeyCode()==KeyEvent.VK_R)
        {
           
            gameOver = false; 
            score = 0; 
            text.setFont(new Font("Arial", Font.BOLD, 12));
            text.setText("Score: " + score);
            
            snakeHead.x = 2; 
            snakeHead.y = 1; 
            moveX = 0; 
            moveY = 0;  
            loop.start(); 
            repaint(); 
            
        }
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
        {
            JComponent comp = (JComponent) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();
        }
        if(e.getKeyCode()==KeyEvent.VK_UP && moveY!=1)
        {
            moveY = -1; 
            moveX = 0; 
            System.out.println("up");
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && moveY!=-1 )
        {
            moveY = 1; 
            moveX = 0; 
            System.out.println("down");
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && moveX!=1)
        {
            moveY = 0; 
            moveX = -1; 
            System.out.println("left");
        } 
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && moveX!=-1)
        {
            moveY = 0; 
            moveX = 1; 
            System.out.println("right");
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    @Override
    //called by loop timer
    public void actionPerformed(ActionEvent e) {
       
        if(colision(food,snakeHead))
        { 
            SnakeBody.add(new Tile(food.x,food.y)); 
            System.out.println("collsiona"); 
            score+=10; 
            text.setText("Score: " + score);
            serveFood();  
            //int n = SnakeBody.size(); 
           
        }
        move();   
        repaint(); 
        if(gameOver)
        { 
            text.setText("GAME OVER!\nYour score is : " + score );
            text.setFont(new Font("Arial", Font.BOLD, 24)); 
            loop.stop(); 
            SnakeBody.clear(); 
            text.setHorizontalAlignment(SwingConstants.CENTER);
            text.setVerticalAlignment(SwingConstants.CENTER); 
        }
       
    }
    public void serveFood()
    {
        food.x = (int)(Math.random()*(boardW/tileSize)); 
        food.y = (int)(Math.random()*(boardH/tileSize));  
        System.out.println((food.x + " " + food.y));
    }
    public boolean colision(Tile t1, Tile t2)
    {  
        return t1.x==t2.x && t1.y==t2.y; 
    }
    public void move()
    {  
        

        for(int i = SnakeBody.size()-1; i>0; i --)
        {
            SnakeBody.get(i).x = SnakeBody.get(i-1).x;
            SnakeBody.get(i).y = SnakeBody.get(i-1).y;  
        }
        if(SnakeBody.size()>0)
        {
            SnakeBody.get(0).x = snakeHead.x; 
            SnakeBody.get(0).y = snakeHead.y; 
        }
        //text.setText("Score" + score + " " + gameOver);
       
        snakeHead.x +=moveX; 
        snakeHead.y += moveY; 
        if(snakeHead.x >=boardW/tileSize || snakeHead.x<0 )
            gameOver = true; 
        else if(snakeHead.y >boardH/tileSize || snakeHead.y<0 )
            gameOver = true; 
        else if (eatingItself())
            gameOver = true; 
    }
    public boolean eatingItself()
    {

        int n = SnakeBody.size(); 
        if(n<4)
            return false;
        for(int i = 0; i < n; i++)
            if(SnakeBody.get(i).x==snakeHead.x && SnakeBody.get(i).y==snakeHead.y )
                return true; 
        return false; 
    }
}
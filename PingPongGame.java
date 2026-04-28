import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//oh shit here we go again
public class PingPongGame extends JFrame implements KeyListener {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int PADDLE_WITH = 10;
    private static final int PADDLE_HEIGHT = 60;
    private static final int BALL_SIZE = 10;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 3;
    private static final int MAX_SCORE = 5;
    private int Paddle1Y;
    private int Paddle2Y;
    private int ballX;
    private int ballY;
    private int ballXSpeed;
    private int ballYSpeed;
    private int player1Score;
    private int player2Score;

//konsturktor
    public PingPongGame(){
        setTitle("Ping Pong");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setFocusable(true);
        addKeyListener(this);

        Paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        Paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;

        Timer timer = new Timer(10, new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e){
             moveBall();
             repaint();
         }
        });
        timer.start();
    }
    public void movePaddle1Up(){
        Paddle1Y -= PADDLE_SPEED;
        if(Paddle1Y < 0 ){
            Paddle1Y = 0;
        }
    }
    public void movePaddle1Down(){
        Paddle1Y += PADDLE_SPEED;
        if(Paddle1Y > HEIGHT - PADDLE_HEIGHT){
            Paddle1Y = HEIGHT - PADDLE_HEIGHT;
        }
    }
    //sasha lox
    public void movePaddle2Up(){
        Paddle2Y -= PADDLE_SPEED;
        if( Paddle2Y < 0){
            Paddle2Y = 0;
        }
    }
    //dvizhenie myacha 2
    public void movePaddle2Down(){
        Paddle2Y += PADDLE_SPEED;
        if(Paddle2Y > HEIGHT - PADDLE_HEIGHT){
            Paddle2Y = HEIGHT - PADDLE_HEIGHT;
        }
    }
    //dvizhenie myacha
    public void moveBall(){
        ballY += ballYSpeed;
        ballX += ballXSpeed;
        if (ballX <= PADDLE_WITH && ballY + BALL_SIZE >= Paddle1Y &&
                ballY <= Paddle1Y + PADDLE_HEIGHT) {
            ballXSpeed = BALL_SPEED;
        } else if (ballX >= WIDTH - PADDLE_WITH - BALL_SIZE && ballY + BALL_SIZE >= Paddle2Y
                && ballY <= Paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -BALL_SPEED;
        }
        if(ballY <= 0 || ballY >= HEIGHT - BALL_SIZE){
            ballYSpeed = -ballYSpeed;
        }
        if(ballX < 0){
            player2Score++;
            resetGame();
        }else if( ballX > WIDTH - BALL_SIZE){
            player1Score++;
            resetGame();
        }
        if (player1Score >= MAX_SCORE || player2Score >= MAX_SCORE) {
            String winner = (player1Score >= MAX_SCORE) ? "Игрок 1" : "Игрок 2";
            JOptionPane.showMessageDialog(this,
                    winner + " Победил!");
            System.exit(0);
        }
    }
    //restart igry
    public void resetGame(){
            ballX = WIDTH / 2 - BALL_SIZE / 2;
            ballY = HEIGHT / 2 - BALL_SIZE / 2;
            ballXSpeed = BALL_SPEED;
            ballYSpeed = BALL_SPEED;
    }
    //otobrachenie igry
    public void paint (Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.fillRect(PADDLE_WITH, Paddle1Y, PADDLE_WITH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - 2 * PADDLE_WITH, Paddle2Y, PADDLE_WITH, PADDLE_HEIGHT);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
        g.setColor(Color.WHITE);
        Font font = new Font("Arial",
                Font.PLAIN, 20);
        g.setFont(font);
        FontMetrics fontMetrics =
                g.getFontMetrics(font);
        String player1ScoreStr = "Игрок 1: "
                + player1Score;
        String player2ScoreStr = "Игрок 2: "
                + player2Score;
        int player1ScoreX = (WIDTH - fontMetrics.stringWidth(player1ScoreStr)) / 4;
        int player2ScoreX = 2 * WIDTH / 3 - fontMetrics.stringWidth(player2ScoreStr) / 2;
        g.drawString(player1ScoreStr, player1ScoreX, 60 );
        g.drawString(player2ScoreStr, player2ScoreX, 60);
        Toolkit.getDefaultToolkit().sync();
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PingPongGame().setVisible(true);
            }
        });
    }
    @Override
    public void keyTyped(KeyEvent e){
    }
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_W){
            movePaddle1Up();
        }else if (e.getKeyCode() == KeyEvent.VK_S) {
            movePaddle1Down();
        }else if (e.getKeyCode() == KeyEvent.VK_UP) {
            movePaddle2Up();
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            movePaddle2Down();
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
    }
}
//i dont know what to write here
// and i dont gay, lol)))
//fuck you my viver
//fuck you my java
//fuck you my jdk
//fuck you my jre
//fuck you my jvm
//fuck you my jdk
//fuck you my jre
//fuck you my jvm
//fuck you my jdk


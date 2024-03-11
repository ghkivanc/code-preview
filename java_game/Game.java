
import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
public class Game extends JFrame implements KeyListener, MouseInputListener
{
    ReentrantLock lock = new ReentrantLock();
    boolean over = false;
    boolean win = false;
    ArrayList<GameObjects> gameObjects = new ArrayList<>();
    GameObjects[][] grid = new GameObjects[50][50];
    AirCraft player;
    Thread panelThread;
    Integer enemies = 0;
    

    class GamePanel extends JPanel implements Runnable
    {
        public GamePanel()
        {
            setSize(500, 500);
            setLayout(new GridLayout(1, 1));
            setVisible(true);
        }


        @Override
        public void paint(Graphics g) {
            super.paint(g);
            synchronized(gameObjects)
            {
                for(GameObjects object:gameObjects)
                {
                    g.setColor(object.color);
                    g.fillRect(object.x, object.y, object.width, object.height);
                }
            }
        }

        @Override
        public void run() {
            try {
                while(!over)
                {
                    repaint();
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

    }

    public Game()
    {
        setSize(500, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel gamePanel = new GamePanel();
        panelThread = new Thread(gamePanel);
        add(gamePanel, BorderLayout.CENTER);
        setVisible(true);
        addMouseListener(this);
        addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch(keyCode)
        {
            case KeyEvent.VK_W:
                player.updatePosition(keyCode);
                break;

            case KeyEvent.VK_S:
                player.updatePosition(keyCode);
                break;

            case KeyEvent.VK_D:
                player.updatePosition(keyCode);
                break;

            case KeyEvent.VK_A:
                player.updatePosition(keyCode);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        player.fire(Color.orange, false);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    class GameObjects extends Thread
    {
        int x, y, width, height;
        Color color;
        ArrayList<Projectile> projectiles = new ArrayList<>();
        boolean enemy;
        boolean terminated = false;

        public boolean inBounds(int x, int y)
        {
            if((((x >= 0) && (x <= 490)) && ((y >= 0) && (y <= 490))))
            {
                return true;
            }

            return false;
        }    
        
        public GameObjects collision(int x, int y)
        {
            GameObjects object = grid[y/10][x/10];
           
            return object;
        }

        @Override
        public String toString() {
            if(enemy)
            {
                return "enemy";
            }
            return "friendly";
        }
    
        public void remove()
        {
            synchronized(gameObjects)
            {
                gameObjects.remove(this);
            }
            grid[y/10][x/10] = null;

            if(this instanceof Projectile)
            {
                projectiles.remove(this);
            }

            terminated = true;
        }

        public void fire(Color color, boolean enemy)
        {
            lock.lock();
            projectiles.add(new Projectile(x + 10, y, color, 0, enemy, projectiles));
            projectiles.add(new Projectile(x - 10, y, color, 1, enemy, projectiles));
            lock.unlock();
        }

        public void updatePosition(int isEnemy)
        {
            ArrayList<GameObjects> toBeRemoved = new ArrayList<>();
            int[] move = {10, -10};
            Random choose = new Random();
            boolean moved = false;
            

            lock.lock();
            while(!moved)
            {
                int direction = choose.nextInt(2);
                int byThisMuch = move[choose.nextInt(2)];
                
                switch(direction)
                {
                    case 0:
                        if(inBounds(x + byThisMuch, y))
                        {
                            GameObjects object = collision(x + byThisMuch, y);

                            if(object != null)
                            {
                                if(object.enemy != enemy)
                                {
                                    object.remove();
                                    this.remove();
                                    moved = true;
                                }
                                else
                                {
                                continue;
                                }
                            }
                            else
                            {
                                grid[y/10][x/10] = null;
                                x = x + byThisMuch;
                                grid[y/10][x/10] = this;
                                moved = true;
                            }
                            
                        }
                        else
                        {
                            continue;
                        }
                        break;

                    case 1:
                        if(inBounds(x , y + byThisMuch))
                        {
                            GameObjects object = collision(x , y + byThisMuch);

                            if(object != null)
                            {
                                if(object.enemy != enemy)
                                {
                                    object.remove();
                                    this.remove();
                                    moved = true;
                                }
                                else
                                {
                                continue;
                                }
                            }
                            else
                            {
                                grid[y/10][x/10] = null;
                                y = y + byThisMuch;
                                grid[y/10][x/10] = this;
                                moved = true;
                            }

                        }
                        else
                        {
                            continue;
                        }
                        break;
                }

                for(GameObjects beingRemoved:toBeRemoved)
                {
                    beingRemoved.remove();
                }
            }
            lock.unlock();
        }

        public void updateProjectiles()
        {
            ArrayList<GameObjects> toBeRemoved = new ArrayList<>();

            lock.lock();
            if(!projectiles.isEmpty())
            {
                for(Projectile projectile:projectiles)
                {
                    switch(projectile.direction)
                    {
                        case 0:
                            if(inBounds(projectile.x + 10, projectile.y))
                            {
                                GameObjects object = collision(projectile.x + 10, projectile.y);

                                if(object != null)
                                {
                                    
                                    if(object.enemy != projectile.enemy)
                                    {
                                        toBeRemoved.add(object);
                                        toBeRemoved.add(projectile);
                                    }
                                    else
                                    {
                                        grid[projectile.y/10][projectile.x/10] = null;
                                        projectile.x = projectile.x + 10;
                                        grid[projectile.y/10][projectile.x/10] = projectile;
                                    }
                                  
                                }
                                else
                                {
                                    grid[projectile.y/10][projectile.x/10] = null;
                                    projectile.x = projectile.x + 10;
                                    grid[projectile.y/10][projectile.x/10] = projectile;
                                }
                            }
                            else
                            {
                                toBeRemoved.add(projectile);
                            }
                            break;

                        case 1:
                            if(inBounds(projectile.x - 10, projectile.y))
                            {
                                GameObjects object = collision(projectile.x - 10, projectile.y);

                                if(object != null)
                                {
                                    
                                    if(object.enemy != projectile.enemy)
                                    {
                                        toBeRemoved.add(projectile);
                                        toBeRemoved.add(object);
                                    }
                                    else
                                    {
                                        grid[projectile.y/10][projectile.x/10] = null;
                                        projectile.x = projectile.x - 10;
                                        grid[projectile.y/10][projectile.x/10] = projectile;
                                    }
    
                                }
                                else
                                {
                                    grid[projectile.y/10][projectile.x/10] = null;
                                    projectile.x = projectile.x - 10;
                                    grid[projectile.y/10][projectile.x/10] = projectile;
                                }
                            }
                            else
                            {
                                toBeRemoved.add(projectile);
                            }
                            break;
                    }
                }


                if(!toBeRemoved.isEmpty())
                {
                    for(GameObjects beingRemoved:toBeRemoved)
                    {
                        beingRemoved.remove();
                    }
                }
            }
            lock.unlock();
        }
    }

    class AirCraft extends GameObjects
    {
        public AirCraft()
        {
            x = 250;
            y = 250;
            width = 10;
            height = 10;

            enemy = false;
            color = Color.red;

            grid[y/10][x/10] = this;

            gameObjects.add(this);
            player = this;
            panelThread.start();
            setPriority(MAX_PRIORITY);
        }

        @Override
        public String toString() {
            return "player";
        }

        public void updatePosition(int keyCode)
        {
            ArrayList<GameObjects> toBeRemoved = new ArrayList<>();
        
            lock.lock();
            
            switch(keyCode)
            {
                case KeyEvent.VK_W:
                    if(inBounds(x, y - 10))
                    {
                        GameObjects object = collision(x , y - 10);
                        if(object != null)
                        {
                            if(object.enemy != enemy)
                            {
                                object.remove();
                                this.remove();
                            }
                        }
                        else
                        {
                            grid[y/10][x/10] = null;
                            y = y - 10;
                            grid[y/10][x/10] = this;
                        }
                        
                    }
                    break;
                case KeyEvent.VK_S:
                    if(inBounds(x , y + 10))
                    {
                        GameObjects object = collision(x , y + 10);

                        if(object != null)
                        {
                            if(object.enemy != enemy)
                            {
                                this.remove();
                                object.remove();
                            }
                        }
                        else
                        {
                            grid[y/10][x/10] = null;
                            y = y + 10;
                            grid[y/10][x/10] = this;
                        }
                    }
                    break;

                case KeyEvent.VK_D:
                    if(inBounds(x + 10, y))
                    {
                        GameObjects object = collision(x + 10, y);
                        if(object != null)
                        {
                            if(object.enemy != enemy)
                            {
                                this.remove();
                                object.remove();
                            }
                        }
                        else
                        {
                            grid[y/10][x/10] = null;
                            x = x + 10;
                            grid[y/10][x/10] = this;
                        }
                    }
                    break;

                case KeyEvent.VK_A:
                    if(inBounds(x - 10, y))
                    {
                        GameObjects object = collision(x - 10,y);
                        if(object != null)
                        {
                            if(object.enemy != enemy)
                            {
                                this.remove();
                                object.remove();
                            }
                        }
                        else
                        {
                            grid[y/10][x/10] = null;
                            x = x - 10;
                            grid[y/10][x/10] = this;
                        }
                    }
                    break;
            }

            for(GameObjects beingRemoved:toBeRemoved)
            {
                beingRemoved.remove();
            }

            lock.unlock();
        }

        @Override
        public void run() {
            try {
                while(!over && !terminated)
                {
                    Thread.sleep(100);
                    if(!projectiles.isEmpty())
                    {
                        updateProjectiles();
                    }

                    synchronized(enemies)
                    {
                        if(enemies == 0)
                        {
                            over = true;
                        }
                    }
                }

                if(!terminated)
                {
                    JFrame winPopUp = new JFrame();
                    winPopUp.setVisible(true);
                    winPopUp.setSize(250, 250);
                    winPopUp.setLayout(new BorderLayout());
                    JLabel winText = new JLabel("You win!");
                    winText.setSize(250, 250);
                    winText.setVisible(true);
                    winPopUp.add(winText, BorderLayout.WEST);
                    winPopUp.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
                else
                {
                    over = true;
                    JFrame losePopUp = new JFrame();
                    losePopUp.setSize(250, 250);
                    losePopUp.setVisible(true);
                    losePopUp.setLayout(new BorderLayout());
                    JLabel lostText = new JLabel("You lost!");
                    lostText.setSize(250, 250);
                    lostText.setVisible(true);
                    losePopUp.add(lostText, BorderLayout.WEST);
                    losePopUp.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    class Friend extends GameObjects
    {
        public Friend()
        {
            int generated_x = 0;
            int generated_y = 0;
            boolean distinct = false;
            Random generatePosition = new Random();
            enemy = false;

            while(!distinct)
            {
                generated_x = generatePosition.nextInt(49)*10;
                generated_y = generatePosition.nextInt(49)*10;
                
                if(collision(generated_x, generated_y) == null)
                {
                    distinct = true;
                }
            }

            this.x = generated_x;
            this.y = generated_y;
            grid[y/10][x/10] = this;
            width = 10;
            height = 10;

            color = Color.green;

            setPriority(8);

            gameObjects.add(this);
        }


        @Override
        public void run() {
            try {

                while(!over && !terminated)
                {
    
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        updatePosition(0);
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updatePosition(0);
                        fire(Color.magenta, false);
                        updateProjectiles();
                }

                if(!projectiles.isEmpty())
                        {
                            Thread.sleep(100);
                            lock.lock();
                            for(Projectile projectile:projectiles)
                            {
                                synchronized(gameObjects)
                                {
                                    gameObjects.remove(projectile);
                                }
                                grid[projectile.y/10][projectile.x/10] = null;
                            }
                            projectiles.clear();
                            lock.unlock();
                        }
                        
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        
    }

    class Enemy extends GameObjects 
    {
        public Enemy()
        {
            int generated_x = 0;
            int generated_y = 0;
            boolean distinct = false;
            Random generatePosition = new Random();
            enemy = true;
            enemies += 1;

            while(!distinct)
            {
                generated_x = generatePosition.nextInt(49)*10; 
                generated_y = generatePosition.nextInt(49)*10;
                    
                if(collision(generated_x, generated_y) == null)
                {
                    distinct = true;
                }
            }

            setPriority(8);
            this.x = generated_x;
            this.y = generated_y;
            grid[y/10][x/10] = this;
            width = 10;
            height = 10;

            color = Color.black;
            gameObjects.add(this);
        }

        @Override
        public void run() {
            try {

                boolean subtractOnce = false;
                while(!over && !terminated)
                {
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        updatePosition(1);
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updateProjectiles();
                        Thread.sleep(100);
                        updatePosition(1); 
                        fire(Color.blue, true); 
                        updateProjectiles();
                }


                if(!subtractOnce)
                {
                    synchronized(enemies)
                    {
                        enemies -= 1;
                    }
                    subtractOnce = true;
                }
                
                    if(!projectiles.isEmpty())
                    {
                        Thread.sleep(100);
                        lock.lock();
                        for(Projectile projectile:projectiles)
                        {
                            synchronized(gameObjects)
                            {
                                gameObjects.remove(projectile);
                            }
                            grid[projectile.y/10][projectile.x/10] = null;
                        }
                        projectiles.clear();
                        lock.unlock();
                    }
    
                
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    class Projectile extends GameObjects
    {
        int direction;
        boolean outOfBounds = false;

        public Projectile(int x, int y, Color color, int direction, boolean enemy, ArrayList<Projectile> projectiles)
        {
            if(inBounds(x, y))
            {
                this.x = x;
                this.y = y;
                width = 5;
                height = 5;
                this.color = color;
                this.direction = direction;
                this.enemy = enemy;

                synchronized(gameObjects)
                {
                    gameObjects.add(this);
                }
                grid[y/10][x/10] = this;

                this.projectiles = projectiles;

                projectiles.add(this);
            
            }
            else
            {
                outOfBounds = true;
            }
        }

        public void update(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "projectile";
        }

        @Override
        public void run() {
        }
    }
}

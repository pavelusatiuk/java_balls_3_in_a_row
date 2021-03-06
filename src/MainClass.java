import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import java.awt.event.*;

public class MainClass extends JFrame implements MouseListener {
    final public int  x_define = 6;
    final public int  y_define = 6;
    final public char UNFILLED = '*';
    public int score = 0;

    int MouseX = 0;
    int MouseY = 0;
    int countner = 0; //for count click on balls, nulling after 2

    int y1 = 0, x1 = 0, y2 = 0, x2 = 0;

    final Random random = new Random();

    GameBall[][] gameBalls = new GameBall[y_define][x_define];
    ScoreMonitor scoreMonitor = new ScoreMonitor();
    MagicshakeMonitor magicshakeMonitor = new MagicshakeMonitor();

        char[][] mass = {
            {'7', '7', UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
    };
    char[][] duplicate = {
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
    };
    char[][] mass_after_string_search = {
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
    };
    char[][] mass_after_column_search = {
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
            {UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED, UNFILLED},
    };

    String del_repeat(String buffer, int curr_pos, char templ) {
        StringBuilder sb = new StringBuilder(buffer);
        while ((curr_pos < buffer.length()) && (buffer.charAt(curr_pos) == templ)) {
            sb.setCharAt(curr_pos, UNFILLED);
            curr_pos++;
        }
        return sb.toString();
    }


    String check_repeat(String buffer) {
        for(int curr_pos = 0;(curr_pos + 2) < buffer.length();){
            if (((buffer.charAt(curr_pos) == buffer.charAt(curr_pos + 1)) && (buffer.charAt(curr_pos) == buffer.charAt(curr_pos + 2))) && ((buffer.charAt(curr_pos)  != UNFILLED && buffer.charAt(curr_pos + 1)  != UNFILLED && buffer.charAt(curr_pos + 2)  != UNFILLED))) {
                buffer = del_repeat(buffer, curr_pos, buffer.charAt(curr_pos));
            } else {
                curr_pos++;
            }
        }
        return buffer;
    }

    void gravitation() {
        for (int countner_of_faling = x_define - 1; countner_of_faling > 0; countner_of_faling--) {
            for (int y = y_define - 1; y >= 0; y--) {
                for (int x = 0; x < x_define; x++) {
                    if (mass[y][x] == '*') {
                        boolean enable = true;
                        int countner = 0;
                        while (enable) {
                            if ((y - countner - 1) >= 0) {
                                mass[y - countner][x] = mass[y - countner - 1][x];
                                mass[y - countner - 1][x] = '*';
                                countner++;
                            } else {
                                enable = false;
                            }
                        }
                    }
                }
            }
        }
    }

    boolean delete_column(char[][] receiving_array) {
        String buffer = "";
        boolean buff = false;

        for (int x = 0; x < x_define; x++) {
            for (int y = 0; y < y_define; y++) {
                buffer = buffer + receiving_array[y][x];
            }
            buffer = check_repeat(buffer);
            for (int y = 0; y < y_define; y++) {
                mass_after_column_search[y][x] = buffer.charAt(y);
            }
            for (int y = 0; y < y_define; y++)
                if (buffer.charAt(y) == UNFILLED) {
                    buff = true;
                    break;
                }
            buffer = "";
        }

        return buff;
    }

    boolean delete_string(char[][] receiving_array) {
        boolean buff = false;
        String buffer = "";

        for (int y = 0; y < y_define; y++) {
            for (int x = 0; x < x_define; x++) {
                buffer = buffer + receiving_array[y][x];
            }
            buffer = check_repeat(buffer);
            for (int x = 0; x < x_define; x++) {
                mass_after_string_search[y][x] = buffer.charAt(x);
            }
            for (int x = 0; x < x_define; x++)
                if (buffer.charAt(x) == UNFILLED) {
                    buff = true;
                    break;
                }
            buffer="";
        }

        return buff;
    }

    void comparison() {
        for (int x = 0; x < x_define; x++) {
            for (int y = 0; y < y_define; y++) {
                if ((mass_after_column_search[y][x] != mass_after_string_search[y][x]) || ((mass_after_string_search[y][x] == UNFILLED) && (mass_after_column_search[y][x] == UNFILLED))) {
                    mass[y][x] = UNFILLED;
                }
            }
        }
    }

    boolean filling() {
        boolean edit = false;
        for (int x = 0; x < x_define; x++) {
            for (int y = 0; y < y_define; y++) {
                if (mass[y][x] == UNFILLED) {
                    mass[y][x] = ((char) ((random.nextInt(9) + 1) + '0')); //ot 1 do 9
                    edit = true;
                }
            }
        }
        return edit;
    }

    void swap(int y1, int x1, int y2, int x2, char[][] receiving_array) {
        char buff;
        buff = receiving_array[y1][x1];
        receiving_array[y1][x1] = receiving_array[y2][x2];
        receiving_array[y2][x2] = buff;
    }

    boolean findcomdinations() {
        duplicate = mass;
        boolean boolbuffstring = false;
        boolean boolbuffcolumn = false;
        for (int x = 0; x < x_define; x++) {
            for (int y = 0; y < y_define; y++) {
                if (y + 1 < y_define) {
                    swap(y, x, y + 1, x, duplicate);
                    boolbuffstring = delete_string(duplicate);
                    boolbuffcolumn = delete_column(duplicate);
                }
                if (boolbuffcolumn || boolbuffstring) {
                    swap(y + 1, x, y, x, duplicate);
                    return true;
                }
                else {
                    if (y + 1 < y_define)
                        swap(y + 1, x, y, x, duplicate);
                }
            }
        }
        for (int y = 0; y < y_define; y++) {
            for (int x = 0; x < x_define; x++) {
                if (x + 1 < x_define) {
                    swap(y, x, y, x + 1, duplicate);
                    boolbuffstring = delete_string(duplicate);
                    boolbuffcolumn = delete_column(duplicate);
                }
                if (boolbuffstring || boolbuffcolumn) {
                    swap(y, x + 1, y, x, duplicate);
                    return true;
                }
                else {
                    if (x + 1 < x_define) {
                        swap(y, x + 1, y, x, duplicate);
                    }
                }
            }
        }
        return false;
    }

    void magicshake() {
        int end = (random.nextInt(99) + 1);
        magicshakeMonitor.setVisible(true);
        do {
            int x1 = (random.nextInt(x_define - 1) + 1); //ot 1 do x_def
            int y1 = (random.nextInt(y_define - 1) + 1); //ot 1 do y_def
            int x2 = (random.nextInt(x_define - 1) + 1); //ot 1 do x_def
            int y2 = (random.nextInt(y_define - 1) + 1); //ot 1 do y_def
            swap(y1, x1, y2, x2, mass);
            end--;
        } while (end != 0);
    }

    void cout_mass(char[][] resive_mass) {
        for (int y = 0; y < y_define; y++) {
            for (int x = 0; x < x_define; x++) {
                gameBalls[y][x].setColor(Integer.parseInt(String.valueOf(resive_mass[y][x])));
                //buffer.append(resive_mass[y][x]);
            }
           // System.out.println(buffer);
           // buffer = new StringBuilder();
        }

        repaint();
    }

    boolean gamecore() {
        delete_string(mass);
        delete_column(mass);
        comparison();
        gravitation();
        return filling();
    }

    void refilling(){
        for (int y = 0; y <= y_define; y++) {
            for (int x = 0; x <= x_define; x++) {
                mass[y][x] = ((char) ((random.nextInt(9) + 1) + '0'));
            }
        }
    }

    void make_a_good_mass(){
        int i = 0;
        while (true) {
            magicshake();
            while(gamecore());
            if((findcomdinations() && !gamecore())){
                break;
            }
            i++;
            if(i == 100){
                refilling();
                i = 0;
            }
        }
    }

    private void initBalls(){
        for (int y = 0; y < y_define; y++){
            for (int x = 0; x < x_define; x++){
                gameBalls[y][x] = new GameBall();
                gameBalls[y][x].setxCenter(100 + y*2*50);
                gameBalls[y][x].setyCenter(100 + x*2*50);
                gameBalls[y][x].setRadius(50);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int y = 0; y < y_define; y++) {
            for (int x = 0; x < x_define; x++) {
                gameBalls[y][x].draw(g);
            }
        }
        scoreMonitor.setScore(score);
        scoreMonitor.draw(g);
        magicshakeMonitor.draw(g);
    }

    public void choiceswap(int y1, int x1, int y2, int x2){
        if (((( (y1 > 0 && y1 <= y_define) && (x1 > 0 && x1 <= x_define)) && ((y2 > 0 && y2 <= y_define) && (x2 > 0 && x2 <= x_define))) || (x1 == x2 || y1 == y2)) && ((Math.abs(x1 - x2) + Math.abs(y1 - y2)) == 1)) {
            if (Math.abs(y1 - y2) <= 1 && Math.abs(x1 - x2) <= 1) {
                swap(y1-1, x1-1, y2-1, x2-1, mass);
                if (!gamecore()){
                    swap(y1-1, x1-1, y2-1, x2-1, mass);
                } else {
                    score++;
                }
            }
        }
    }

    public void startGame(){
        filling();
        make_a_good_mass();
        initBalls();
        gamecore();
        findcomdinations();
            if(!findcomdinations()) make_a_good_mass();
            cout_mass(mass);
        magicshakeMonitor.setVisible(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        countner++;
        if (countner == 1) {
            MouseX = e.getX();
            MouseY = e.getY();
            for (int y = 0; y < y_define; y++) {
                for (int x = 0; x < x_define; x++) {
                    if (gameBalls[y][x].isMouseUnderMe(MouseY, MouseX)) {
                        x1 = x + 1;
                        y1 = y + 1;
                        gameBalls[y][x].setSelected(true);
                        repaint();
                        break;
                    }
                }
            }
            magicshakeMonitor.setVisible(false);
        }
        if (countner == 2) {
            MouseX = e.getX();
            MouseY = e.getY();
            for (int y = 0; y < y_define; y++) {
                for (int x = 0; x < x_define; x++) {
                    if (gameBalls[y][x].isMouseUnderMe(MouseY, MouseX)) {
                        x2 = x + 1;
                        y2 = y + 1;
                        gameBalls[y][x].setSelected(true);
                        repaint();
                        break;
                    }
                }
            }
            countner = 0;
            for (int y = 0; y < y_define; y++)
                for (int x = 0; x < x_define; x++)
                    gameBalls[y][x].setSelected(false); //delete selected mode from balls
            choiceswap(y1, x1, y2, x2);
            cout_mass(mass);
            initBalls();
            while (gamecore()) ;
            gamecore();
            findcomdinations();
            if (!findcomdinations()) make_a_good_mass();
            cout_mass(mass);
        }
    }

    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

    public static void main(String[] args) {
        MainClass game = new MainClass();
        game.createFrame();
        game.startGame();
    }

    public void createFrame() {
        addMouseListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Cyberpunk2077 minimal settings");
        setSize(700, 700);
        setResizable(false);
        setVisible(true);
    }
}

/*  Import File  */
import java.util.*;
import java.util.random.*;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;

/* Main */
class Main_UI extends JFrame {
    public Main_UI() {

       JPanel A = new JPanel( new GridLayout(1, 1) );

       JPanel Main = new JPanel( new GridLayout(1, 1) );

       Main.add( new DataToShow() );
       add(Main);

    }
}

class DataToShow extends JPanel {
    private int Phrase = -1, P = 0, Overall_Score = 0, Strength = 1, Intelligence = 1, Endurance = 1, Willpower = 1, Best_Score = 0;
    private int Timer = 360; 
    private int counterA = 50; /* For status loop */ 
    private int counterB = 40; /* For map loop */
    private int currentB = 0; /* Indicate which city player in */
    private int Takes = 0;
    private int Sq = 0, S1 = 0, S2 = 0;
    private int HP = 150, Attack = 70, Defense = 10, Level = 1, StatPoint = Level * 20, Heal = 20, EXP = 0;
    private int which, amount;
    Player A = new Player(HP,Attack,Defense,Heal,Willpower);
    Monster B = new Monster(0, 1);
    Random C = new Random();

    public DataToShow() {
        Timer timer = new Timer(10,new TimerListener());
        timer.start();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if( Phrase == 1 ) {
                    if( P == 0 ) {
                        if( c == KeyEvent.VK_E ) {
                            Status();
                        } else if( c == KeyEvent.VK_RIGHT ) {
                            counterB = counterB + 1;
                        } else if( c == KeyEvent.VK_LEFT ) {
                            counterB = counterB - 1;
                        } else if( c == KeyEvent.VK_UP ) {
                            if( counterB % 4 == currentB ) {
                                P = 2;
                            } else if( counterB % 4 != currentB ) {
                                P = 1;
                            }
                        }
                    } else if( P == 1 ) {
                        if( c == KeyEvent.VK_UP ) {
                            if( counterA % 2 == 0 ) {
                                currentB = counterB % 4;
                                Takes = currentB;
                                P = 0;
                                Fight();
                            } else if( counterA % 2 == 1 ) {
                                P = 0;
                            }
                        } else if( c == KeyEvent.VK_RIGHT ) {
                            counterA++;
                        } else if( c== KeyEvent.VK_LEFT ) {
                            counterA--;
                        }
                    } else if( P == 2 ) {
                        if( c == KeyEvent.VK_UP ) {
                            P = 0;
                        }
                    }
  
                } else if( Phrase == 2 ) {
                    switch(c) {
                        case KeyEvent.VK_DOWN: counterA = counterA + 1; break;
                        case KeyEvent.VK_UP: counterA = counterA - 1; break;
                        case KeyEvent.VK_RIGHT: switch( counterA % 5 ) {
                            case 0: 
                                if( StatPoint > 0 ) {
                                    Strength = Strength + 1;
                                    StatPoint = StatPoint - 1;
                                    Attack = 10 + ( Strength * 3 );   
                                };
                                break;
                            case 1: 
                                if( StatPoint > 0 ) {
                                    Intelligence = Intelligence + 1;
                                    StatPoint = StatPoint - 1;
                                    Heal = 10 + ( Intelligence * 3 );
                                }
                                break;
                            case 2: 
                                if( StatPoint > 0 ) {
                                    Endurance = Endurance + 1; 
                                    StatPoint = StatPoint - 1;
                                    HP = Defense * ( Endurance * 5 / 2 ) + 150;
                                    Defense = ( Endurance * 2 );  
                                }
                                break;
                            case 3: 
                                if( StatPoint > 0 ) {
                                    StatPoint = StatPoint - 1;
                                    Willpower = Willpower + 1; 
                                }
                                break;
                            case 4: map(); counterA = 50; break;
                            default: break;
                        } ; break;
                         case KeyEvent.VK_LEFT: switch( counterA % 5 ) {
                            case 0:
                                if( Strength > -256 ) {
                                    Strength = Strength - 1;
                                    Attack = 10 + ( Strength * 3 ); 
                                    StatPoint = StatPoint + 1; 
                                } 
                                break;
                            case 1: 
                                if( Intelligence > -256 ) {
                                    Intelligence = Intelligence - 1;
                                    Heal = 10 + ( Intelligence * 3 );
                                    StatPoint = StatPoint + 1;
                                }
                                break;
                            case 2: 
                                if( Endurance > 0 ) {
                                    Endurance = Endurance - 1; 
                                    HP = Defense * ( Endurance * 5 ) + 100;
                                    StatPoint = StatPoint + 1;
                                }
                                break;
                            case 3: 
                                if( Willpower > -256 ) {
                                    Willpower = Willpower - 1;
                                    StatPoint = StatPoint + 1;
                                }
                                break;
                            case 4: map(); counterA = 50; break;
                            default: break;
                        } ; break;
                        case KeyEvent.VK_E: map(); break;
                        case KeyEvent.VK_T: Test(); break;
                        default: break;
                    }
                } else if( Phrase == 3 ) {
                    switch(c) {
                        case KeyEvent.VK_UP : 
                            switch( counterA % 2 ) {
                                case 0:
                                    attack(B);
                                    Sq = Sq + 1;
                                    S1 = 0;
                                    int ED = C.nextInt(2);
                                    switch(ED) {
                                        case(0) : B.attack(A); break;
                                        case(1) : B.heal(); break;
                                    }
                                    if( B.HP <= 0 ) {
                                        EXP = EXP + B.Exp;
                                        Overall_Score = Overall_Score + B.Exp;
                                        EXP_MAX();
                                       if(Takes > 0) {
                                        Takes = Takes - 1;
                                        Fight();
                                       } else {
                                        P = 0;
                                        map();
                                       }
                                    }
                                    if( A.HP <= 0 ) {
                                        End();
                                    }
                                break;
                                case 1:
                                    heal();
                                    S1 = 1;
                                    Sq = Sq + 1;
                                    int AD = C.nextInt(2);
                                    switch(AD) {
                                        case(0) : B.attack(A); break;
                                        case(1) : B.heal(); break;
                                    }
                                    if( B.HP <= 0 ) {
                                        EXP = EXP + B.Exp;
                                        Overall_Score = Overall_Score + B.Exp;
                                        EXP_MAX();
                                       if(Takes > 0) {
                                        Takes = Takes - 1;
                                        Fight();
                                       } else {
                                         P = 0;
                                        map();
                                       }
                                    }
                                    if( A.HP <= 0 ) {
                                        End();
                                    }
                                break;
                            }
                        break;
                        case KeyEvent.VK_RIGHT :
                            counterA = counterA + 1;
                        break;
                        case KeyEvent.VK_LEFT :
                            counterA = counterA - 1;
                        break;
                    }
                } else if( Phrase == 4) {
                    switch(c) {
                        case(KeyEvent.VK_UP) : Phrase = -1; break;
                    }
                } else if( Phrase == -1 ) {
                    switch(c) {
                        case(KeyEvent.VK_LEFT) : counterA++; break;
                        case(KeyEvent.VK_RIGHT) : counterA--; break;
                        case(KeyEvent.VK_UP) :
                            switch(counterA % 3) {
                                case(0) : System.exit(0);
                                case(1) : Phrase = 5; break;
                                case(2) : Phrase = 1; Timer Duration = new Timer(1000, new TotheEnd()); Duration.start(); break;
                            }
                    }
                } else if( Phrase == 5 ) {
                    switch(c) {
                        case(KeyEvent.VK_UP) : Phrase = -1; break;
                        default : Phrase = 0;
                    }
                }        
                repaint();
            }
        });
    }

   

    public void EXP_MAX() {
        if( Level < 15 ) {
            if( this.EXP >= (this.Level+1)*100 ) {
                this.Level = this.Level + 1;
                this.StatPoint = this.StatPoint + 20;
                this.EXP = 0;
            }
        }
    }

    public void Test() {
        this.Phrase = 0;
    }

    public void map() {
        this.Phrase = 1;
    }

    public void Status() {
        this.Phrase = 2;
    }
    
    public void Fight() {

        this.Phrase = 3;

        int a1 = 5, a2 = 1, b1 = 2, b2 = 1; /* 2 = Start point, 1 = Border */
        Random rand = new Random();

        if( Level <= 12 ) {
            if( Level <= 8 ) {
                if( Level <= 4 ) {
                    if( Level <= 3 ) {
                        a1 = 5; a2 = 1; b1 = 2; b2 = 1;
                    }
                } else {
                    a1 = 14; a2 = 1; b1 = 5; b2 = 1;
                }
            } else {
                a1 = 14; a2 = 5; b1 = 20; b2 = 5;
            }
        } else {
            a1 = 19; a2 = 5; b1 = 51; b2 = 20;
        }
        
        which = rand.nextInt(a2, a1+1);
        amount = rand.nextInt(b2, b1+1);

        Player Some = new Player(HP, Attack, Defense, Heal, Willpower);
        Monster Any = new Monster( which, amount );
        B = Any;
        A = Some;

    }

    public void End() {
        this.Phrase = 4; this.P = 0; this.Overall_Score = 0; this.Timer = 360;

        this.Strength = 1; this.Intelligence = 1; this.Endurance = 1; this.Willpower = 1; 
        this.HP = 150; this.Attack = 30; this.Defense = 10; this.Level = 1; this.StatPoint = 20; this.Heal = 20; this.EXP = 0;

    }

    public void Menu() {
        this.Phrase = -1;
    }

    public void HOW() {
        this.Phrase = 5;
    }

    public void attack( Monster any) {
        if( this.Attack * ( any.Willpower / 100 ) < any.Defend ) {
            any.HP = any.HP - ( this.Attack * 10 / 100 );
        } else {
             any.HP = any.HP - ( ( this.Attack * ( ( 100 - any.Willpower ) / 100 ) ) - any.Defend );
        }
    }

    public void heal() {
        if( A.Heal < 0 ) {
            A.Heal = Math.abs(A.Heal);
        }
        if( A.Heal + A.HP < HP ) {
            A.HP = A.HP + ( A.Heal * ( 1 + ( Intelligence/10 ) ) );
        } else {
            A.HP = HP;
        }
        System.out.println(A.HP = A.HP + ( A.Heal * ( 1 + ( Intelligence/10 ) ) ));
    }

    class Player {
        private int HP, Attack, Heal, Defend, Willpower;

        Player(int HP, int Attack, int Defense, int Heal, int Willpower) {
            this.HP = HP; this.Attack = Attack; this.Heal = Heal; this.Willpower = Willpower; this.Defend = Defense;
        }

        public void attack( Monster any) {
           int A = ( this.Attack * ( ( 100 - any.Willpower ) / 100 ) - any.Defend );
            if( A < 0 ) {
                any.HP = any.HP - ( this.Attack * 10 / 100 );
             } else {
                any.HP = any.HP - A;
             }
        }

        public void heal() {
            if( A.HP + A.Heal <= HP) {
                A.HP = A.HP + A.Heal;
            } else {
                A.HP = HP;
            }
        }
    }

    class Monster {
        private String Name;
        private int HP, Attack, Heal, Defend, Willpower, Exp;

        Monster(int any, int amount) {
            switch(any) {

        /* Easy Enemies -------------------------------------------------------------------------------- */

                case 1:
                    this.Name = "    Goblin";
                    this.HP = 150;
                    this.Attack = 20;
                    this.Heal = 5;
                    this.Defend = 5;
                    this.Willpower = 10;
                    this.Exp = 10;
                break;

                case 2:
                    this.Name = "     Thief";
                    this.HP = 170;
                    this.Attack = 80;
                    this.Heal = 10;
                    this.Defend = 20;
                    this.Willpower = 20;
                    this.Exp = 30;
                break;

                case 3:
                    this.Name = "    Furry";
                    this.HP = 50;
                    this.Attack = 100;
                    this.Heal = 0;
                    this.Defend = 5;
                    this.Willpower = 5;
                    this.Exp = 10;
                break;

                case 4:
                    this.Name = "      Rat";
                    this.HP = 170;
                    this.Attack = 10;
                    this.Heal = 0;
                    this.Defend = 0;
                    this.Willpower = 0;
                    this.Exp = 5;
                break;

                case 5:
                    this.Name = "     slime";
                    this.HP = 30;
                    this.Attack = 5;
                    this.Heal = 20;
                    this.Defend = 2;
                    this.Willpower = 10;
                    this.Exp = 1;
                break;

        /* Moderate Enemies -------------------------------------------------------------------- */

                case 6:
                    this.Name = "Newbie Adventurer";
                    this.HP = 150;
                    this.Attack = 30;
                    this.Heal = 10;
                    this.Defend = 5;
                    this.Willpower = 10;
                    this.Exp = 50;
                break;

                case 7:
                    this.Name = "Sentry";
                    this.HP = 200;
                    this.Attack = 50;
                    this.Heal = 10;
                    this.Defend = 10;
                    this.Willpower = 10;
                    this.Exp = 70;
                break;

                case 8:
                    this.Name = "Infantry";
                    this.HP = 170;
                    this.Attack = 100;
                    this.Heal = 0;
                    this.Defend = 20;
                    this.Willpower = 5;
                    this.Exp = 70;
                break;

                case 9:
                    this.Name = "Newbie Wizard";
                    this.HP = 130;
                    this.Attack = 100;
                    this.Heal = 20;
                    this.Defend = 0;
                    this.Willpower = 5;
                    this.Exp = 100;
                break;

                case 10:
                    this.Name = "Giant Spider";
                    this.HP = 300;
                    this.Attack = 50;
                    this.Heal = 30;
                    this.Defend = 20;
                    this.Willpower = 20;
                    this.Exp = 150;
                break;

                case 11:
                    this.Name = "Wanderer Ghost";
                    this.HP = 80;
                    this.Attack = 20;
                    this.Heal = 20;
                    this.Defend = 10;
                    this.Willpower = 90;
                    this.Exp = 60;
                break;

                case 12:
                    this.Name = "High Goblin";
                    this.HP = 210;
                    this.Attack = 60;
                    this.Heal = 10;
                    this.Defend = 10;
                    this.Willpower = 10;
                    this.Exp = 150;
                break;

                case 13:
                    this.Name = "Thief Leader";
                    this.HP = 250;
                    this.Attack = 120;
                    this.Heal = 50;
                    this.Defend = 50;
                    this.Willpower = 30;
                    this.Exp = 200;
                break;

                case 14:
                    this.Name = "Giant Slime";
                    this.HP = 300;
                    this.Attack = 20;
                    this.Heal = 50;
                    this.Defend = 60;
                    this.Willpower = 70;
                    this.Exp = 100;
                break;

        /* Hard Enemies --------------------------------------------------------------- */

                case 15:
                    this.Name = "Assaasin";
                    this.HP = 350;
                    this.Attack = 200;
                    this.Heal = 30;
                    this.Defend = 10;
                    this.Willpower = 50;
                    this.Exp = 300;
                break;

                case 16:
                    this.Name = "Veteran Adventurer";
                    this.HP = 250;
                    this.Attack = 100;
                    this.Heal = 20;
                    this.Defend = 30;
                    this.Willpower = 20;
                    this.Exp = 225;
                break;
                case 17:
                    this.Name = "Wyvern";
                    this.HP = 750;
                    this.Attack = 350;
                    this.Heal = 120;
                    this.Defend = 50;
                    this.Willpower = 70;
                    this.Exp = 1000;
                break;

                case 18:
                    this.Name = "Child-Class Dragon";
                    this.HP = 15000;
                    this.Attack = 2500;
                    this.Heal = 300;
                    this.Defend = 120;
                    this.Willpower = 90;
                    this.Exp = 120000;
                break;

            }

            this.HP = this.HP * amount;
            this.Exp = this.Exp * amount;
            this.Attack = this.Attack * amount;

        }

        void attack( Player any ) {
             int A = ( this.Attack * ( ( 100 - any.Willpower ) / 100 ) - any.Defend );
             if( A < 0 ) {
                any.HP = any.HP - ( this.Attack * 10 / 100 );
             } else {
                any.HP = any.HP - A;
             }
             S2 = 0;
        }

        void heal() {
            this.HP = this.HP + this.Heal;
            S2 = 1;
        }

    }

    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

    class TotheEnd implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Timer = Timer - 1;
            if( Timer == 0 ) {
                End();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Random rand = new Random();
        int x = getWidth()/2;
        int y = getHeight()/2;
        int r1 = Math.round(getWidth()/256+1);
        int r2 = Math.round(getHeight()/256+1);
       
        if( Phrase == 0 ) {
            for( int i = 0; i < 1000; i++ ) {
                int a = rand.nextInt(getWidth());
                int b = rand.nextInt(getHeight());
                int c = rand.nextInt(256);
                g.setColor(new Color( Math.round(a/r1), Math.round(b/r2), Math.round(c)));
                g.drawLine(x, y, a, b);
            }
        }

        if( Phrase == 1 ) {
            int a = getWidth();
            int b = getHeight();
            float Re = Level * 15 ,Ge = 255 - ( Level*15 ) ,Be = 225 - ( Level*15 );

            g.setColor(new Color(Re/255, Ge/255, Be/255));
            g.fillRect(0, 0, a, b);

            g.setColor(Color.BLACK);
            g.drawString("Time left : ", getWidth()-110, 30);
            g.drawString(Integer.toString(Timer), getWidth()-50, 30);

            g.setColor(Color.BLACK);
            g.drawString("Overall_Score :", 30, 30);
            g.drawString(Integer.toString(Overall_Score), 120, 30);
            g.drawString("New Town", a/2-20, b/2-50);
            g.drawString("Pearl Port", a/2+10, b/2-80);
            g.drawString("Chili Forest", a/2-90, b/2+70);
            g.drawString("Temple Green", a/2+70, b/2+100);

            g.setColor(Color.YELLOW);
            if( counterB % 4 == 0 ) {
                g.drawString("New Town", a/2-20, b/2-50);
            }  else if( counterB % 4 == 1 ) {
                g.drawString("Pearl Port", a/2+10, b/2-80);
            } else if( counterB % 4 == 2 ) {
                g.drawString("Chili Forest", a/2-90, b/2+70);
            } else if( counterB % 4 == 3 ) {
                g.drawString("Temple Green", a/2+70, b/2+100);
            }

            if( P == 1 ) {

                int temp = Math.abs(counterB % 4 - currentB);

                g.setColor(Color.RED);
                g.drawRect(a/2-151, b/2-81, 301,201);
                g.setColor(Color.WHITE);
                g.fillRect(a/2-150, b/2-80, 300,200);
                g.setColor(Color.BLACK);
                g.drawString("Are you sure to go there?",a/2-131,b/2-30);
                g.drawString("It'll take : ",a/2-131,b/2+10);
                g.drawString(Integer.toString(temp),a/2-81,b/2+10);
                g.drawString("Day(s)",a/2-70,b/2+10);

                g.drawString("Yup!", a/2+50, b/2+80);
                g.drawString("Nah!", a/2-70, b/2+80);

                g.setColor(Color.RED);
                if( counterA % 2 == 0 ) {
                    g.drawString("Yup!", a/2+50, b/2+80);
                } else if( counterA % 2 == 1 ) {
                    g.drawString("Nah!", a/2-70, b/2+80);
                }

            } else if( P == 2 ) {
                g.setColor(Color.RED);
                g.drawRect(a/2-151, b/2-81, 301,201);
                g.setColor(Color.WHITE);
                g.fillRect(a/2-150, b/2-80, 300,200);
                g.setColor(Color.BLACK);
                g.drawString("Thats the place you from!", a/2-70, y/2+80);
                g.setColor(Color.RED);
                g.drawString("Understood!", a/2-40, y/2+130);
                g.setColor(Color.BLACK);
                g.drawString("Tap Arrow UP again to get off this.", a/2-90, y/2+190);
            }   

        }

        if( Phrase == 2 ) {

            int a = getWidth();
            int b = getHeight();

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, a, b);
            g.setColor(Color.WHITE);

            g.drawString("Stat", a/2, 50);
            g.drawString("Strength :", 50, b/2);
            g.drawString("Intelligence :", 50, b/2+20);
            g.drawString("Endurance :", 50, b/2+40);
            g.drawString("Willpower :", 50, b/2+60);

            g.drawString("Level :", a/2+170, 30);
            g.drawString(Integer.toString(this.Level), a/2+170, 50);

            g.drawString("EXP :", a/2+250, 30);
            g.drawString(Integer.toString(this.EXP), a/2+250, 50);

            g.drawString("Point Left :", 20, 50);
            g.drawString(Integer.toString(this.StatPoint), 20, 70);


            g.drawString("HP :", a/2-50, 90);
            g.drawString(Integer.toString(this.HP), a/2+20, 90);

            g.drawString("Attack :", a/2-50, 110);
            g.drawString(Integer.toString(this.Attack), a/2+20, 110);

            g.drawString("Heal :", a/2-50, 130);
            g.drawString(Integer.toString(this.Heal), a/2+20, 130);

            g.drawString("Defense :", a/2-50, 150);
            g.drawString(Integer.toString(this.Defense), a/2+20, 150);


            g.drawString(Integer.toString(this.Strength), a-70, b/2);
            g.drawString(Integer.toString(this.Intelligence), a-70, b/2+20);
            g.drawString(Integer.toString(this.Endurance), a-70, b/2+40);
            g.drawString(Integer.toString(this.Willpower), a-70, b/2+60);
            g.drawString("Back", a/2 , b-70);

            g.setColor(Color.red);
            if( counterA % 5 == 0 ) {
                g.drawString(Integer.toString(this.Strength), a-70, b/2);
                g.setColor(Color.yellow);
                g.drawString("Strength : Stat that directly influenced damage. Nothing else.", 50 , b-40);
            } else if( counterA % 5 == 1 ) {
                g.drawString(Integer.toString(this.Intelligence), a-70, b/2+20);
                g.setColor(Color.yellow);
                g.drawString("Intelligence : Stat that mainly influence your healing. More you have, more you healed.", 50 , b-40);
            } else if( counterA % 5 == 2 ) {
                g.drawString(Integer.toString(this.Endurance), a-70, b/2+40);
                g.setColor(Color.yellow);
                g.drawString("Endurance : Stat that give you health and defend.", 50 , b-40);
                g.drawString("Defend work by directly subtract incoming damage.", 50 , b-20);
            } else if( counterA % 5 == 3 ) {
                g.drawString(Integer.toString(this.Willpower), a-70, b/2+60);
                g.setColor(Color.yellow);
                g.drawString("Willpower : Will that dispel an damage by percentage.", 50 , b-40);
            } else if( counterA % 5 == 4 ) {
                g.drawString("Back", a/2, b-70);
            }



        }

        if( Phrase == 3 ) {

            String SQ1 = " ", SQ2 = " ";
            int SA1 = 0, SA2 = 0;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.GRAY);
            g.drawRect(getWidth()*5/100, getHeight()*5/100, getWidth()*90/100, getHeight()*90/100);

            g.setColor(Color.WHITE);

            g.drawString("Time left : ", getWidth()-150, 50);
            g.drawString(Integer.toString(Timer), getWidth()-90, 50);

            if(Takes == 0) {
                 g.drawString("Last One!", getWidth()/2-20, 50);
            } else {
                g.drawString("Monster(s) left : ", getWidth()/2-60, 50);
                g.drawString(Integer.toString(Takes), getWidth()/2+30, 50);
            }
            g.drawString(B.Name,getWidth()/2+100,getHeight()/2+20);
            g.drawString(Integer.toString(amount),getWidth()/2+190,getHeight()/2+20);
            g.drawString(Integer.toString(B.HP),getWidth()/2+150,getHeight()/2+40);

            g.drawString("You",getWidth()/2-150,getHeight()/2+20);
            g.drawString(Integer.toString(A.HP),getWidth()/2-150,getHeight()/2+40);

            g.drawString("Attack",getWidth()/2+40,getHeight()/2+150);
            g.drawString("Heal",getWidth()/2-60,getHeight()/2+150);

            if( counterA % 2 == 0 ) {
                g.setColor(Color.RED);
                g.drawString("Attack",getWidth()/2+40,getHeight()/2+150);
            } else {
                g.setColor(Color.RED);
                g.drawString("Heal",getWidth()/2-60,getHeight()/2+150);
            }

            if( S1 == 0 ) {
                SQ1 = "You attack at Enemy! At : ";
                SA1 = ( A.Attack * ( ( 100 - B.Willpower ) / 100 ) ) - B.Defend; 
            } else {
                SQ1 = "You Heal yourself At : ";
                SA1 = A.Heal;
            }

            if( S2 == 0 ) {
                SQ2 = "Enemy attack at You! At : ";
                SA2 = ( B.Attack * ( ( 100 - A.Willpower ) / 100 ) ) - A.Defend; 
            } else {
                SQ2 = "Enemy Heal themself At : ";
                SA2 = B.Heal;
            }

            g.setColor(Color.RED);
            g.drawString(SQ1, 65, getHeight()/2+80);
            g.drawString(Integer.toString(SA1), 200, getHeight()/2+80);
            g.drawString(SQ2, getWidth()/2+10 ,getHeight()/2+80);
            g.drawString(Integer.toString(SA2), getWidth()/2+170, getHeight()/2+80);

        }
        
        if( Phrase == 4 ) {

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", getWidth()/2-20, 50);
            g.drawString("Score : ", getWidth()/2-10, 80);
            g.drawString(Integer.toString(Overall_Score), getWidth()/2+30, 80);
            g.drawString("Level : ", getWidth()/2-10, 100);
            g.drawString(Integer.toString(Level), getWidth()/2+30, 100);

            g.setColor(Color.RED);
            g.drawString("Menu", getWidth()/2, getHeight()-50);
        }

        if( Phrase == -1 ) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("Best Score : ",10,20);
            g.drawString(Integer.toString(Best_Score),100,20);
            g.drawString("Mediocre RPG Game", getWidth()/2-50, 50);
            g.drawString("Start", getWidth()/2-40, 150);
            g.drawString("How To Play", getWidth()/2-40, 170);
            g.drawString("EXIT", getWidth()/2-40, 190);
            g.drawString("'Left' and 'Right' Arrow to scroll around.", getWidth()/2-80, 300);
            g.drawString("'Up' for selecting.", getWidth()/2-80, 320);
            g.setColor(Color.RED);
            switch (counterA%3) {
                case 2:
                    g.drawString("Start", getWidth()/2-40, 150);
                    break;
                case 1:
                    g.drawString("How To Play", getWidth()/2-40, 170);
                    break;
                case 0:
                    g.drawString("EXIT", getWidth()/2-40, 190);
                    break;
            }

        }

        if( Phrase == 5 ) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, x*2, y*2);

            g.setColor(Color.white);
            g.drawString("Here it is : ", 55, 50);
            g.drawString("Using 'Left' and 'Right' Arrow to scroll options around. Use 'UP' Arrow to select option.", 50, 80);
            g.drawString("Which mean the entire game, You have only 3 keys to play a whole!", 50, 100);
            g.drawString("But.... Not really!", 50, 120);
            g.drawString("When you're in the map! You can use 'e' key to customize your stats.", 50, 140);
            g.drawString("Which is very important in order to win the game.", 50, 160);
            g.drawString("Be Advised, Eventhough you can kill enemy in 1 hit. It doesn't mean you just avoid it's attack", 50, 180);
            g.drawString("Heal if it need. Hit when its time!", 50, 200);
            g.drawString("GoodLuck!", 50, 220);

            g.setColor(Color.RED);
            g.drawString("Understood!", x/2, y/2+250);

        }
    
        /*  */

    }

}


public class A {
    public static void main(String[] args) {
        //System.out.println("Hello");
        Main_UI Test = new Main_UI();
        Test.setTitle("Game");
        Test.setSize(600, 400);
        Test.setMinimumSize(new Dimension(500, 400));
        Test.setLocationRelativeTo(null);
        Test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Test.setVisible(true);
    }
}
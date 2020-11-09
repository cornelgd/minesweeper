import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class Main {

    public static class joc extends JFrame {
        final int patratel = 30;
        final int bombe = 10;
        final int gridSize = 9;
        public int casute = gridSize * gridSize;
        public int[] bombeA = new int[bombe];
        public boolean [][] check = new boolean[gridSize][gridSize];
        public JFrame frameMeniu = new JFrame("Welcome");
        public  JButton[][] buttons = new JButton[gridSize][gridSize];


        joc() {


            int width = gridSize * patratel + 100;
            int height = gridSize * patratel + 120;
            int centerw = (width - (gridSize * patratel)) / 2 - 8;
            int centerh = (height - (gridSize * patratel)) / 2 - 20;
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable desen = new JTable(model);

            desen.setFont(new Font("Arial", Font.BOLD, 17));
            frameMeniu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameMeniu.setSize(width, height);
            frameMeniu.setLayout(null);
            frameMeniu.setLocationRelativeTo(null);


            model.setNumRows(gridSize);
            model.setColumnCount(gridSize);


            desen.setFocusable(false);
            desen.setRowSelectionAllowed(false);
            desen.setRowHeight(patratel);


            desen.setBounds(centerw, centerh, gridSize * 30, gridSize * 30);
            desen.setOpaque(true);
            desen.setVisible(true);
            desen.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);




            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(SwingConstants.CENTER);
            desen.getColumnModel().getColumn(0).setCellRenderer(center);


            desen.setEnabled(false);
            for (int i = 0; i < desen.getColumnModel().getColumnCount(); i++) {
                desen.getColumnModel().getColumn(i).setMaxWidth(patratel);
                desen.getColumnModel().getColumn(i).setCellRenderer(center);
            }


            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setBounds(centerw + i * patratel, centerh + j * patratel, patratel, patratel);

                    buttons[i][j].setVisible(true);
                    frameMeniu.add(buttons[i][j]);

                    generateBomb(bombe);


                }
            }
            desen.setShowGrid(true);
            frameMeniu.add(desen);


            frameMeniu.setVisible(true);



            int rw, cl;

            for (int i = 0; i < bombe; i++) {
                rw = Math.abs(bombeA[i] / gridSize)-1;
                if (rw <0)  rw=0;

                cl = bombeA[i] % gridSize-1;
                if (cl <0)  cl=0;
                desen.setValueAt("X", rw, cl);

            }
            for (int x = 0; x < gridSize; x++) {
                for (int y = 0; y < gridSize; y++) {
                    int val=0;
                    if (desen.getValueAt(x,y) != "X")
                    {
                        for (int xx= -1; xx < 2; xx++) {
                            for (int yy = -1; yy < 2; yy++) {
                                try {
                                    if (desen.getValueAt(x + xx, y + yy) == "X") {
                                        val++;
                                    }
                                }
                                catch (Exception e){
                                }
                            }
                        }
                        if (val!=0)
                            desen.setValueAt(val, x, y);

                    }

                }
            }




            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    int finalI = i;
                    int finalJ = j;

                    buttons[i][j].addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (desen.getValueAt(finalJ,finalI) == "X"){
                                JFrame frameMeniu1 = new JFrame("Boom!");
                                frameMeniu1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                frameMeniu1.setSize(width, height);
                                frameMeniu1.setLayout(null);
                                frameMeniu1.setLocationRelativeTo(null);
                                frameMeniu1.setVisible(true);

                            }

                            findEmptyCells(desen,finalI,finalJ);

                            frameMeniu.getContentPane().remove(buttons[finalI][finalJ]);

                            frameMeniu.revalidate();
                            frameMeniu.repaint();

                        }

                    });
                    MouseListener mouseListener = new MouseAdapter() {
                        public void mousePressed(MouseEvent mouseEvent) {
                            int modifiers = mouseEvent.getModifiersEx();

                            if ((modifiers & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK) {

                                if (!buttons[finalI][finalJ].isEnabled()) {
                                    try {




                                        buttons[finalI][finalJ].setDisabledIcon(null);
                                        buttons[finalI][finalJ].setIcon(null);

                                    } catch (Exception ex) {
                                        System.out.println(ex);
                                    }


                                    buttons[finalI][finalJ].setEnabled(true);
                                } else
                                {
                                    try {


                                        Image img = ImageIO.read(getClass().getResource("res/bomb.png"));
                                        Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);

                                        buttons[finalI][finalJ].setDisabledIcon(new ImageIcon(newimg));
                                        buttons[finalI][finalJ].setIcon(new ImageIcon(newimg));

                                    } catch (Exception ex) {
                                        System.out.println(ex);
                                    }


                                    buttons[finalI][finalJ].setEnabled(false);
                                }
                            }
                        }
                    };
                    buttons[i][j].addMouseListener(mouseListener);

                }
            }
        }









        public void findEmptyCells(JTable desen, int i, int j)        {

            if (i >= 0 && j >= 0 && i < gridSize && j < gridSize && check[i][j]==false)
            {
                if (desen.getValueAt(j, i) == null)
                {
                    check[i][j]=true;
                    int finalI = i;
                    int finalJ = j;
                    frameMeniu.getContentPane().remove(buttons[finalI][finalJ]);
                    frameMeniu.revalidate();
                    frameMeniu.repaint();
                    findEmptyCells(desen, i - 1, j);
                    findEmptyCells(desen, i + 1, j);
                    findEmptyCells(desen, i, j + 1);
                    findEmptyCells(desen, i, j - 1);


                } else if (desen.getValueAt(j, i) != null && desen.getValueAt(j, i) != "X") {
                    check[i][j]=true;
                    int finalI = i;
                    int finalJ = j;
                    frameMeniu.getContentPane().remove(buttons[finalI][finalJ]);
                    frameMeniu.revalidate();
                    frameMeniu.repaint();
                    return;
                }

            } else {
                return;
            }

        }







        private int[] generateBomb(int bombe) {
            Random rand = new Random();
            for (int i = 0; i < bombe; i++) {
                bombeA[i] = rand.nextInt(casute + 1);
                for (int j = 0; j < i; j++) {
                    if (bombeA[j] == bombeA[i]) {
                        i--;
                    }
                }
            }
            return bombeA;
        }


    }


    public static void main(String[] args) {
        new joc();
    }
}
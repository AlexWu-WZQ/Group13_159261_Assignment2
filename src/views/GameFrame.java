/*
 * Created by JFormDesigner on Thu May 19 10:21:36 CST 2022
 */

package views;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : basic frame for all views
 */

public class GameFrame extends JFrame {

    public GameFrame() {
        initComponents();
    }

    private void about() {
        JOptionPane.showMessageDialog(null,"Author:\nJiang Yu 20007896\n" +
                "Mengyao Jia 20008017\n" +
                "Zhibo Zhang 20007864\n" +
                "Alex Wu 20007895\n" +
                "\nVersion: 1.0\n" +
                "Date: 2022/5/19\n" +
                "Description: 159261 assignment2", "About", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))));
    }

    private void initComponents() {
        menuBar = new JMenuBar();
        about = new JButton();

        //======== this ========
        setTitle("Running Game");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))).getImage());
        setMinimumSize(new Dimension(816, 650));
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //======== menuBar ========
        {

            //======== about ========
            {
                about.setText("about");
                about.addActionListener(e -> about());
                about.setBorder(null);
                about.setContentAreaFilled(false);
                //button is not called by the SPACE key
                about.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
            }
            menuBar.add(about);
        }
        setJMenuBar(menuBar);
        pack();

        //add panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel();
        contentPanel.setLayout(cardLayout);
        this.add(contentPanel);

    }

    //show card panel
    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
    }

    private JMenuBar menuBar;
    private JButton about;
    private CardLayout cardLayout;
    public JPanel contentPanel;


}

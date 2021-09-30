import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewPort extends JPanel implements ActionListener {
    int WIDTH   = 1000;
    int HEIGHT  = 800;

    private final String NAME = "test";
    private final String FONT_NAME = "Cour";

    int size = 7;
    int pixelSize = size + 8;

    Timer t = new Timer(1, this);

    void run() {
        JFrame frame = new JFrame(NAME);
        frame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.add(new ViewPort());
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        Font font = new Font(FONT_NAME, Font.BOLD, pixelSize);
        g.setFont(font);


        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
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

    Camera camera = new Camera(new Vector3D(0,0,0), new Vector3D(0,0,1));
    Screen screen = new Screen(camera, 1F);

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


        for (int y = 0; y < HEIGHT; y += size) {
            for (int x = 0; x < WIDTH; x += size) {
                // Scale coordinates down to screens size
                float u = (float) x / WIDTH;
                float v = (float) y / HEIGHT;

                // Point on screen = p0 + (p1 - p0) * u + (p2-p0) * v;
                Vector3D p1p0 = Util.vectorMultiply(Util.vectorSubtract(screen.p1, screen.p0), u);
                Vector3D p2p0 = Util.vectorMultiply(Util.vectorSubtract(screen.p2, screen.p0), v);
                Vector3D point = Util.vectorAdd(Util.vectorAdd(screen.p0, p1p0), p2p0);

                // Shoot ray through point
                Vector3D vectorDirection = Util.vectorSubtract(point, camera.position);

                Ray ray = new Ray(camera.position, vectorDirection, 10E5F);

                // Trace ray, return pixel data, fill pixel

            }
        }
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ViewPort extends JPanel implements ActionListener, KeyListener, MouseWheelListener {
    int WIDTH   = 1000;
    int HEIGHT  = 800;

    static int size = 4;
    static int pixelSize = size + 6;

    String FONT_NAME = "Cambria";

    static Camera camera = new Camera(new Vector3D(0,0,0), new Vector3D(0,0,1));
    static Screen screen = new Screen(camera, 1F);

    Timer t = new Timer(1, this);

    static boolean rotate = true;

    static List<Sphere> spheres;
    static Light light = new Light(new Vector3D(120,250,0), Color.WHITE);

    Shader shader = new Shader();

    public ViewPort() {
        Sphere sphere1 = new Sphere(new Vector3D(0,30,0), 40F,  true);
        Sphere sphere2 = new Sphere(new Vector3D(100,30,0), 10F,  false);
        Sphere sphere3 = new Sphere(new Vector3D(-100,30,0), 10F, false);
        Sphere sphere4 = new Sphere(new Vector3D(0,30,100), 10F,  false);
        Sphere sphere5 = new Sphere(new Vector3D(0,30,-100), 10F, false);
        Sphere sphere6 = new Sphere(new Vector3D(125,255,0), 5F, false);
        Sphere bottom = new Sphere(new Vector3D(0, -1E5F, -2500), 1E5F, false);


        spheres = new ArrayList<>();
        spheres.add(sphere1);
        spheres.add(sphere2);
        spheres.add(sphere3);
        spheres.add(sphere4);
        spheres.add(sphere5);
        spheres.add(sphere6);
        spheres.add(bottom);
    }

    void run() {
        String NAME = "rt";
        JFrame frame = new JFrame(NAME);
        frame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.add(new ViewPort());
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);
        frame.addMouseWheelListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        Font font = new Font(FONT_NAME, Font.BOLD, pixelSize);
        g.setFont(font);


        g.setColor(Color.WHITE);

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

                Util.normalize(vectorDirection);

                Ray ray = new Ray(camera.position, vectorDirection, 10E5F);

                // Trace ray, return pixel data, fill pixel

                float brightness = trace(ray);

                g.setColor(Util.colorMultiply(light.color, brightness));
                g.drawString(".", x, y);

            }
        }
        t.start();
    }

    public float trace(Ray ray) {

        Sphere sphere = null;

        float brightness = 0.0F;

        // Find which sphere is hit
        for (Sphere sph : spheres) {
            if (ray.RaySphereIntersection(sph)) {
                sphere = sph;
            }
        }

        // If sphere is hit
        if (sphere != null) {


            if (sphere.reflective) {
                Vector3D point = Util.vectorAdd(ray.origin, Util.vectorMultiply(ray.direction, ray.length));
                Vector3D normal = Util.vectorSubtract(point, sphere.origin);
                Util.normalize(normal);
                Vector3D newDirection = Util.vectorReflect(ray.direction, normal);
                Ray reflectionRay = new Ray(point, newDirection, 100000000F);
                return trace(reflectionRay);
            }

            // Find exact intersection point on sphere
            Vector3D point = Util.vectorAdd(ray.origin, Util.vectorMultiply(ray.direction, ray.length));

            // Get normal of point
            Vector3D normal = Util.vectorSubtract(point, sphere.origin);

            Util.normalize(normal);

            // Calculate brightness of pixel/character
            Vector3D lightRay = Util.vectorSubtract(light.position, point);

            float len = Util.len(lightRay);

            // Have to normalize
            Util.normalize(lightRay);

            // Is a shadow if shadowRay intersects with anything on the way to the light
            boolean isShadow = false;
            Ray shadowRay = new Ray(point, lightRay, len);
//
            for (Sphere sph : spheres) {
                if (shadowRay.RaySphereIntersection(sph)) {
                    isShadow = true;
                    break;
                }
            }
//
            if (!isShadow) {
                brightness = Util.dotP(normal, lightRay);
                if (brightness < 0) {
                    brightness = 0;
                }
            }
        } else {
            return 0;
        }
        return brightness;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        if (rotate) {
            Util.rotate(spheres.get(1).origin, 0, 0.01F, 0);
            Util.rotate(spheres.get(2).origin, 0, 0.01F, 0);
            Util.rotate(spheres.get(3).origin, 0, 0.01F, 0);
            Util.rotate(spheres.get(4).origin, 0, 0.01F, 0);
        }
        Util.rotate(spheres.get(5).origin, 0,-0.01F, 0);
        Util.rotate(light.position, 0,-0.01F, 0);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        final float SPEED = 5F;
        final float DEG = 0.1F;
        Util.normalize(camera.direction);
        Vector3D forward = Util.vectorMultiply(camera.direction, SPEED);
        Vector3D right = Util.vectorMultiply(Util.rotateDXY(camera.direction), SPEED);

        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_E -> {
                Util.rotateY(camera.direction,DEG);
                Util.rotateAroundPoint(camera.position, screen.p0,0F, DEG,0F);
                Util.rotateAroundPoint(camera.position, screen.p1,0F, DEG,0F);
                Util.rotateAroundPoint(camera.position, screen.p2,0F, DEG,0F);
            }
            case KeyEvent.VK_Q -> {
                Util.rotateY(camera.direction,-DEG);
                Util.rotateAroundPoint(camera.position, screen.p0,0F, -DEG,0F);
                Util.rotateAroundPoint(camera.position, screen.p1,0F, -DEG,0F);
                Util.rotateAroundPoint(camera.position, screen.p2,0F, -DEG,0F);

            }
            case KeyEvent.VK_SHIFT -> {
                camera.position.y -= SPEED;
                screen.p0.y       -= SPEED;
                screen.p1.y       -= SPEED;
                screen.p2.y       -= SPEED;

            }
            case KeyEvent.VK_SPACE -> {
                camera.position.y += SPEED;
                screen.p0.y       += SPEED;
                screen.p1.y       += SPEED;
                screen.p2.y       += SPEED;

            }
            case KeyEvent.VK_W -> {
                camera.position = Util.vectorAdd(camera.position, forward);
                screen.p0       = Util.vectorAdd(screen.p0, forward);
                screen.p1       = Util.vectorAdd(screen.p1, forward);
                screen.p2       = Util.vectorAdd(screen.p2, forward);
            }
            case KeyEvent.VK_S -> {
                camera.position = Util.vectorSubtract(camera.position, forward);
                screen.p0       = Util.vectorSubtract(screen.p0, forward);
                screen.p1       = Util.vectorSubtract(screen.p1, forward);
                screen.p2       = Util.vectorSubtract(screen.p2, forward);

            }
            case KeyEvent.VK_A -> {
                camera.position = Util.vectorSubtract(camera.position, right);
                screen.p0       = Util.vectorSubtract(screen.p0, right);
                screen.p1       = Util.vectorSubtract(screen.p1, right);
                screen.p2       = Util.vectorSubtract(screen.p2, right);

            }
            case KeyEvent.VK_D -> {
                camera.position = Util.vectorAdd(camera.position, right);
                screen.p0       = Util.vectorAdd(screen.p0, right);
                screen.p1       = Util.vectorAdd(screen.p1, right);
                screen.p2       = Util.vectorAdd(screen.p2, right);

            }
            case KeyEvent.VK_F -> rotate = !rotate;
            case KeyEvent.VK_1 -> size = 3;
            case KeyEvent.VK_2 -> size = 4;
            case KeyEvent.VK_3 -> size = 5;
            case KeyEvent.VK_4 -> size = 6;
            case KeyEvent.VK_5 -> size = 7;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float DEG = -0.1F;
        if (e.getWheelRotation() == 1) {
            Util.rotateX(camera.direction, -DEG);
            Util.rotateAroundPoint(camera.position, screen.p0,-DEG, 0F,0F);
            Util.rotateAroundPoint(camera.position, screen.p1,-DEG, 0F,0F);
            Util.rotateAroundPoint(camera.position, screen.p2,-DEG, 0F,0F);

        } else {
            Util.rotateX(camera.direction, DEG);
            Util.rotateAroundPoint(camera.position, screen.p0,DEG, 0F,0F);
            Util.rotateAroundPoint(camera.position, screen.p1,DEG, 0F,0F);
            Util.rotateAroundPoint(camera.position, screen.p2,DEG, 0F,0F);
        }
    }
}
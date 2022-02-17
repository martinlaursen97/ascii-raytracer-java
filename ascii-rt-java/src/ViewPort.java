import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ViewPort extends JPanel implements ActionListener, KeyListener, MouseWheelListener {
    int WIDTH   = 1000;
    int HEIGHT  = 800;

    static int size = 8;
    static int FONT_SIZE = 10;

    String FONT_NAME = "Cambria";

    static Camera camera = new Camera(new Vector3D(0,0,0), new Vector3D(0,0,1));
    static Screen screen = new Screen(camera, 1F);

    Timer t = new Timer(1, this);

    static boolean rotate = true;

    //static List<Sphere> spheres;
    public static List<GameObject> objects;
    static Light light = new Light(new Vector3D(120,450,0), Color.WHITE);

    Shader shader = new Shader();

    public ViewPort() {
        Sphere sphere1 = new Sphere(true, new Vector3D(0,30,0), 40F);
        Sphere sphere2 = new Sphere(false, new Vector3D(100,30,0), 10F);
        Sphere sphere3 = new Sphere(false, new Vector3D(-100,30,0), 10F);
        Sphere sphere4 = new Sphere(false, new Vector3D(0,30,100), 10F);
        Sphere sphere5 = new Sphere(false, new Vector3D(0,30,-100), 10F);
        Sphere sphere6 = new Sphere(false, new Vector3D(125,455,0), 5F);
        Plane bottom = new Plane(false, new Vector3D(0, -50, 0), new Vector3D(0, 1, 0));

        Triangle t1 = new Triangle(false, new Vector3D(0, 200, 0), new Vector3D(100, 200, 0), new Vector3D(50, 200, -100));


        //ArrayList<Triangle> cube2 = Util.loadObject("teapot.txt", false);


        objects = new ArrayList<>();
        objects.add(sphere1);
        objects.add(sphere2);
        objects.add(sphere3);
        objects.add(sphere4);
        objects.add(sphere5);
        objects.add(sphere6);
        objects.add(t1);
        objects.add(bottom);

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

        Font font = new Font(FONT_NAME, Font.BOLD, FONT_SIZE);
        g.setFont(font);

        g.setColor(Color.WHITE);

        for (int y = 0; y < HEIGHT; y += size) {
            for (int x = 0; x < WIDTH; x += size) {
                // Scale
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

                TraceData pixelData = trace(ray);
                float brightness = pixelData.brightness;
                boolean isPlane = pixelData.isPlane;

                g.setColor(Util.colorMultiply(light.color, brightness));
                //g.fillRect(x, y, size, size);
                Character c = shader.getBrightness(brightness);

                if (isPlane) {
                    c = '.';
                }

                g.drawString(c.toString(), x, y);

            }
        }
        t.start();
    }

    public TraceData trace(Ray ray) {

        GameObject obj = null;

        // Find what object is hit
        for (GameObject o : objects) {
            if (o.intersect(ray)) {
                obj = o;
            }
        }

        // If object is hit
        if (obj != null) {

            float brightness = 0.0F;

            if (obj.reflective) {
                Vector3D pointOnObject = Util.vectorAdd(ray.origin, Util.vectorMultiply(ray.direction, ray.length));
                Vector3D normal = obj.originPointNormal(pointOnObject);

                Util.normalize(normal);
                Vector3D newDirection = Util.vectorReflect(ray.direction, normal);
                Ray reflectionRay = new Ray(pointOnObject, newDirection, 100000000F);
                return trace(reflectionRay);
            }

            // Find exact intersection point of object
            Vector3D pointOnObject = Util.vectorAdd(ray.origin, Util.vectorMultiply(ray.direction, ray.length));

            // Get normal of point

            Vector3D normal;
            if (obj instanceof Triangle) {
                normal = ((Triangle) obj).getNormal();
            } else {
                normal = obj.originPointNormal(pointOnObject);
                Util.normalize(normal);
            }

            // Calculate brightness of pixel/character
            Vector3D lightRay = Util.vectorSubtract(light.position, pointOnObject);

            float len = Util.len(lightRay);

            // Have to normalize
            Util.normalize(lightRay);

            // Is a shadow if shadowRay intersects with anything on the way to the light
            boolean isShadow = false;
            Ray shadowRay = new Ray(pointOnObject, lightRay, len);

            // Check if shadowRay intersects with any of the other objects
            for (GameObject o : objects) {
                if (obj instanceof Triangle) {
                    break;
                } else {
                    if (o.intersect(shadowRay)) {
                        isShadow = true;
                        break;
                    }
                }
            }
//
            if (!isShadow) {

                brightness = Util.dotP(normal, lightRay);

                if (brightness < 0) {
                    brightness = 0;
                }
            }

            boolean isPlane = obj instanceof Plane;

            return new TraceData(brightness, isPlane);
        }
        return new TraceData(0, false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        objects.get(0).rotate(0, 0.01F, 0);
        if (rotate) {
            objects.get(0).rotate(0, 0.01F, 0);

            objects.get(1).rotate(0, 0.01F, 0);
            objects.get(2).rotate(0, 0.01F, 0);
            objects.get(3).rotate(0, 0.01F, 0);
            objects.get(4).rotate(0, 0.01F, 0);
        }
        objects.get(5).rotate(0, -0.01F, 0);
        objects.get(6).rotate(0, 0.01F, 0);
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
            case KeyEvent.VK_1 -> size++;
            case KeyEvent.VK_2 -> {
                if (size != 1) {
                    size--;
                }
            }
            case KeyEvent.VK_3 -> {
                FONT_SIZE++;
            }
            case KeyEvent.VK_4 -> {
                if (size != 1) {
                    FONT_SIZE--;
                }
            }
            case KeyEvent.VK_5 -> {
                System.out.println("size=" + size + ", FONT_SIZE=" + FONT_SIZE);
            }
            case KeyEvent.VK_6 -> {
                spawnSphere(300, 200, 300, 10, 50);
            }
        }
    }

    public void spawnSphere(int x, int y, int z, int minRad, int maxRad) {
        int rad = ThreadLocalRandom.current().nextInt(minRad, maxRad + 1);

        float randomX = ThreadLocalRandom.current().nextInt(-x, x + 1);
        float randomY = ThreadLocalRandom.current().nextInt(0, y + 1);
        float randomZ = ThreadLocalRandom.current().nextInt(-z, z + 1);
        objects.add(new Sphere(false, new Vector3D(randomX, randomY, randomZ), rad));
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
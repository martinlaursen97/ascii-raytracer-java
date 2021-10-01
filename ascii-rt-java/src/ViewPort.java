import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ViewPort extends JPanel implements ActionListener {
    int WIDTH   = 1000;
    int HEIGHT  = 800;

    int size = 7;
    int pixelSize = size + 8;

    String FONT_NAME = "Cour";

    Camera camera = new Camera(new Vector3D(0,0,0), new Vector3D(0,0,1));
    Screen screen = new Screen(camera, 1F);

    Timer t = new Timer(1, this);

    List<Sphere> spheres = new ArrayList<>();
    Light light = new Light(new Vector3D(0,30,0), Color.WHITE);

    Shader shader = new Shader();

    public ViewPort() {
        Sphere sphere1 = new Sphere(new Vector3D(0,0,30), 10);
        Sphere bottom = new Sphere(new Vector3D(0, -1E5F, -2500), 1E5F);


        spheres.add(sphere1);
        spheres.add(bottom);
    }

    void run() {
        String NAME = "raytracer";
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

        Font font = new Font(FONT_NAME, Font.PLAIN, pixelSize);
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

                Ray ray = new Ray(camera.position, vectorDirection, 10E5F);

                // Trace ray, return pixel data, fill pixel

                PixelData traced = trace(ray);

                g.setColor(Util.colorMultiply(light.color, traced.brightness));
                g.drawString(String.valueOf(traced.character), x, y);

            }
        }
        t.start();
    }

    public PixelData trace(Ray ray) {

        int hitIndex = -1;

        // Find which sphere is hit
        for (int i = 0; i < spheres.size(); i++) {
            if (ray.RaySphereIntersection(spheres.get(i))) {
                hitIndex = i;
            }
        }

        // If sphere is hit
        if (hitIndex != -1) {

            Sphere sphere = spheres.get(hitIndex);

            // Find exact intersection point on sphere
            Vector3D point = Util.vectorAdd(ray.origin, Util.vectorMultiply(ray.direction, ray.length));

            // Get normal of point
            Vector3D normal = Util.vectorSubtract(point, sphere.origin);

            // Calculate brightness of pixel/character
            Vector3D lightRay = Util.vectorSubtract(light.position, point);

            float len = Util.len(lightRay);

            // Have to normalize
            Util.normalize(normal);
            Util.normalize(lightRay);

            // Is a shadow if shadowRay intersects with anything on the way to the light
            boolean isShadow = false;
            Ray shadowRay = new Ray(point, lightRay, len);

            for (Sphere sph : spheres) {
                if (shadowRay.RaySphereIntersection(sph)) {
                    isShadow = true;
                    break;
                }
            }

            if (!isShadow) {
                float brightness = Util.dotP(normal, lightRay);
                if (brightness < 0) {
                    brightness = 0;
                }
                return new PixelData(shader.getBrightness(brightness), brightness);
            } else {
                return new PixelData(' ', 0);
            }
        }
        return new PixelData(' ', 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
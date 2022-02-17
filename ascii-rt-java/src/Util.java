import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Util {
    public static Vector3D vectorSubtract(Vector3D v2, Vector3D v1) {
        return new Vector3D(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    public static Vector3D vectorMultiply(Vector3D v, float k) {
        return new Vector3D(v.x * k, v.y * k, v.z * k);
    }

    public static Vector3D vectorAdd(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static float dotP(Vector3D v1, Vector3D v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
    }

    public static float len(Vector3D v) {
        return (float) Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
    }

    public static void normalize(Vector3D v) {
        float len = len(v);
        v.x /= len;
        v.y /= len;
        v.z /= len;
    }

    public static Color colorMultiply(Color c, float k) {
        int R = (int) (c.getRed()   * k);
        int G = (int) (c.getGreen() * k);
        int B = (int) (c.getBlue()  * k);
        return new Color(R, G, B);
    }

    public static Vector3D vectorReflect(Vector3D incoming, Vector3D normal)  {
        return vectorSubtract(incoming, vectorMultiply(vectorMultiply(normal, 2F), dotP(incoming, normal)));
    }

    public static Vector3D crossP(Vector3D v1, Vector3D v2) {
        float a = v1.y * v2.z - v1.z * v2.y;
        float b = -(v1.x * v2.z - v1.z * v2.x);
        float c = v1.x * v2.y - v1.y * v2.x;
        return new Vector3D(a, b, c);
    }

    public static void rotateAroundPoint(Vector3D point, Vector3D v, float x, float y, float z) {
        v.translate(-point.x, -point.y, -point.z);
        rotate(v, x, y, z);
        v.translate(point.x, point.y, point.z);
    }

    public static void rotate(Vector3D v, float x, float y, float z) {
        rotateX(v, x);
        rotateY(v, y);
        rotateZ(v, z);
    }

    public static void rotateY(Vector3D v, float degrees) {
        float cos = (float) Math.cos(degrees);
        float sin = (float) Math.sin(degrees);
        float negSin = (float) -Math.sin(degrees);
        float dx = cos    * v.x + sin  * v.z;
        float dz = negSin * v.x + cos  * v.z;
        v.setLocation(dx, v.y, dz);
    }

    public static void rotateX(Vector3D v, float degrees) {
        float cos = (float) Math.cos(degrees);
        float sin = (float) Math.sin(degrees);
        float negSin = (float) -Math.sin(degrees);
        float dy = cos * v.y + negSin * v.z;
        float dz = sin * v.y + cos * v.z;
        v.setLocation(v.x, dy, dz);
    }

    public static void rotateZ(Vector3D v, float degrees) {
        float cos = (float) Math.cos(degrees);
        float sin = (float) Math.sin(degrees);
        float negSin = (float) -Math.sin(degrees);
        float dx = cos * v.x + negSin * v.y;
        float dy = sin * v.x + cos    * v.y;
        v.setLocation(dx, dy, v.z);
    }

    public static Vector3D rotateDXY(Vector3D p) {
        float cos = (float) Math.cos(Math.toRadians((float) 90));
        float sin = (float) Math.sin(Math.toRadians((float) 90));
        float negSin = (float) -Math.sin(Math.toRadians((float) 90));
        float newX = cos * p.x + 0 * p.y + sin * p.z;
        float newZ = negSin * p.x + 0 * p.y + cos * p.z;
        return new Vector3D(newX, 0, newZ);
    }

    public static ArrayList<Triangle> loadObject(String filePath) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Vector3D> vectors = new ArrayList<>();
        ArrayList<ArrayList<Integer>> iBuffer = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        vectors.add(new Vector3D(0,0,0));
        try {
            Scanner sc = new Scanner(new File(filePath));
            while (sc.hasNextLine()) {
                String text = sc.nextLine();
                String[] arr = text.split(" ");

                if (arr[0].equals("v")) {
                    float x = Float.parseFloat(arr[1]);
                    float y = Float.parseFloat(arr[2]);
                    float z = Float.parseFloat(arr[3]);
                    Vector3D v = new Vector3D(x, y, z);
                    vectors.add(v);
                }
                if (arr[0].equals("f")) {
                    for (int i = 1; i < arr.length; i++) {
                        temp.add(Integer.parseInt(arr[i]));
                    }
                    iBuffer.add(new ArrayList<>(temp));
                    temp.clear();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }

        for (ArrayList<Integer> i : iBuffer) {
            triangles.add(new Triangle(false,
                    new Vector3D(vectors.get(i.get(0))),
                    new Vector3D(vectors.get(i.get(1))),
                    new Vector3D(vectors.get(i.get(2)))));
        }
        return triangles;
    }
}



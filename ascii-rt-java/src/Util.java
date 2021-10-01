import java.awt.*;

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
}



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
}


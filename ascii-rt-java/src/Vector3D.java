public class Vector3D {
    float x;
    float y;
    float z;

    Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3D(Vector3D vector3D) {
        this.x = vector3D.x;
        this.y = vector3D.y;
        this.z = vector3D.z;
    }

    public void setLocation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public String toString() {
        return "x= " + x + ", y= " + y + " z=" + z;
    }
}

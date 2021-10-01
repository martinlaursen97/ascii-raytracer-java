public class Ray {
    Vector3D origin;
    Vector3D direction;
    float length;

    public Ray(Vector3D origin, Vector3D direction, float length) {
        this.origin = origin;
        this.direction = direction;
        this.length = length;
    }
}

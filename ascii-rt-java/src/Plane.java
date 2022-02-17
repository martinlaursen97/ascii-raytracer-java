public class Plane extends GameObject {
    Vector3D origin;
    Vector3D normal;

    public Plane(boolean reflective, Vector3D origin, Vector3D normal) {
        super(reflective);
        this.origin = origin;
        this.normal = normal;
    }

    @Override
    public boolean intersect(Ray ray) {
        float D = Util.dotP(normal, ray.direction);

        if (D < 0.0001F) {
            Vector3D p0l0 = Util.vectorSubtract(origin, ray.origin);
            float t = Util.dotP(p0l0, normal) / D;
            if (t <= 0) {
                return false;
            }

            Vector3D p = Util.vectorAdd(ray.origin, Util.vectorMultiply(ray.direction, t));
            Vector3D v = Util.vectorSubtract(p, ray.origin);
            float newLength = Util.len(v) * 1.001F;

            if (newLength < ray.length && newLength > 0F) {
                ray.length = newLength;
                return true;
            }
        }
        return false;
    }

    @Override
    public void rotate(float x, float y, float z) {
    }

    @Override
    public Vector3D originPointNormal(Vector3D point) {

        return this.normal;
    }
}

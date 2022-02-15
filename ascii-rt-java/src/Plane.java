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


        System.out.println("HERE");

        float D = Util.dotP(normal, ray.direction);

        if (D > 1e-6) {
            Vector3D p0l0 = Util.vectorSubtract(origin, ray.origin);
            float t = Util.dotP(p0l0, normal) / D;
            if (t >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void rotate(float x, float y, float z) {
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        return this.normal;
    }
}

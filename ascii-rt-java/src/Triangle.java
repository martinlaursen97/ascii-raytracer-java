public class Triangle extends GameObject {
    Vector3D pointA;
    Vector3D pointB;
    Vector3D pointC;

    public Triangle(boolean reflective, Vector3D pointA, Vector3D pointB, Vector3D pointC) {
        super(reflective);
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
    }

    @Override
    public boolean intersect(Ray ray) {
        return false;
    }

    @Override
    public void rotate(float x, float y, float z) {

    }

    @Override
    public Vector3D originPointNormal(Vector3D point) {
        return null;
    }

    public Vector3D getNormal() {
        Vector3D AB = Util.vectorSubtract(pointB, pointA);
        Vector3D AC = Util.vectorSubtract(pointC, pointA);
        Vector3D normal = Util.crossP(AB, AC);
        Util.normalize(normal);
        return normal;
    }
}

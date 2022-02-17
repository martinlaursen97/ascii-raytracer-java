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
        Plane triPlane = new Plane(false, pointA, getNormal());

        // calc ray length
        float nDotD = Util.dotP(triPlane.normal, ray.direction);
        if (Math.abs(nDotD) < 0.0001F) {
            return false;
        }

        float nDotPS = Util.dotP(triPlane.normal, Util.vectorSubtract(triPlane.origin, ray.origin));

        float tempT = ray.length;
        ray.length = nDotPS / nDotD;

        Vector3D planePoint = Util.vectorAdd(ray.origin, Util.vectorMultiply(ray.direction, ray.length));

        // test if plane point is within the triangle
        Vector3D edgeAB = Util.vectorSubtract(pointB, pointA);
        Vector3D edgeBC = Util.vectorSubtract(pointC, pointB);
        Vector3D edgeCA = Util.vectorSubtract(pointA, pointC);

        Vector3D AtoPoint = Util.vectorSubtract(planePoint, pointA);
        Vector3D BtoPoint = Util.vectorSubtract(planePoint, pointB);
        Vector3D CtoPoint = Util.vectorSubtract(planePoint, pointC);

        Vector3D aTestVec = Util.crossP(edgeAB, AtoPoint);
        Vector3D bTestVec = Util.crossP(edgeBC, BtoPoint);
        Vector3D cTestVec = Util.crossP(edgeCA, CtoPoint);

        boolean aTestVecMatchesNormal = Util.dotP(aTestVec, triPlane.normal) > 0.0F;
        boolean bTestVecMatchesNormal = Util.dotP(bTestVec, triPlane.normal) > 0.0F;
        boolean cTestVecMatchesNormal = Util.dotP(cTestVec, triPlane.normal) > 0.0F;

        if (aTestVecMatchesNormal && bTestVecMatchesNormal && cTestVecMatchesNormal) {
            return true;
        } else {
            ray.length = tempT;
            return false;
        }
    }

    @Override
    public void rotate(float x, float y, float z) {
        Util.rotate(pointA, x, y, z);
        Util.rotate(pointB, x, y, z);
        Util.rotate(pointC, x, y, z);
    }

    @Override
    public Vector3D originPointNormal(Vector3D point) {
        return Util.crossP(getNormal(), point);
    }

    public Vector3D getNormal() {
        Vector3D AB = Util.vectorSubtract(pointA, pointB);
        Vector3D AC = Util.vectorSubtract(pointA, pointC);
        Vector3D normal = Util.crossP(AB, AC);
        Util.normalize(normal);
        return normal;
    }
}

public class Sphere extends GameObject {
    Vector3D origin;
    float radius;

    public Sphere(boolean reflective, Vector3D origin, float radius) {
        super(reflective);
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public boolean intersect(Ray ray) {
        Vector3D originToSphere = Util.vectorSubtract(this.origin, ray.origin);
        float projection = Util.dotP(originToSphere, ray.direction);

        Vector3D projectionVector =  Util.vectorMultiply(ray.direction, projection);
        Vector3D distanceVector = Util.vectorSubtract(originToSphere, projectionVector);

        float distanceSq = Util.dotP(distanceVector, distanceVector);

        float radiusSq = this.radius*this.radius;

        // If the distance vector is shorter than the radius, it means that the ray hit the sphere
        if (distanceSq > radiusSq) {
            return false;
        }

        float newLength = (float) (projection - Math.sqrt(radiusSq-distanceSq));

        if (newLength < ray.length && newLength > 0F) {
            ray.length = newLength;
            return true;
        }

        return false;
    }

    @Override
    public void rotate(float x, float y, float z) {
        Util.rotate(this.origin, x, y, z);
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        return Util.vectorSubtract(point, this.origin);
    }
}

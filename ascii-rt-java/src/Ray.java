public class Ray {
    Vector3D origin;
    Vector3D direction;
    float length;

    public Ray(Vector3D origin, Vector3D direction, float length) {
        this.origin = origin;
        this.direction = direction;
        this.length = length;
    }

    public boolean RaySphereIntersection(Sphere sphere) {
        Vector3D originToSphere = Util.vectorSubtract(sphere.origin, origin);
        float projection = Util.dotP(originToSphere, direction);

        Vector3D projectionVector =  Util.vectorMultiply(direction, projection);
        Vector3D distanceVector = Util.vectorSubtract(originToSphere,projectionVector);

        float distanceSq = Util.dotP(distanceVector, distanceVector);

        float radiusSq = sphere.radius*sphere.radius;

        // If the distance vector is shorter than the radius, it means that the ray hit the sphere
        if (distanceSq > radiusSq) {
            return false;
        }

        float newLength = (float) (projection - Math.sqrt(radiusSq-distanceSq));

        if (newLength < length && newLength > 0F) {
            length = newLength;
            return true;
        }

        return false;
    }
}

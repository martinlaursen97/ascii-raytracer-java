public abstract class GameObject {
    boolean reflective;

    public GameObject(boolean reflective) {
        this.reflective = reflective;
    }

    public abstract boolean intersect(Ray ray);
    public abstract void rotate(float x, float y, float z);
    public abstract Vector3D getNormal(Vector3D point);
}

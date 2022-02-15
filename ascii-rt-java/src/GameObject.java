public abstract class GameObject {
    boolean reflective;

    public GameObject(boolean reflective) {
        this.reflective = reflective;
    }

    public abstract boolean intersect(Ray ray);
}

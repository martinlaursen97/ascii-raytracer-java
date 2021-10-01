public class Screen {
    Vector3D p0;
    Vector3D p1;
    Vector3D p2;
    Vector3D center;
    float near;

    public Screen(Camera camera, float near) {
        this.near = near;
        this.center = Util.vectorAdd(camera.position, Util.vectorMultiply(camera.direction, near));
        this.p0 = Util.vectorAdd(this.center, new Vector3D(-1,1,0));
        this.p1 = Util.vectorAdd(this.center, new Vector3D(1,1,0));
        this.p2 = Util.vectorAdd(this.center, new Vector3D(-1,-1,0));
    }
}

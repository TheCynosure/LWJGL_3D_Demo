import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Jack on 3/14/2016.
 */
public class Sprite {
    private Model model;
    private Vector3f position = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0;
    private float scale = 1;

    public Sprite(Model model) {
        this.model = model;
    }

    public Sprite(Model model, float x, float y, float z) {
        this.model = model;
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Sprite(Model model, float x, float y, float z, float rx, float ry, float rz) {
        this.model = model;
        position.x = x;
        position.y = y;
        position.z = z;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }
    
    public void rotate(float rx, float ry, float rz) {
        this.rx += rx;
        this.ry += ry;
        this.rz += rz;
    }

    public void scale(float scale) {
        scale = scale;
    }
    
    public void render(Shader shader, Camera camera) {
        shader.loadMatrix(shader.viewMatrixID, camera.createViewMatrix());
        shader.loadMatrix(shader.transformMatrixID, MatrixUtils.createTransformMatrix(position, rx, ry, rz, scale));
        model.render();
    }
}

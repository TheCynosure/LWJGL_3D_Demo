import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by john_bachman on 3/14/16.
 */
public class Camera {
    private Vector3f pos;
    float pitch, yaw;
    private Shader shader;
    private Matrix4f transMatrix;

    public Camera(int x, int y, int z, Shader shader) {
        pos = new Vector3f(x, y, z);
        this.shader = shader;
        transMatrix = new Matrix4f();

    }

    public void moveZ(float change) {
        pos.z += change;
    }

    public void updateView() {
        transMatrix.translate(pos);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        transMatrix.store(floatBuffer);
        floatBuffer.flip();
        int id = GL20.glGetUniformLocation(shader.programID, "transMatrix");
        GL20.glUniformMatrix4(id, false, floatBuffer);
    }
}

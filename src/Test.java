import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Test {

    private static int rotx = 0;

    public static void main(String[] args) {
        float[] vertices = {
            0.0f, -0.5f, 0.0f,
            0.5f, 0.0f, 0.0f,
            -0.5f, 0.0f, 0.0f
        };

        int[] indices = {
                0, 1, 2
        };

        Window window = new Window(800, 600, "Game Testing");
        ModelLoader loadModel = new ModelLoader();
        Model vertexModel = loadModel.createVertexModel(vertices, indices);
        GL30.glBindVertexArray(vertexModel.getVaoID());
        Shader shader = new Shader("shaders/vertexShader.vshr", "shaders/fragmentShader.fshr");
        int transMatrixID = GL20.glGetUniformLocation(shader.programID, "transMatrix");
        while(!Display.isCloseRequested()) {
            rotx += 5;
            GL11.glGetError();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            shader.start();
            shader.loadMatrix(transMatrixID, MatrixUtils.createTransformMatrix(new Vector3f(0,0,0), 0, rotx, 0, 1));
            vertexModel.render();
            shader.stopUsing();
            window.update();
            while(Keyboard.next()) {
                if(Keyboard.getEventKey() == Keyboard.KEY_A) {
                    rotx -= 10;
                } else if(Keyboard.getEventKey() == Keyboard.KEY_D) {
                    rotx += 10;
                }
            }
        }
        shader.stop();
        loadModel.clearPointers();
        Display.destroy();
    }
}

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import java.util.Scanner;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Test {

    static boolean switchModel = false;

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
        Sprite plane = new Sprite(loadModel.loadOBJ("res/cesnaTri.obj"));
        Sprite book = new Sprite(loadModel.loadOBJ("res/booklow.obj"));
        Shader shader = new Shader("shaders/vertexShader.vshr", "shaders/fragmentShader.fshr");
        Camera camera = new Camera(shader);
        plane.translate(0,0,-10);
        book.translate(0,0,-10);
        while(!Display.isCloseRequested()) {
            window.clean();
            shader.start();
            //Rendering between the Shader Calls
            if(switchModel) {
                plane.render(shader, camera);
                plane.rotate(0.5f, 0.5f, 0.5f);
            } else {
                book.render(shader, camera);
                book.rotate(0.5f, 0.5f, 0.5f);
            }
            shader.stop();
            window.update();
            while(Keyboard.next()) {
                if(Keyboard.getEventKey() == Keyboard.KEY_W) {
                    camera.move(0,0,-1);
                } else if(Keyboard.getEventKey() == Keyboard.KEY_S) {
                    camera.move(0,0,1);
                } else if(Keyboard.getEventKey() == Keyboard.KEY_A) {
                    camera.move(-1,0,0);
                } else if(Keyboard.getEventKey() == Keyboard.KEY_D) {
                    camera.move(1,0,0);
                } else if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
                    if (switchModel) {
                        switchModel = false;
                    } else {
                        switchModel = true;
                    }
                }
            }

            while(Mouse.next()) {
                camera.rotate(Mouse.getDY() / 3, Mouse.getDX() / 3, 0);
            }
        }
        shader.destroy();
        loadModel.clearPointers();
        Display.destroy();
    }
}

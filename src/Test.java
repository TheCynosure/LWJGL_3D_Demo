import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import java.util.Scanner;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Test {

    static boolean switchModel = false;
    static boolean paused = false;

    public static void main(String[] args) {
        Window window = new Window(800, 600, "Game Testing");
        OBJLoader loader = new OBJLoader();
        Sprite book = new Sprite(loader.loadObjModel("res/booklow.obj", "res/stone.png", "PNG"));
        Sprite book1 = new Sprite(loader.loadObjModel("res/booklow.obj", "res/Book_colorlowres.png", "PNG"));
        Sprite book2 = new Sprite(loader.loadObjModel("res/booklow.obj", "res/Book_colorlowres.png", "PNG"));
        GL30.glBindVertexArray(book.getModel().getVaoID());
        Shader shader = new Shader("shaders/vertexShaderTest.vshr", "shaders/fragmentShaderTest.fshr");
        GL30.glBindVertexArray(0);
        Camera camera = new Camera(shader);
        book.translate(-5, -5, -20);
        book1.translate(0, 2, -20);
        book2.translate(5, -2, -20);
        while(!Display.isCloseRequested()) {
            if(!paused) {
                window.clean();
                shader.start();
                //Rendering between the Shader Calls
                book.rotate(0.5f, 0.5f, 0.5f);
                book1.rotate(-0.5f, -0.5f, 0.5f);
                book2.rotate(0.5f, -0.5f, -0.5f);
                book.render(shader, camera);
                book1.render(shader, camera);
                book2.render(shader, camera);
                shader.stop();
            }
            window.update();
            if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
                camera.move(0,0,-1);
            } else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
                camera.move(0,0,1);
            } else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
                camera.move(-1,0,0);
            } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
                camera.move(1,0,0);
            }
            while(Keyboard.next()) {
                if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
                    if (switchModel) {
                        switchModel = false;
                    } else {
                        switchModel = true;
                    }
                } else if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
                    if (paused) {
                        paused = false;
                    } else {
                        paused = true;
                    }
                }
            }

            while(Mouse.next()) {
                if(!paused) {
//                    camera.rotate((float)(Mouse.getDY() * 0.5), (float)(Mouse.getDX() *0.5), 0);
                }


            }
        }
        loader.destroy();
        shader.destroy();
        Display.destroy();
    }
}

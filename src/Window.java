import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Window {

    private int WIDTH, HEIGHT;

    public Window(int WIDTH, int HEIGHT, String title) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        initDisplay(title);
    }

    private void initDisplay(String title) {
        try {
            ContextAttribs contextAttribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(title);
            Display.create(new PixelFormat(), contextAttribs);
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
            GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        Display.sync(60);
        Display.update();
    }

}

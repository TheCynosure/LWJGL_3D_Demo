import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.FloatBuffer;

/**
 * Created by john_bachman on 3/14/16.
 */
public class Shader {
    int programID;
    int vertexShaderID;
    int fragmentShaderID;

    public Shader(String vertexShaderPath, String fragmentShaderPath) {
        vertexShaderID = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);

        if( GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.out.println("Linking Error");
            System.out.println(GL20.glGetProgramInfoLog(programID, 500));
            System.exit(1);
        }

        GL20.glValidateProgram(programID);

        if( GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.out.println("Validation Error");
            System.out.println(GL20.glGetProgramInfoLog(programID, 500));
            System.exit(1);
        }
    }



    protected void loadMatrix(int matrixLocationID, Matrix4f matrix) {
        GL20.glUniformMatrix4(matrixLocationID, false, MatrixToFloatBuffer(matrix));
    }

    private FloatBuffer MatrixToFloatBuffer(Matrix4f matrix) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        matrix.store(floatBuffer);
        floatBuffer.flip();
        return floatBuffer;
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stopUsing() {
        GL20.glUseProgram(0);
    }

    public void stop() {
        stopUsing();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    private void bindAttributes() {
        bindAttribute(0, "position");
    }

    private void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    private int loadShader(String filePath, int type) {
        StringBuilder source = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }

        } catch (java.io.IOException e) {
            System.out.println("File could not be read!");
            e.printStackTrace();
            System.exit(1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);
        if( GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.out.println("Could not compile Shader!");
        }
        return shaderID;
    }
}

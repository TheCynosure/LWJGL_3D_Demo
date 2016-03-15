import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by john_bachman on 3/11/16.
 */

public class ModelLoader {

    ArrayList<Integer> vbos;
    ArrayList<Integer> vaos;

    public ModelLoader() {
        vaos = new ArrayList<Integer>();
        vbos = new ArrayList<Integer>();
    }

    public Model createVertexModel(float[] data, int[] indices) {
        int vaoID = createVAO();
        storeVerticesInVBO(data);
        int indicesID = storeIndicesInVBO(indices);
        return new Model(vaoID, indicesID, indices.length);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeVerticesInVBO(float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, toFloatBuffer(data), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        unbindVAO();
    }

    private int storeIndicesInVBO(int[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, toIntBuffer(data), GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        return vboID;
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer toFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer toIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void clearPointers() {
        for (int vboID : vbos) {
            GL15.glDeleteBuffers(vboID);
        }
        for (int vaoID : vaos) {
            GL30.glDeleteVertexArrays(vaoID);
        }
    }

    //TODO: Make this not return a vector2f
    private Vector2f findOccurences(String fileName) {
        int vertexCount = 0;
        int faceCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.length() > 2) {
                    if (line.substring(0, 2).equals("v ")) {
                        vertexCount++;
                    } else if (line.substring(0, 2).equals("f ")) {
                        faceCount++;
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return new Vector2f(vertexCount * 3, faceCount * 3);
    }

    public Model loadOBJ(String fileName) {
        System.out.println("Loading - " + fileName);
        Vector2f occurences = findOccurences(fileName);
        System.out.println("Vertices: " + (int)occurences.x);
        System.out.println("Faces: " + (int)occurences.y);
        float vertexs[] = new float[(int)occurences.x];
        int indexes[] = new int[(int)occurences.y];

        int vertexIndex = 0;
        int faceIndex = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.length() > 2) {
                    if (line.substring(0, 2).equals("v ")) {
                        Scanner scanner = new Scanner(line.substring(2));
                        while(scanner.hasNext()) {
                            vertexs[vertexIndex] = Float.parseFloat(scanner.next());
                            vertexIndex++;
                        }
                    } else if (line.substring(0, 2).equals("f ")) {
                        Scanner scanner = new Scanner(line.substring(2));
                        int repeat = 0;
                        while (scanner.hasNext()) {
                            indexes[faceIndex] = findVertexFromFace(scanner.next());
                            faceIndex++;
                            repeat++;
                            if(repeat >= 3) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < indexes.length; i++) {
            indexes[i] = indexes[i] - 1;
        }

        System.out.println("Finished Loading - " +  fileName);
        return createVertexModel(vertexs, indexes);
    }

    private int findVertexFromFace(String face) {
        int vertex = 0;
        for(int i = 0; i < face.length(); i++) {
            if(face.substring(i, i+1).equals("/")) {
                break;
            } else {
                vertex *= 10;
                vertex += Integer.parseInt(face.substring(i, i+1));
            }
        }
        return vertex;
    }
}

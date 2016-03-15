import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Model {
    int vaoID;
    int vertexCount;
    int indicesBufferID;

    public Model(int vaoID, int indicesBufferID, int vertexCount) {
        this.indicesBufferID = indicesBufferID;
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public void render() {
        //Bind the VAO so we can access the Vertices
        GL30.glBindVertexArray(vaoID);
        //Bind the first attribute because that's were we put the vertices.
        GL20.glEnableVertexAttribArray(0);
        //Bind the Indices which are in a separate VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferID);
        //Draw the elements using the indices to access the correct vertices.
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
        //Unbind the Indices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        //Unbind the vertex vbo
        GL20.glDisableVertexAttribArray(0);
        //Unbind the VAO
        GL30.glBindVertexArray(0);
    }

    public int getVaoID() {
        return vaoID;
    }
}

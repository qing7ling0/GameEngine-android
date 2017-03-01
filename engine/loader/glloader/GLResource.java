package loader.glloader;

import javax.microedition.khronos.opengles.GL10;

public interface GLResource {
	void load(GL10 gl);
	void reLoad(GL10 gl);
}

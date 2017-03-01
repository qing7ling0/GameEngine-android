package loader.glloader;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class GLResourceManage {

    private static GLResourceManage instance = new GLResourceManage();
	private ArrayList<GLResource>  reloadList;

	public GLResourceManage() {
		reloadList = new ArrayList<GLResource>();
	}

    public static GLResourceManage getInstance() {
        return instance;
    }

    public void addLoad(GLResource res) {
		reloadList.add(res);
	}
    
    public void removeRes(GLResource res)
    {
    	reloadList.remove(res);
    }

    public void reLoad(GL10 gl)
    {
    	for(int i=0; i<reloadList.size(); i++)
    	{
    		reloadList.get(i).reLoad(gl);
    	}
    }

	public void update(GL10 gl) {
		GLAsyncResourceLoaderHelper.getInstance().loadTexture(gl);
	}
	
	public void clean()
	{
		GLAsyncResourceLoaderHelper.getInstance().clean();
	}
	
	public int getReloadCount()
	{
		return reloadList.size();
	}
}

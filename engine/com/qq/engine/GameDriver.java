package com.qq.engine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import loader.glloader.GLAsyncResourceLoaderHelper;
import loader.glloader.GLResourceManage;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.widget.FrameLayout;

import com.baidu.location.e;
import com.qq.engine.action.Scheduler;
import com.qq.engine.events.EventsDispatcher;
import com.qq.engine.graphics.GGamePerform;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.graphics.text.TextTextureCache;
import com.qq.engine.log.LogOut;
import com.qq.engine.net.ConnPool;
import com.qq.engine.opengl.GLSurfaceView2D;
import com.qq.engine.scene.FPSLayer;
import com.qq.engine.scene.Scene;
import com.qq.engine.utils.Debug;
import com.qq.engine.view.EditInputView;
import com.qq.engine.view.GameView;
import com.qq.engine.view.Screen;

public class GameDriver implements GLSurfaceView.Renderer {
	
	public static Activity ANDROID_ACTIVITY;
	private static GameDriver instance;
	public static boolean outGame;
	
	public static Graphics g;

	private static GameView gameRootView;
	public static EditInputView gameEditInputView;

	public static long intervalTime;
	
	/** 当前正在运行的场景 */
	private Scene runningScene;
	
	/** 等待显示的场景 */
	private Scene waitShowScene;
	
	/** 场景队列 */
	private ArrayList<Scene> sceneList;
	
	/** 是否销毁场景 */
	private boolean isDestroyScene;
	
	private long lastRenderTime;
	private boolean drawFps;
	
	private boolean pause;
	
	/** 当前正绘制的时间  */
	private float dt;
	private int gameWidth;
	private int gameHeight;
	private int gameFps = 60;
	private long lastTime;
	private GLSurfaceView glView;
	private GameLauncher launcher;
	
	private Scene reloadScene;
	private Scene rootScene;
	
	private FPSLayer fpsLayer;
	
	private GameDriver(GameLauncher launcher)
	{
		this.launcher = launcher;
		this.gameWidth = launcher.getWidth();
		this.gameHeight = launcher.getHeight();
		this.sceneList = new ArrayList<Scene>();
		Screen.initGame(gameWidth, gameHeight);
		
		this.rootScene = Scene.create();
		this.rootScene.onEnter();
		this.fpsLayer = FPSLayer.create();
		this.rootScene.addChild(fpsLayer, 100000);
		
		GameDriver.ANDROID_ACTIVITY = launcher.getActivity();
		GameDriver.gameRootView = new GameView(ANDROID_ACTIVITY);
		GameDriver.gameEditInputView = new EditInputView(ANDROID_ACTIVITY);
		GameDriver.gameRootView.addView(gameEditInputView);
		
		this.loadGameView(GameDriver.ANDROID_ACTIVITY);
		GameDriver.ANDROID_ACTIVITY.setContentView(gameRootView);
		
	}
	
	public static void createInstance(GameLauncher launcher)
	{
		instance = new GameDriver(launcher);
	}
	
	public void loadGameView(Activity activity)
	{
		this.glView = new GLSurfaceView2D(activity);
		this.glView.setRenderer(this);
		GameDriver.gameRootView.addView(glView);
	}
	
	protected void destroyGameView()
	{
		GameDriver.gameRootView.removeView(glView);
	}
	
	public void onSizeChange(int width, int height)
	{
		Screen.initScreen(width, height);
	}
	
	public static GameDriver getInstance()
	{
		return instance;
	}
	
	public void start()
	{
		this.launcher.launch();
		GLAsyncResourceLoaderHelper.getInstance().start();
	}
	
	protected void render() {
		// TODO Auto-generated method stub
		try {
			ConnPool.parsePacket();
			GGamePerform.getInstance().update();
			EventsDispatcher.getInstance().update(runningScene);
			drawFrame();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Debug.e("GameDriver.run....error............!!!");
			Debug.initError(e, true);
//			StringWriter sw = new StringWriter();
//			e.printStackTrace(new PrintWriter(sw));
//			Debug.e("GameDriver===", sw.getBuffer().toString());
		}
	}

	public static FrameLayout getGameRootView() {
		return gameRootView;
	}

	public void drawFrame() {
		GLResourceManage.getInstance().update(Graphics.gl10);
		
		handlerWaitShowScene();

		if (lastRenderTime == 0)
		{
			lastRenderTime = System.currentTimeMillis();
		}
		dt = 0;
        long now = System.currentTimeMillis();
        
        dt = (now - lastRenderTime) * 0.001f;
        dt = Math.max(0, dt);
        lastRenderTime = now;
        
        TextTextureCache.getInstance().update(dt);
        Scheduler.getInstance().update(dt);

        if (this.rootScene != null)
		{
			this.rootScene.updating(dt);
			Graphics.batchBegin();
			paintGame(g);
			Graphics.batchEnd();
		}
	}
	
	
	private int fps;
	private float fpsTime;
	private int fpsCount; 
	private void paintGame(Graphics g)
	{
		g.begin();
		this.rootScene.visit(g);
		fpsTime += dt;
		if (fpsTime < 1)
		{
			fpsCount++;
		}
		else
		{
			fps = (int) ((1/fpsTime) * fpsCount);
			fpsCount = 1;
			fpsTime = dt;
		}
		if (drawFps && dt>0)
		{
			fpsLayer.setFps(fps);
		}
		
		g.end();
	}
	
	public void replaceScene(Scene scene)
	{
        int index = sceneList.size();
        isDestroyScene = true;
        if (index > 0)
        	sceneList.set(index - 1, scene);
        else
        {
    		sceneList.add(scene);
        }
        waitShowScene = scene;
	}
	
	public void pushScene(Scene scene)
	{
		isDestroyScene = false;
		sceneList.add(scene);
        waitShowScene = scene;
	}
	
	public void handlerWaitShowScene()
	{
		if ( waitShowScene != null)
		{
			if( runningScene != null ) {
				rootScene.removeChild(runningScene, isDestroyScene);
			}
			
			runningScene = waitShowScene;
			waitShowScene = null;
			rootScene.addChild(runningScene);
		}
	}
	
	public void popScene() {

		if (sceneList.size() == 1)
		{
        	quitGame();
		}
		else
		{
			sceneList.remove(sceneList.size() - 1);
			int count = sceneList.size();
	        waitShowScene = sceneList.get(count - 1);
		}
    }
	
	public void showReloadingScene()
	{
		reloadScene = launcher.createReloadScene();
		pushScene(reloadScene);
	}
	
	public void closeReloadingScene()
	{
		if (reloadScene != null)
		{
			popScene();
			reloadScene = null;
		}
	}

	public void onResume()
	{
//		loadGameView(ANDROID_ACTIVITY);
		Debug.e("GameDriver  game start");
		GLAsyncResourceLoaderHelper.getInstance().resume();
		if (pause)
		{
//			showReloadingScene();
		}
		pause = false;
	}
	
	public void onPause()
	{
		Debug.e("GameDriver  onPause");
		pause = true;
		GLAsyncResourceLoaderHelper.getInstance().pause();
	}
	
	public void destroy()
	{
		GLAsyncResourceLoaderHelper.getInstance().stop();
		Debug.colse();
        android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	
	public static void runOnUIThread( final Runnable runnable )
	{
		GameLauncher.getInstance().runOnUIThread(runnable);
	}
	
	public void quitGame()
	{
		GameLauncher.getInstance().quitGame();
	}

	/**
	 * @return the drawFps
	 */
	public boolean isDrawFps() {
		return drawFps;
	}

	/**
	 * @param drawFps the drawFps to set
	 */
	public void setDrawFps(boolean drawFps) { 
		this.drawFps = drawFps;
		this.fpsLayer.setVisible(drawFps);
	}
	
	public void beginRender(GL10 gl) {
		gl.glViewport(Screen.OFF_X, Screen.OFF_Y, Screen.SCALE_W, Screen.SCALE_H);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glOrthof(0, Screen.GAME_W, Screen.GAME_H, 0, -1, 1);
	}
	
	protected void setGLDefaults(GL10 gl)
	{
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// 纹理的使用与开启颜色渲染一样，需要开启纹理功能
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	}

	public void endRender(GL10 gl) {
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		Graphics.gl10 = gl;
		g = new Graphics();
		GameDriver.getInstance().start();
		Graphics.glAlphaFunc(Graphics.BLEND_NORMAL);
		
		TextTextureCache.getInstance().removeAll();
		GLResourceManage.getInstance().clean();
		GLAsyncResourceLoaderHelper.getInstance().setCanLoad(true);
		GLResourceManage.getInstance().reLoad(gl);
		
		setGLDefaults(gl);
		Log.e("", "GameDriver onSurfaceCreated gl10=" + Graphics.gl10);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		onSizeChange(width, height);
		beginRender(gl);
		Log.e("", "GameDriver onSurfaceChanged gl10=" + Graphics.gl10);
	}
	

	private long gap;
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gap = System.currentTimeMillis() - lastTime;
		long fpsgap = Math.max(1, 1000/gameFps - gap);
		try {
			Thread.sleep(fpsgap);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lastTime = System.currentTimeMillis();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		render();
	}

	public int getGameFps() {
		return gameFps;
	}

	public void setGameFps(int gameFps) {
		this.gameFps = gameFps;
	}
	
	public void setTouchEnable(boolean enable)
	{
		EventsDispatcher.getInstance().setDispatchEvents(enable);
	}
}

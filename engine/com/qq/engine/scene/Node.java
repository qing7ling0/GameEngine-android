package com.qq.engine.scene;

import java.util.ArrayList;

import com.qq.engine.action.Action;
import com.qq.engine.action.ActionManger;
import com.qq.engine.drawing.Color;
import com.qq.engine.drawing.PointF;
import com.qq.engine.drawing.RectangleF;
import com.qq.engine.drawing.SizeF;
import com.qq.engine.events.Event;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.opengl.glutils.Matrix3;
import com.qq.engine.utils.Debug;

/**
 * 
 * 节点
 * 
 * 界面中所有元素都是基于此类实现
 * 
 * @author wuqingqing
 * 
 */
public abstract class Node extends NodeProperty {
	public final static int NO_TAG = -1;

	/** 子节点集合 */
	protected ArrayList<Node> childrens;

	protected Node parent;

	public int zOrder;

	protected int tag;

	/** 变换矩阵 */
	protected Matrix3 matrix;
	/** 在世界坐标系中的矩阵 */
	protected Matrix3 worldMatrix;

	/** Action 集合 */
	protected ArrayList<Action> actionList;

	/** 节点置灰 */
	protected boolean isGray;

	/** 节点的绘制方式：top, right, bottom, left, hcenter, vcenter */
	protected int anchor;

	/** 绘制的锚点，0-1.0 */
	protected PointF anchorPoint;

	/** 触摸区域 */
	protected RectangleF worldRect;

	protected boolean isRunning;

	/** 相对于父级位置是否增加锚点的位移 */
	protected boolean isAnchorPointForPosition;

	/**
	 * 八种变换方式
	 * 
	 * */
	protected int transRot;

	/**
	 * 是否改变了状态，缩放、位置，旋转、颜色。。。 如果改变了，将会充值矩阵
	 * */
	protected boolean changeTrans;

	protected boolean parentChangeTrans;

	protected boolean parentColorChange;

	protected boolean isTouchEnabled;
	protected boolean isKeyEnabled;
	protected boolean swallowsTouches;
	protected ArrayList<String> singleTouchPointKeyList;

	/** 是否中断Event处理，包括中断子集的 */
	protected boolean isInterruptHandleEvent;
	
	protected SizeF size;

	protected Node() {
		super();
		anchorPoint = new PointF();
		tag = NO_TAG;
		isInterruptHandleEvent = false;
		size = SizeF.create(0, 0);
		ActionManger.getInstance().add(this);
	}

	protected void init() {
	}

	protected void initOffPoint() {
		setChangeTrans(true);
	}

	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub
		super.setWidth(width);
		this.size.width = width;
		setChangeTrans(true);
	}

	@Override
	public void setHeight(float height) {
		// TODO Auto-generated method stub
		super.setHeight(height);
		this.size.height = height;
		setChangeTrans(true);
	}

	public void setContentSize(float width, float height) {
		if (this.width != width || this.height != height) {
			setWidth(width);
			setHeight(height);
			initOffPoint();
		}
	}

	public void setContentSize(SizeF size) {
		this.setContentSize(size.width, size.height);
	}

	public void removeFromParent(boolean doClean) {
		if (parent != null) {
			parent.removeChild(this, doClean);
		}
	}

	public void removeSelf() {
		this.removeSelf(true);
	}

	public void removeSelf(boolean doClean) {
		this.removeFromParent(doClean);
	}

	public void removeChild(Node child) {
		this.removeChild(child, true);
	}

	public void removeChildByTag(int tag) {
		if (tag != NO_TAG) {
			Node node = getChildByTag(tag);
			if (node != null) {
				node.removeSelf();
			}
		}
	}

	public void removeChild(Node child, boolean doClean) {
		if (isRunning)
			child.onExit();

		if (doClean)
			child.clean();

		child.setParent(null);

		if (childrens != null)
			childrens.remove(child);
	}

	public void removeAll() {
		if (childrens != null) {
			int size = childrens.size();
			for (int i = 0; i < size; i++) {
				removeChild(childrens.get(i));
				size--;
				i--;
			}
		}
	}

	public void addChild(Node child) {
		addChild(child, child.getZOrder());
	}

	public void addChild(Node child, int zorder) {
		if (child == null)
			return;
		if (child.getParent() != null)
			return;

		if (childrens == null)
			childrens = new ArrayList<Node>();

		insertChild(child, zorder);

		child.setParent(this);
		if (isGray())
			child.setGray(true);
		if (isRunning)
			child.onEnter();
	}

	public void addChildToTop(Node child) {
		int zorder = 0;
		if (childrens != null) {
			zorder = childrens.get(childrens.size() - 1).getZOrder();
		}
		addChild(child, zorder);
	}

	public Node getChildByTag(int tag) {
		if (tag != NO_TAG && childrens != null) {
			for (Node node : childrens) {
				if (node.tag == tag) {
					return node;
				}
			}
		}

		return null;
	}

	public ArrayList<Node> getChildrens() {
		return childrens;
	}

	private void insertChild(Node node, int zorder) {
		node.zOrder = zorder;
		int ind = -1;
		ArrayList<Node> childs = childrens;
		for (int i = childs.size()-1; i > -1; i--) {
			Node ch = childs.get(i);
			if (ch.zOrder <= zorder) {
				ind = i+1;
				break;
			}
		}
		if (ind == -1) {
			ind = 0;
		}
		childs.add(ind, node);
	}

	public void onEnter() {
		isRunning = true;
		if (childrens != null) {
			if (childrens != null && childrens.size() > 0) {
				for (int i = 0; i < childrens.size(); i++) {
					childrens.get(i).onEnter();
				}
			}
		}
	}

	public void onExit() {
		if (childrens != null) {
			if (childrens != null && childrens.size() > 0) {
				for (int i = childrens.size() - 1; i > -1; i--) {
					childrens.get(i).onExit();
				}
			}
		}
		isRunning = false;
	}

	public void update(float dt) {
	}

	public void updating(float dt) {
		if (isRunning) {
			if (childrens != null && childrens.size() > 0) {
				for (int i = childrens.size() - 1; i > -1; i--) {
					childrens.get(i).updating(dt);
				}
			}
			update(dt);
		}
	}

	public Action getActionByTag(int tag) {
		if (tag != Action.TAG_NULL) {
			if (actionList == null) return null;
			int size = actionList.size();
			for (int i = 0; i < size; i++) {
				Action ac = actionList.get(i);
				if (ac.getTag() == tag) {
					return ac;
				}
			}
		}

		return null;
	}

	public void removeAction(Action action) {
		action.stop();
		action.destroy();
	}

	public void removeActionByTag(int tag) {
		if (tag != Action.TAG_NULL) {
			if (actionList == null) return;
			int size = actionList.size();
			for (int i = 0; i < size; i++) {
				Action ac = actionList.get(i);
				if (ac.getTag() == tag) {
					ac.stop();
					ac.destroy();
				}
			}
		}
	}

	public void removeAllAction() {
		if (actionList == null) return;
		for (Action ac : actionList) {
			ac.stop();
			ac.destroy();
		}
		actionList.clear();
	}

	public boolean hasAction() {
		if (actionList == null) return false;
		return actionList.size() > 0;
	}

	public void stopAction(Action action) {
		action.stop();
	}

	public void stopActionByTag(int tag) {
		if (actionList == null) return;
		if (tag != Action.TAG_NULL) {
			int size = actionList.size();
			for (int i = 0; i < size; i++) {
				Action ac = actionList.get(i);
				if (ac.getTag() == tag) {
					ac.stop();
				}
			}
		}
	}

	public void stopAllAction() {
		if (actionList == null) return;
		int size = actionList.size();
		for (int i = 0; i < size; i++) {
			Action ac = actionList.get(i);
			ac.stop();
		}
	}

	public void actionsUpdate(float dt) {
		if (actionList == null) return;
		int size = actionList.size();
		for (int i = 0; i < size; i++) {
			if (isRunning) {
				Action ac = actionList.get(i);
				boolean remove = false;
				if (ac.destroy) {
					remove = true;
				} else {
					// 这里还有点小问题，暂未修改
					if (ac.start)
						ac.step(1f / 20);
					if (ac.isDone()) {
						// 这里可能销毁自己，clean
						ac.stop();
						remove = true;
					}
				}
				if (remove) {
					// 如果已经clean actionList 为 null
					if (actionList != null) {
						boolean suc = actionList.remove(ac);
						if (suc) {
							i--;
							size--;
						}
					}
				}
			}
		}
	}

	public void draw(Graphics g) {
	}

	public void clean() {
		parent = null;
		if (childrens != null) {
			if (childrens != null && childrens.size() > 0) {
				for (int i = childrens.size() - 1; i > -1; i--) {
					childrens.get(i).clean();
				}
			}
		}
		if (actionList != null) actionList.clear();
		ActionManger.getInstance().remove(this);

		// actionList = null;
		// pushColor = null;
		// pushMat = null;
		// matrix = null;
		// worldMatrix = null;
		// worldRect = null;
		// anchorPoint = null;
		// offPoint = null;
		// color = null;
	}

	/**
	 * 转换成在父级的矩阵
	 * 
	 * @param mat
	 */
	public void convertToParentCoordinate(Matrix3 mat) {
		if (parent != null) {
			parent.convertToParentCoordinate(mat);
		}
		if (changeTrans || parentChangeTrans) {
			if (changeTrans) {
				changeTrans = false;
				transformSelf();
			}
			transformToWorld();
			parentChangeTrans = false;
		}
		
		if (matrix != null) mat.mul(matrix);
	}

	/**
	 * 相对于整个坐标系的坐标和尺寸
	 * 
	 * @return
	 */
	public void convertToWorldCoordinate() {
		PointF[] points = new PointF[2];
		points[0] = PointF.create(0, 0);
		points[1] = PointF.create(width, height);

		for (int i = 0; i < points.length; i++) {
			worldMatrix.matrixPoint(points[i]);
		}

		float x1 = points[0].x;
		float y1 = points[0].y;
		float x2 = points[1].x;
		float y2 = points[1].y;

		if (x1 > x2) {
			float temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			float temp = y1;
			y1 = y2;
			y2 = temp;
		}
		if (worldRect == null) worldRect = new RectangleF();
		worldRect.x = x1;
		worldRect.y = y1;
		worldRect.width = x2 - x1;
		worldRect.height = y2 - y1;
	}

	/**
	 * 重置矩阵
	 */
	public void transformSelf() {
		if (matrix == null) matrix = new Matrix3();
		else matrix.idt();
		
		if (px != 0 || py != 0)
			matrix.translate(px, py);

		if (rotation != 0)
			matrix.rotate(rotation);

		if (scaleX != 1 || scaleY != 1)
			matrix.scale(scaleX, scaleY);

		if (skewX != 0 || skewY != 0)
			matrix.skew(skewX, skewY);

		final float offx = width * anchorPoint.x;
		final float offy = height * anchorPoint.y;
		if (!isAnchorPointForPosition && (offx != 0 || offy != 0)) {
			transform(transRot, width, height, anchorPoint.x, anchorPoint.y);
		} else {
			transform(transRot, width, height, 0, 0);
		}
	}

	public void transformToWorld() {
		if (worldMatrix == null) worldMatrix = new Matrix3();
		Matrix3 worldMa = worldMatrix;

		worldMa.idt();
		if (parent != null) {
			Matrix3 parMa = parent.getWorldMatrix();
			if (parMa != null) worldMa.set(parMa);
		}
		if (matrix != null) worldMa.mul(this.matrix);
		onMatrixChange();
	}

	/**
	 * 发生矩阵变化
	 */
	protected void onMatrixChange() {
		convertToWorldCoordinate();
	}

	protected void pushMatrix(Matrix3 mat) {
		// if (pushMat == null) pushMat = new Matrix3();
		pushMat.set(mat);
	}

	protected void popMatrix(Matrix3 mat) {
		mat.set(pushMat);
	}

	protected void handleTransform() {
		if (changeTrans || parentChangeTrans) {
			if (changeTrans) {
				changeTrans = false;
				transformSelf();
			}
			transformToWorld();
			parentChangeTrans = false;
		}
	}

	private Matrix3 pushMat = new Matrix3();
	private Color pushColor = new Color();

	public void visit(Graphics g) {
		if (!visible) {
			return;
		}

		handleTransform();
		Color cor = g.color;
		pushColor.r = cor.r;
		pushColor.g = cor.g;
		pushColor.b = cor.b;
		pushColor.a = cor.a;
		if (color != null)
		{
			cor.mul(color);
		}

		ArrayList<Node> childrens = this.childrens;
		if (childrens != null) {
			for (int i = 0, count = childrens.size(); i < count; i++) {
				Node child = childrens.get(i);
				if (child.getZOrder() < 0) {
					child.visit(g);
				} else
					break;
			}
		}

		draw(g);

		if (childrens != null) {
			for (int i = 0, count = childrens.size(); i < count; i++) {
				Node child = childrens.get(i);
				if (child.getZOrder() >= 0) {
					child.visit(g);
				}
			}
		}
		cor.r = pushColor.r;
		cor.g = pushColor.g;
		cor.b = pushColor.b;
		cor.a = pushColor.a;
		parentColorChange = false;
	}

	/**
	 * 左上角位置
	 * 
	 * @return
	 */
	public PointF getTopLeftPoint() {
		PointF point = new PointF(px, py);
		if (!isAnchorPointForPosition) {
			final float offx = width * anchorPoint.x;
			final float offy = height * anchorPoint.y;
			point.x -= offx;
			point.y -= offy;
		}

		return point;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getZOrder() {
		return zOrder;
	}

	public void setZOrder(int zOrder) {
		this.zOrder = zOrder;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public void setAnchor(int anchor) {
		float anchorX = 0f;
		float anchorY = 0f;
		if ((anchor & Graphics.LEFT) == Graphics.LEFT) {

		} else if ((anchor & Graphics.HCENTER) == Graphics.HCENTER) {
			anchorX = 0.5f;
		} else if ((anchor & Graphics.RIGHT) == Graphics.RIGHT) {
			anchorX = 1f;
		}
		if ((anchor & Graphics.TOP) == Graphics.TOP) {

		} else if ((anchor & Graphics.VCENTER) == Graphics.VCENTER) {
			anchorY = 0.5f;
		} else if ((anchor & Graphics.BOTTOM) == Graphics.BOTTOM) {
			anchorY = 1f;
		}

		setAnchorPoint(anchorX, anchorY);
	}

	public void setAnchorPoint(float x, float y) {
		anchorPoint.x = x;
		anchorPoint.y = y;

		initOffPoint();
	}

	@Override
	public void setPosition(PointF position) {
		setPosition(position.x, position.y);
	}

	@Override
	public void setPosition(float x, float y) {
		if (px != x || py != y) {
			px = x;
			py = y;

			initOffPoint();
		}
	}

	@Override
	public void setPositionX(float x) {
		if (px != x) {
			px = x;

			initOffPoint();
		}
	}

	@Override
	public void setPositionY(float y) {
		if (py != y) {
			py = y;

			initOffPoint();
		}
	}

	/**
	 * 父级有没有发生矩阵变化
	 * 
	 * @return
	 */
	public boolean parentChangeTransform() {
		if (parent != null) {
			return parent.isChangeTrans();
		}
		return false;
	}

	public int getAnchor() {
		return anchor;
	}

	@Override
	public void setScaleX(float scaleX) {
		if (this.scaleX != scaleX) {
			this.scaleX = scaleX;
			this.setChangeTrans(true);
		}
	}

	@Override
	public void setScaleY(float scaleY) {
		if (this.scaleY != scaleY) {
			this.scaleY = scaleY;
			this.setChangeTrans(true);
		}
	}

	@Override
	public void setSkewX(float skewX) {
		if (this.skewX != skewX) {
			this.skewX = skewX;
			this.setChangeTrans(true);
		}
	}

	@Override
	public void setSkewY(float skewY) {
		if (this.skewY != skewY) {
			this.skewY = skewY;
			this.setChangeTrans(true);
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isAnchorPointForPosition() {
		return isAnchorPointForPosition;
	}

	public void setAnchorPointForPosition(boolean anchorPointForPosition) {
		if (this.isAnchorPointForPosition != anchorPointForPosition) {
			this.isAnchorPointForPosition = anchorPointForPosition;
			this.setChangeTrans(true);
		}
	}

	public Matrix3 getMatrix() {
		if (matrix == null) matrix = new Matrix3();
		return matrix;
	}

	public Matrix3 getWorldMatrix() {
		if (worldMatrix == null) worldMatrix = new Matrix3();
		return worldMatrix;
	}

	@Override
	public void setAlpha(int alpha) {
		if (this.alpha != alpha) {
			this.alpha = (short) alpha;
			if (color == null) color = new Color(1, 1, 1, 1);
			this.color.a = Color.alphaToFloat(alpha);
			this.setParentColorChange(true);
		}

	}

	@Override
	public void setRotation(float rotation) {
		if (this.rotation != rotation) {
			this.rotation = rotation;
			this.setChangeTrans(true);
		}
	}

	public RectangleF getWorldRect() {
		if (worldRect == null)
		{
			transformToWorld();
		}
		return worldRect;
	}

	/** 左右镜像 */
	public void mirrorLR() {
		this.scaleX *= -1;
		this.setChangeTrans(true);
	}

	/** 上下镜像 */
	public void mirrortTB() {
		this.scaleY *= -1;
		this.setChangeTrans(true);
	}

	public boolean isGray() {
		return isGray;
	}

	/**
	 * 置灰
	 * 
	 * @param isGray
	 */
	public void setGray(boolean isGray) {
		this.isGray = isGray;
		if (childrens != null) {
			if (childrens != null && childrens.size() > 0) {
				for (int i = childrens.size() - 1; i > -1; i--) {
					childrens.get(i).setGray(isGray);
				}
			}
		}
	}

	public int getTransRot() {
		return transRot;
	}

	public void setTransRot(int transRot) {
		if (this.transRot != transRot) {
			this.transRot = transRot;
			this.setChangeTrans(true);
		}
	}

	@Override
	public void setColor(Color color) {
		if (this.color == null) this.color = new Color(color);
		else this.color.set(color);
		this.setParentColorChange(true);
	}

	public void runAction(Action action) {
		if (actionList == null) actionList = new ArrayList<Action>();
		actionList.add(action);
		action.start(this);
	}

	protected void transform(int transfrom, float width, float height,
			float anchorx, float anchory) {
		if (matrix == null) matrix = new Matrix3();
		matrix.transform(transfrom, width, height, anchorx, anchory);
	}

	public boolean isChangeTrans() {
		return changeTrans;
	}

	public void setChangeTrans(boolean changeTrans) {
		this.changeTrans = changeTrans;
		if (this.changeTrans) {
			ArrayList<Node> childs = childrens;
			if (childs != null) {
				for (Node node : childs) {
					node.setParentChangeTrans(true);
				}
			}
		}
	}

	public boolean isParentChangeTrans() {
		return parentChangeTrans;
	}

	public void setParentChangeTrans(boolean parentChangeTrans) {
		this.parentChangeTrans = parentChangeTrans;
		if (this.parentChangeTrans) {
			ArrayList<Node> childs = childrens;
			if (childs != null) {
				for (Node node : childs) {
					node.setParentChangeTrans(true);
				}
			}
		}
	}

	public boolean isParentColorChange() {
		return parentColorChange;
	}

	public void setParentColorChange(boolean parentColorChange) {
		this.parentColorChange = parentColorChange;
		if (this.parentColorChange) {
			ArrayList<Node> childs = childrens;
			if (childs != null) {
				for (Node node : childs) {
					node.setParentColorChange(true);
				}
			}
		}
	}

	public void handle(Event event) {
		if (isInterruptHandleEvent)
			return;
		if (childrens != null) {
			if (childrens != null && childrens.size() > 0) {
				for (int i = childrens.size() - 1; i > -1; i--) {
					if (i < childrens.size()) {
						childrens.get(i).handle(event);
					}
				}
			}
		}
		event.handled(this);
	}

	public boolean onTouchBegan(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onTouchMoved(int x, int y) {
		// TODO Auto-generated method stub

	}

	public void onTouchEnded(int x, int y) {
		// TODO Auto-generated method stub

	}

	public void onTouchInterrupted() {
		// TODO Auto-generated method stub

	}

	public boolean isInterruptOther() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onTouchesBegan(PointF[] points) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onTouchesMoved(PointF[] points) {
		// TODO Auto-generated method stub

	}

	public void onTouchesEnded(PointF[] points) {
		// TODO Auto-generated method stub

	}

	public void onTouchesCancel(PointF[] points) {
		// TODO Auto-generated method stub

	}

	public boolean onKeyDown(int keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onKeyUp(int keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isKeyEnabled() {
		return isKeyEnabled;
	}

	public boolean isTouchEnabled() {
		return isTouchEnabled;
	}

	public void setTouchEnabled(boolean isTouchEnabled) {
		this.isTouchEnabled = isTouchEnabled;
	}

	public void setKeyEnabled(boolean isKeyEnabled) {
		this.isKeyEnabled = isKeyEnabled;
	}

	public boolean isSwallowsTouches() {
		return swallowsTouches;
	}

	public void setSwallowsTouches(boolean swallowsTouches) {
		this.swallowsTouches = swallowsTouches;
	}

	public void addTouchPointKey(String key) {
		if (singleTouchPointKeyList == null) singleTouchPointKeyList = new ArrayList<String>();
		if (!hasTouchPointKey(key)) {
			this.singleTouchPointKeyList.add(key);
		} else {
			Debug.e(this, "addTouchPointKey  has single Touch Point Key");
		}
	}

	public void removeTouchPointKey(String key) {
		if (singleTouchPointKeyList == null) return;
		this.singleTouchPointKeyList.remove(key);
	}

	public boolean hasTouchPointKey(String key) {
		if (singleTouchPointKeyList == null) return false;
		return this.singleTouchPointKeyList.contains(key);
	}

	public boolean isInterruptHandleEvent() {
		return isInterruptHandleEvent;
	}

	public void setInterruptHandleEvent(boolean isInterruptHandleEvent) {
		this.isInterruptHandleEvent = isInterruptHandleEvent;
	}

}

package com.qq.engine.graphics.image;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.qq.engine.utils.Debug;

import loader.GLImageLoaderManager;
import loader.glloader.GLResourceLoader;

public class ImagePacker {

	private Image img;
	private ImagePackerData packerData;
	
	public static ImagePacker create(GLImageLoaderManager loaderManager, GLResourceLoader loader, InputStream packerIs)
	{
		ImagePacker ij = new ImagePacker();
		ij.init(loaderManager, loader, packerIs);
		
		return ij;
	}

	public void init(GLImageLoaderManager loaderManager, GLResourceLoader loader, InputStream packerIs)
	{
		if (loader == null) return;
		this.packerData = ImagePackerData.create(loader.key, packerIs);
		if (packerData.createOk)
		{
			this.img = ImageCache.createImage(loaderManager, loader, packerData.scale);
		}
	}
	
	protected ImagePacker() {
	}
	
	public Image getImage(String name)
	{
		if (this.img == null) return null;
		
		String[] names = name.split(File.separator);
		name = names[names.length-1];
		
		ArrayList<ImageRegion> imgs = this.packerData.imgs;
		for(int i=0; i<imgs.size(); i++)
		{
			if (name != null && name.equals(imgs.get(i).filename))
			{
				return Image.create(img.getLoaderManager(), img, imgs.get(i));
			}
		}
		
		return null;
	}
	
	public Image getSourceImage()
	{
		return img;
	}
	
	public void recycle()
	{
		if (img != null) img.recycle();
		
		if (packerData != null)
		{
			packerData.recycle();
			packerData = null;
		}
	}

	public ImagePackerData getPackerData() {
		return packerData;
	}

	public void setPackerData(ImagePackerData packerData) {
		this.packerData = packerData;
	}
	
}

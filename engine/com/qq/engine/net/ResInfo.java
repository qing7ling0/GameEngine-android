package com.qq.engine.net;


/**
 * @author wuqingqing
 * 下载资源返回的相关信息
 */
public class ResInfo {

	/**下载资源:文件数量*/
	private byte	filesNum		= -1;
	/**下载资源:大小*/
	private int[]	filesSize		= null;
	/**下载资源:内容*/
	private byte[][]	filesContent	= null;
	
	public static ResInfo read(Packet in)
	{
		ResInfo res = new ResInfo();
		res.filesNum = in.decodeByte();
		res.filesSize		= new int[res.filesNum];
		res.filesContent	= new byte[res.filesNum][];		
		for(byte i = 0; i < res.filesNum; i++)
		{
			res.filesSize[i]	= in.decodeInt();
			res.filesContent[i] = in.decodeBytes(res.filesSize[i]);
		}
		return res;
	}

	/**获取文件内容
	 * @param index
	 * @return
	 */
	public byte[] getFileContent(int index)
	{
		if(filesSize[index] > 0)
		{
			return filesContent[index];
		}
		return null;
	}
	
	public byte[][] getFileContent()
	{
		return filesContent;
	}

	public byte getFilesNum() {
		return filesNum;
	}

	public void setFilesNum(byte filesNum) {
		this.filesNum = filesNum;
	}

	public int[] getFilesSize() {
		return filesSize;
	}
}

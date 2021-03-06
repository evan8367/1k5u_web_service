package com.zhenyulaw.jf.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiskFileClientImpl extends AbstractFileClientImpl {

	/**
	 * 路径分隔符
	 */
	private static final String DIR_SEPARATOR = "/";

	/**
	 * 
	 */
	@Autowired
	Properties propertiesConfig;

	@Override
	public boolean upLoad(String localFileName, String path, String fileName,boolean isFile) {
		String property = null;
		if (isFile) {
			property = propertiesConfig.getProperty("document.template.path");
		}else{
			property = propertiesConfig.getProperty("img.disk.path");
		}
		String localPath = property + DIR_SEPARATOR
				+ path;
		new File(localPath).mkdirs();
		File fileNew = new File(localPath + fileName);
		try {
			fileNew.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		fileChannelCopy(new File(localFileName), fileNew);
		return true;
	}

	public void fileChannelCopy(File s, File t) {

		FileInputStream fi = null;

		FileOutputStream fo = null;

		FileChannel in = null;

		FileChannel out = null;

		try {

			fi = new FileInputStream(s);

			fo = new FileOutputStream(t);

			in = fi.getChannel();// 得到对应的文件通道

			out = fo.getChannel();// 得到对应的文件通道

			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (fi != null) {
					fi.close();
				}
				if (in != null) {
					in.close();
				}
				if (fo != null) {
					fo.close();
				}
				if (out != null) {
					out.close();
				}

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

	@Override
	public boolean deleteFile(String localPathName) {
		String localPath = propertiesConfig.getProperty("disk.path") + DIR_SEPARATOR
				+ localPathName;
		File file = new File(localPath);
		if(file.exists())
			return file.delete();
		return false;
	}

}

/*
 * Copyright 2014 Habzy Huang
 */
package com.lib.imagesee;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageTools {
	private static ImageLoader instance = null;

	public static ImageLoader getImageLoaderInstance(Context context) {
		if (null == instance) {
			instance = getImageLoader(context);
		}
		return instance;
	}

	private static ImageLoader getImageLoader(Context context) {
		ImageLoader imageLoader = null;
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(context,
					CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheOnDisk(true).cacheInMemory(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					context)
					.defaultDisplayImageOptions(defaultOptions)
					.threadPoolSize(5)
					.diskCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(
							new UsingFreqLimitedMemoryCache(1024 * 1024 * 20));

			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

		} catch (Exception e) {

		}
		return imageLoader;
	}

	public static ArrayList<ItemModel> getGalleryPhotos() {
		File[] data = getImageFile();
		ArrayList<ItemModel> galleryList = new ArrayList<ItemModel>();
		ItemModel item;
		for (int i = 0, len = data.length; i < len; i++) {
			item = new ItemModel();
			item.mPath = data[i].getPath();
			Log.i("viewpicer", item.mPath);
			galleryList.add(item);
		}
		// try {
		// final String[] columns = { MediaStore.Images.Media.DATA,
		// MediaStore.Images.Media._ID };
		// final String orderBy = MediaStore.Images.Media._ID;
		//
		// Cursor imagecursor = resolver.query(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
		// null, null, orderBy);
		//
		// if (imagecursor != null && imagecursor.getCount() > 0) {
		//
		// while (imagecursor.moveToNext()) {
		// ItemModel item = new ItemModel();
		//
		// int dataColumnIndex = imagecursor
		// .getColumnIndex(MediaStore.Images.Media.DATA);
		//
		// item.mPath = "file://"
		// + imagecursor.getString(dataColumnIndex);
		//
		// galleryList.add(item);
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}

	static File[] getImageFile() {
		File[] data = null;
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/screenshot";
		File file = new File(path);
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (pathname.getName().endsWith(".jpg")) {
					return true;
				} else {
					return false;
				}

			}
		};
		data = file.listFiles(filter);
		if (null == data) {
			data = new File[0];
		}
		return data;
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

}

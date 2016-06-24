package com.chen.develop.zhihudaily.Manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 图片下载管理类
 * @author sun.luwei
 * @version 1.0
 */
public class ChatImageLoaderManager {
    private static ChatImageLoaderManager mInstance;
    private Context mContext;

    private ChatImageLoaderManager(Context mContext){
        this.mContext = mContext;
        initImageLoader(this.mContext);
    }

    public static ChatImageLoaderManager getInstance(Context mContext){
        if(mInstance==null){
            mInstance = new ChatImageLoaderManager(mContext);
        }
        return mInstance;
    }

    public static ChatImageLoaderManager getInstance(){
        if(mInstance==null){
            throw new IllegalStateException("mInstance must be init in MainApplication");
        }
        return mInstance;
    }

    /**
     * 显示普通配置图片
     * @param uri
     * @param imageView
     */
    public void displayImage(String uri, ImageView imageView){
        ImageLoader.getInstance().displayImage(
                uri, imageView, getImageLoaderCustomOptions());
    }

    /**
     * 显示圆形配置图片
     * @param uri
     * @param imageView
     */
//    public void displayRoundImage(String uri, ImageView imageView){
//        ImageLoader.getInstance().displayImage(
//                uri, imageView, getImageLoaderRoundOptions());
//    }

    /**
     * 显示自定义配置图片
     * @param uri
     * @param imageView
     * @param options
     */
    public void displayImage(String uri, ImageView imageView,DisplayImageOptions options){
        ImageLoader.getInstance().displayImage(
                uri, imageView, options);
    }

    /**
     * 无缓存加载
     * @param uri
     * @param imageView
     */
    public void displayImageNoCache(String uri, ImageView imageView){
        ImageLoader.getInstance().displayImage(
                uri, imageView, getImageLoaderNocacheOptions());
    }

    /**
     * 显示普通常用配置图片
     * @param uri
     * @param imageView
     * @param loadingImg
     * @param emptyImg
     * @param failImg
     */
    public void displayImage(String uri, ImageView imageView,int loadingImg,int emptyImg,int failImg){
        ImageLoader.getInstance().displayImage(
                uri, imageView, getImageLoaderCustomOptions(loadingImg, emptyImg, failImg));
    }

    /*
	 * protected void initImageLoader() { DisplayImageOptions defaultOptions =
	 * new
	 * DisplayImageOptions.Builder().resetViewBeforeLoading(true).imageScaleType
	 * (ImageScaleType.IN_SAMPLE_INT) // .displayer(new
	 * FadeInBitmapDisplayer(1000))
	 * .bitmapConfig(Bitmap.Config.RGB_565).showStubImage
	 * (R.drawable.picture_loading
	 * ).cacheInMemory(true).cacheOnDisc(true).build();
	 *
	 * ImageLoaderConfiguration config = new
	 * ImageLoaderConfiguration.Builder(getApplicationContext()) //
	 * .denyCacheImageMultipleSizesInMemory()
	 * .defaultDisplayImageOptions(defaultOptions).discCacheSize(50 * 1024 *
	 * 1024).memoryCacheSize(20 * 1024 *
	 * 1024).discCacheFileCount(100).memoryCache(new LruMemoryCache(2 * 1024 *
	 * 1024
	 * )).tasksProcessingOrder(QueueProcessingType.FIFO).memoryCacheSizePercentage
	 * (20) // 缓存一百张图片 .memoryCacheExtraOptions(240, 320).threadPoolSize(3) //
	 * .imageDecoder(new BaseImageDecoder())
	 * .threadPriority(Thread.NORM_PRIORITY - 1).imageDownloader(new
	 * BaseImageDownloader(mIns, 60 * 1000, 60 *
	 * 1000)).discCacheExtraOptions(240, 320, CompressFormat.JPEG, 50,
	 * null).build(); ImageLoader.getInstance().init(config); }
	 */

    private void initImageLoader(Context mContext) {
        File cacheDir = StorageUtils.getCacheDirectory(mContext);

        ImageLoaderConfiguration cofig = new ImageLoaderConfiguration.Builder(
                mContext)
                .threadPoolSize(5)
                        // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                        // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                        // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(50))
                        //.memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                        // default
                .diskCache(new UnlimitedDiscCache(cacheDir))
                        // default
                        //.diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        // default
                .imageDownloader(
                        new BaseImageDownloader(mContext)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();

        ImageLoader.getInstance().init(cofig);
    }

    /**
     * 默认options
     *
     * @return
     */
    private DisplayImageOptions getImageLoaderCustomOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_list_img) // resource
                 // or
                 // drawable
                 // or
                 // drawable
                 // or
                 // drawable
                .cacheInMemory(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisk(true)

                .build();

        return options;
    }

    /**
     * 将图片缓存到内存时的options
     *
     * @return
     */
    public static DisplayImageOptions getCachedOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisk(true)
                .build();
        return options;
    }


    /**
     * 圆形options
     *
     * @return
     */
  /*  private DisplayImageOptions getImageLoaderRoundOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.category_round_empty_photo)
                 // set round image
                .showImageForEmptyUri(R.drawable.category_round_empty_photo)
                .showImageOnFail(R.drawable.category_round_empty_photo)
                .displayer(new RoundedBitmapDisplayer(300))
                .displayer(new RoundedBitmapDisplayer(300))
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .build();

        return options;
    }*/

    private DisplayImageOptions getImageLoaderNocacheOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false).cacheOnDisk(false)
                .build();
        return options;
    }

    private DisplayImageOptions getImageLoaderCustomOptions(int loadingImg, int emptyImg, int failImg) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (loadingImg != -1){
            builder.showImageOnLoading(loadingImg); // resource
        }
        if (emptyImg != -1){
            builder.showImageForEmptyUri(emptyImg); // resource
        }
        if (failImg != -1){
            builder.showImageOnFail(failImg); // resource
        }
       return builder .build();
    }

    public void setBgDrawable(String url, final View v){
        ImageLoader.getInstance().loadImage(url, getImageLoaderCustomOptions(),
        new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap != null){
                    v.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
}

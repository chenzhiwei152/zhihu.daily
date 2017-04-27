package com.chen.common.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.chen.develop.zhihudaily.R;

/**
 * 图片下载管理类
 * @author sun.luwei
 * @version 1.0
 */
public class ImageLoaderManager {
    private static ImageLoaderManager mInstance;
    private Context mContext;

    private ImageLoaderManager(Context mContext){
        this.mContext = mContext;
    }

    public static ImageLoaderManager getInstance(Context mContext){
        if(mInstance==null){
            mInstance = new ImageLoaderManager(mContext);
        }
        return mInstance;
    }

    /**
     * 加载本地图片，url不作处理
     * @param context
     * @param url
     * @param imageView
     */
    public void displayNoFilter(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).placeholder(R.mipmap.welcome_bg).error(R.mipmap.welcome_bg).into(imageView);
    }
    public void display(Context context, String url, ImageView imageView) {
//        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(R.mipmap.welcome_bg).error(R.mipmap.welcome_bg).into(imageView);
    }

    public void display(Context context, String url, ImageView imageView, int defaultId) {
//        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(defaultId).into(imageView);
    }

    public void display(Context context, String url, ImageView imageView, int defaultId, int errorId) {
//        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(defaultId).error(errorId).into(imageView);
    }

    /**
     * @param context
     * @param url
     * @param imageView
     * @param degree    加载圆角
     */
    public void displayRound(Context context, String url, ImageView imageView, int degree) {
        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(R.mipmap.welcome_bg).error(R.mipmap.welcome_bg).transform(new GlideRoundTransform(context, degree)).into(imageView);
    }
    /**
     * @param context
     * @param url
     * @param imageView
     * @param defaultId 默认图片
     * @param degree    加载圆角
     */
    public void displayRound(Context context, String url, ImageView imageView, int defaultId, int degree) {
        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(defaultId).error(defaultId).transform(new GlideRoundTransform(context, degree)).into(imageView);
    }

    /**
     * @param context
     * @param url
     * @param imageView 加载圆形
     */
    public void displayCycle(Context context, String url, ImageView imageView) {
        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(R.mipmap.welcome_bg).error(R.mipmap.welcome_bg).transform(new GlideCircleTransform(context)).into(imageView);
    }

    /**
     * 带边框的圆形图片
     * @param context
     * @param url
     * @param imageView
     * @param borderWidth
     * @param borderColor
     */
    public void displayCycle(Context context, String url, ImageView imageView, int borderWidth, int borderColor) {
        url= compatibleUrl(url);
        Glide.with(context).load(url).transform(new GlideCircleBorderTransform(context,borderWidth,borderColor)).into(imageView);
    }

    /**
     * 有边框有默认图的圆形
     * @param context
     * @param url
     * @param imageView
     * @param defaultId
     * @param borderWidth
     * @param borderColor
     */
    public void displayCycle(Context context, String url, ImageView imageView, int defaultId, int borderWidth, int borderColor) {
        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(defaultId).error(defaultId).transform(new GlideCircleBorderTransform(context,borderWidth,borderColor)).into(imageView);
    }

    /**
     *有默认图片的圆形图
     * @param context
     * @param url
     * @param imageView
     * @param defaultId
     */
    public void displayCycle(Context context, String url, ImageView imageView, int defaultId) {
        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(defaultId).error(defaultId).transform(new GlideCircleTransform(context)).into(imageView);
    }

    /**
     * @param uri
     * @return
     * 兼容 链接问题
     */
    public  static String compatibleUrl(String uri) {

        if(null != uri){
            if(uri.startsWith("file://")){
                return  uri;
            }
            if(uri.contains("http://")){
                String[] split = uri.split("http://");
                uri ="http://"+ split[split.length-1];
                return uri;
            }
            if(uri.contains("https://")){
                String[] split = uri.split("https://");
                uri ="https://"+ split[split.length-1];
                return uri;
            }
        }
        return null;
    }


    /**
     *有默认图片的圆形图
     * @param context
     * @param radius
     */
    public void displayTopRounded(Context context, String url, ImageView imageView, int radius) {
        url= compatibleUrl(url);
        Glide.with(context).load(url).placeholder(R.mipmap.welcome_bg).error(R.mipmap.welcome_bg).bitmapTransform(new RoundedCornersTransformation (context,radius,0, RoundedCornersTransformation.CornerType.TOP)).into(imageView);
    }

    public static class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    /**
     * Glide 圆角 Transform
     */

    public static class GlideRoundTransform extends BitmapTransformation {

        private static float radius = 0f;

        /**
         * 构造函数 默认圆角半径 4dp
         *
         * @param context Context
         */
        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        /**
         * 构造函数
         *
         * @param context Context
         * @param dp      圆角半径
         */
        public GlideRoundTransform(Context context, int dp) {
            super(context);
            radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

    public static class GlideCircleBorderTransform extends BitmapTransformation {

        private Paint mBorderPaint;
        private float mBorderWidth;

        public GlideCircleBorderTransform(Context context) {
            super(context);
        }

        public GlideCircleBorderTransform(Context context, int borderWidth, int borderColor) {
            super(context);
            mBorderWidth =borderWidth;

            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }


        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            if (mBorderPaint != null) {
                float borderRadius = r - mBorderWidth / 2;
                canvas.drawCircle(r, r, borderRadius, mBorderPaint);
            }
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    public static class RoundedCornersTransformation implements Transformation<Bitmap> {
        public enum CornerType {
            ALL,
            TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
            TOP, BOTTOM, LEFT, RIGHT,
            OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
            DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
        }

        private BitmapPool mBitmapPool;
        private int mRadius;
        private int mDiameter;
        private int mMargin;
        private CornerType mCornerType;

        public RoundedCornersTransformation(Context context, int radius, int margin) {
            this(context, radius, margin, CornerType.ALL);
        }

        public RoundedCornersTransformation(BitmapPool pool, int radius, int margin) {
            this(pool, radius, margin, CornerType.ALL);
        }

        public RoundedCornersTransformation(Context context, int radius, int margin,
                                            CornerType cornerType) {
            this(Glide.get(context).getBitmapPool(), radius, margin, cornerType);
        }

        public RoundedCornersTransformation(BitmapPool pool, int radius, int margin,
                                            CornerType cornerType) {
            mBitmapPool = pool;
            mRadius = radius;
            mDiameter = mRadius * 2;
            mMargin = margin;
            mCornerType = cornerType;
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap source = resource.get();

            int width = source.getWidth();
            int height = source.getHeight();

            Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            drawRoundRect(canvas, paint, width, height);
            return BitmapResource.obtain(bitmap, mBitmapPool);
        }

        private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
            float right = width - mMargin;
            float bottom = height - mMargin;

            switch (mCornerType) {
                case ALL:
                    canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint);
                    break;
                case TOP_LEFT:
                    drawTopLeftRoundRect(canvas, paint, right, bottom);
                    break;
                case TOP_RIGHT:
                    drawTopRightRoundRect(canvas, paint, right, bottom);
                    break;
                case BOTTOM_LEFT:
                    drawBottomLeftRoundRect(canvas, paint, right, bottom);
                    break;
                case BOTTOM_RIGHT:
                    drawBottomRightRoundRect(canvas, paint, right, bottom);
                    break;
                case TOP:
                    drawTopRoundRect(canvas, paint, right, bottom);
                    break;
                case BOTTOM:
                    drawBottomRoundRect(canvas, paint, right, bottom);
                    break;
                case LEFT:
                    drawLeftRoundRect(canvas, paint, right, bottom);
                    break;
                case RIGHT:
                    drawRightRoundRect(canvas, paint, right, bottom);
                    break;
                case OTHER_TOP_LEFT:
                    drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
                    break;
                case OTHER_TOP_RIGHT:
                    drawOtherTopRightRoundRect(canvas, paint, right, bottom);
                    break;
                case OTHER_BOTTOM_LEFT:
                    drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
                    break;
                case OTHER_BOTTOM_RIGHT:
                    drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
                    break;
                case DIAGONAL_FROM_TOP_LEFT:
                    drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
                    break;
                case DIAGONAL_FROM_TOP_RIGHT:
                    drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
                    break;
                default:
                    canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint);
                    break;
            }
        }

        private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter),
                    mRadius, mRadius, paint);
            canvas.drawRect(new RectF(mMargin, mMargin + mRadius, mMargin + mRadius, bottom), paint);
            canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint);
        }

        private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius,
                    mRadius, paint);
            canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint);
            canvas.drawRect(new RectF(right - mRadius, mMargin + mRadius, right, bottom), paint);
        }

        private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom),
                    mRadius, mRadius, paint);
            canvas.drawRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom - mRadius), paint);
            canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint);
        }

        private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius,
                    mRadius, paint);
            canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint);
            canvas.drawRect(new RectF(right - mRadius, mMargin, right, bottom - mRadius), paint);
        }

        private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right, bottom), paint);
        }

        private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin, mMargin, right, bottom - mRadius), paint);
        }

        private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint);
        }

        private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint);
        }

        private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint);
        }

        private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom - mRadius), paint);
        }

        private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius,
                    paint);
            canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mRadius, bottom), paint);
        }

        private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right,
                                                   float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius,
                    paint);
            canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius,
                    paint);
            canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint);
        }

        private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right,
                                                      float bottom) {
            canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter),
                    mRadius, mRadius, paint);
            canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius,
                    mRadius, paint);
            canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mDiameter, bottom), paint);
            canvas.drawRect(new RectF(mMargin + mDiameter, mMargin, right, bottom - mRadius), paint);
        }

        private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right,
                                                       float bottom) {
            canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius,
                    mRadius, paint);
            canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom),
                    mRadius, mRadius, paint);
            canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint);
            canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint);
        }

        @Override
        public String getId() {
            return "RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter="
                    + mDiameter + ", cornerType=" + mCornerType.name() + ")";
        }
    }
}

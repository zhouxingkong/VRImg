package cn.bluemobi.dylan.vrdevelop;

import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by xingkong on 2017/10/4.
 */

public class VrManager implements GLSurfaceView.Renderer {

    private EffectContext mEffectContext;
    private Effect mEffect;
    private int[] mTextures = new int[2];
    private int mImageWidth;
    private int mImageHeight;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private boolean mInitialized = false;
    private int mCurrentEffect;
    Bitmap bitmap;  //要显示的图片
    public VrManager(){

    }
    public void setImageBitmap(Bitmap b){
        bitmap=b;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        // Nothing to do here
        //Only need to do this once
//        initEffect();
    }
    public void setEffect(boolean b){
        if(!b){
            mCurrentEffect = R.id.none;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        System.out.println("On Draw!!!!!!!!!!!");
        if (!mInitialized) {
//
            //mTexRenderer.init();
            mTexRenderer.init();
            initTextures();
            //System.out.println("调用了");
            mInitialized = true;
//            initEffect();
//            mCurrentEffect = R.id.none;
        }
        else{
            loadTextures();
        }


        if (mCurrentEffect != R.id.none) {
            //if an effect is chosen initialize it and apply it to the texture
            applyEffect();
        }
        renderResult();
    }

    private void initTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);
        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLToolbox.initTexParams();
//        bitmap.recycle();
        // Set texture parameters

    }

    private void loadTextures() {

        // Load input bitmap
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.puppy);
        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLToolbox.initTexParams();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
    }

    private void initEffect() {
        if (mEffect != null) {
//            mEffect.release();
            return;
        }
        mEffectContext = EffectContext.createWithCurrentGlContext();
        EffectFactory effectFactory = mEffectContext.getFactory();

        // Initialize the correct effect based on the selected menu/action item
        mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
        mEffect.setParameter("scale", .4f);
    }
    private void applyEffect() {
        System.out.println("加入效果");
        if (mEffect == null) {
//            mEffect.release();
            mEffectContext = EffectContext.createWithCurrentGlContext();
            EffectFactory effectFactory = mEffectContext.getFactory();

            // Initialize the correct effect based on the selected menu/action item
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
            mEffect.setParameter("scale", .4f);
        }

        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }
    private void renderResult() {
        if (mCurrentEffect != R.id.none) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        } else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

}

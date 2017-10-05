package cn.bluemobi.dylan.vrdevelop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity{
    GLSurfaceView imgLeft;
    GLSurfaceView imgRight;
    VrManager lManager=new VrManager();
    VrManager rManager=new VrManager();
    String dir;
    private int img_index=0;
    private int file_num=0;
    String[] files;
    List imglist;


    ImageView lBlank;
    ImageView rBlank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgs);
        hideBtns();

        lManager.setEffect(false);
        rManager.setEffect(false);
        Intent intent = getIntent();
        file_index=intent.getIntExtra("file",0);

        imgLeft=(GLSurfaceView) findViewById(R.id.view_l);
        imgRight=(GLSurfaceView)findViewById(R.id.view_r);

        imgLeft.setEGLContextClientVersion(2);
        imgLeft.setRenderer(lManager);
        imgLeft.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        imgRight.setEGLContextClientVersion(2);
        imgRight.setRenderer(rManager);
        imgRight.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        lBlank=(ImageView)findViewById(R.id.blank1);
        rBlank=(ImageView)findViewById(R.id.blank2);

        getFileList();
        String path = dir+ imglist.get(0);
        loadImage(path);
        imgLeft.setOnTouchListener(new lTouchListener());
        imgRight.setOnTouchListener(new vrTouchListener());
    }

    public void hideBtns(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public class vrTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event != null) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    System.out.println("onTouch!!");
                    touch();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public class lTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event != null) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    finish();
                }
                return true;
            } else {
                return false;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void getFileList(){
        dir = PATH_BASE+lists[file_index]+"/";
        File file = new File(dir);
        files = file.list();
        file_num=files.length;
        imglist= java.util.Arrays.asList(files);
        Collections.sort(imglist);
    }
    private void loadImage(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);


        int mImageWidth = bitmap.getWidth();
        int mImageHeight = bitmap.getHeight();
        if(mImageWidth>=mImageHeight){  //如果是横版照片就截取平移
            lBlank.setVisibility(View.GONE);
            rBlank.setVisibility(View.GONE);
            int dis=mImageWidth/6;
            Bitmap r=Bitmap.createBitmap (bitmap,dis-1,0,mImageWidth-dis,mImageHeight);    //左眼看到的是右边的那块
            Bitmap l=Bitmap.createBitmap (bitmap,0,0,mImageWidth-dis,mImageHeight);    //左眼看到的是右边的那块
            lManager.setImageBitmap(l);
            rManager.setImageBitmap(r);
        }
        else{   //如果是竖版照片就整体平移
            lBlank.setVisibility(View.VISIBLE);
            rBlank.setVisibility(View.VISIBLE);
            lManager.setImageBitmap(bitmap);
            rManager.setImageBitmap(bitmap);
        }
//        hideBtns();
        imgLeft.requestRender();
        imgRight.requestRender();

    }

    public void touch(){

        if(img_index<file_num-1) {
            System.out.println("下一张");
            img_index++;
            String path = dir+ imglist.get(img_index);
            loadImage(path);
        }
        else if(file_index<Dir_num-1){
            System.out.println("下一目录");
            file_index++;
            getFileList();
            img_index=0;
            String path = dir+ imglist.get(img_index);
            loadImage(path);
        }
        else{
            finish();
        }
    }

}

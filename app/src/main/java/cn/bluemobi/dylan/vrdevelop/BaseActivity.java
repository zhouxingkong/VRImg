package cn.bluemobi.dylan.vrdevelop;

import android.app.Activity;
import android.os.Bundle;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by xingkong on 2017/1/29.
 */

public class BaseActivity  extends Activity {

//    public static final String PATH_BASE= Environment.getExternalStorageDirectory().getPath() + "/vrImgs/";
//    public static final String PATH_BASE= "/storage/sdcard0" + "/vrImgs/";
public static final String PATH_BASE= "/storage/self/primary" + "/vrImgs/";
    public int file_index=0;
    public int Dir_num=0;
    public String[] lists;
    public List Dirlist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDirList();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
    public void getDirList(){
        File file = new File(PATH_BASE);
        lists = file.list();
        Dir_num=lists.length;
        Dirlist= java.util.Arrays.asList(lists);
        Collections.sort(Dirlist);
    }
}


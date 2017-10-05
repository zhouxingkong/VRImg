package cn.bluemobi.dylan.vrdevelop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by xingkong on 2017/1/29.
 */

public class SelectActivity extends BaseActivity {

    ListView fileList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        setListItem();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private void setListItem() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SelectActivity.this, R.layout.menu_list_item, lists);
        fileList= (ListView) findViewById(R.id.file_list);
        fileList.setAdapter(adapter);
        fileList.setOnItemClickListener(new SelectActivity.MenuItemClickListener());
    }
    class MenuItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.putExtra("file",position);
            intent.setClass(SelectActivity.this,MainActivity.class);
            startActivityForResult(intent,0);
        }
    }
}

package com.davim.testdexclassloader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private TextView mTextView;
    private Button mLoadClassButton;
    private Button mLoadSoButton;

    public static final String DEX_FILE_NAME = "show.dex";
    public static final String CLASS_FULL_NAME = "com.davim.testdexclassloader.ShowStringImpl";
    public static final String METHOD_NAME = "getContent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        mTextView = (TextView) findViewById(R.id.text);
        mLoadClassButton = (Button)findViewById(R.id.button_load_class);
        mLoadClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDex(MainActivity.this);
            }
        });
    }


    private void loadDex(Context context) {
        String dexPath = Environment.getExternalStorageDirectory().toString()
                + File.separator + DEX_FILE_NAME;

        // /data/data/{$packageName}/app_dex/
        File optimizedDirectory = context.getDir("dex", Context.MODE_PRIVATE);
        String optimizedDirectoryPath = optimizedDirectory.getAbsolutePath();

        // so加载路径可以为null
        String librarySearchPath = null;
        ClassLoader parentClassLoader = getClassLoader();

        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, optimizedDirectoryPath,
                librarySearchPath, parentClassLoader);

        Class clazz = null;
        // 使用DexClassLoader加载类
        try {
            clazz = dexClassLoader.loadClass(CLASS_FULL_NAME);
            Method getContent = clazz.getMethod(METHOD_NAME);
            String result = (String) getContent.invoke(clazz.newInstance());
            mTextView.setText(result);
        } catch (Exception e) {
            Log.d(TAG, "loadDex, exception: ", e);
        }


    }
}

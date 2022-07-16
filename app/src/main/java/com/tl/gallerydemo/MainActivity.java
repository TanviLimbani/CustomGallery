package com.tl.gallerydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tl.gallerydemo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static int selected_pos;
    Folders_Adapter foldersAdapter;
    FrameLayout frame_folder;
    ImageView img_back;
    ImageView img_up;
    LinearLayout li_dummy;
    Photos_Adapter photosAdapter;
    RecyclerView rv_foldersList;
    RecyclerView rv_photos;
    TextView txt_header;

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(1);
        setContentView(R.layout.activity_main);
        initView();

        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 101);
        } else {
            new AsyncTaskRunner().execute(new String[0]);
        }

    }

    private void initView() {
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.img_up = (ImageView) findViewById(R.id.img_up);
        this.frame_folder = (FrameLayout) findViewById(R.id.frame_folder);
        this.txt_header = (TextView) findViewById(R.id.txt_header);
        this.rv_photos = (RecyclerView) findViewById(R.id.rv_photos);
        this.rv_foldersList = (RecyclerView) findViewById(R.id.rv_foldersList);
        this.li_dummy = (LinearLayout) findViewById(R.id.li_dummy);
        this.rv_foldersList.setLayoutManager(new LinearLayoutManager(this));
        Folders_Adapter folders_Adapter = new Folders_Adapter(this, new Folders_Adapter.itemClick() {
            @Override
            public void OnClick(ArrayList<ImageData> arrayList, String str) {
                MainActivity.this.photosAdapter.setData(arrayList);
                MainActivity.this.frame_folder.setVisibility(8);
                MainActivity.this.txt_header.setText(str);
            }
        });
        this.foldersAdapter = folders_Adapter;
        this.rv_foldersList.setAdapter(folders_Adapter);
        this.rv_photos.setLayoutManager(new GridLayoutManager(this, 4));
        Photos_Adapter photos_Adapter = new Photos_Adapter(this, new Photos_Adapter.itemClick() {
            @Override
            public void OnClick(ImageData imageData) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ImageShowActivity.class).putExtra("selected_path", imageData.getPath()));
            }
        });
        this.photosAdapter = photos_Adapter;
        this.rv_photos.setAdapter(photos_Adapter);
        this.img_up.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (MainActivity.this.frame_folder.getVisibility() == 0) {
                    MainActivity.this.frame_folder.setVisibility(8);
                    MainActivity.this.img_up.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.up_arrow));
                    return;
                }
                MainActivity.this.frame_folder.setVisibility(0);
                MainActivity.this.img_up.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.down_arrow));
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });
        this.li_dummy.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
            }
        });
    }

    
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        public void onPostExecute(String str) {
        }

        private AsyncTaskRunner() {
        }

        @Override 
        protected void onPreExecute() {
            super.onPreExecute();
        }

        
        public String doInBackground(String... strArr) {
            HashMap<String, ArrayList<ImageData>> hashMap = new HashMap<>();
            hashMap.put(Constant.All_photos, new ArrayList<>());
            final ArrayList arrayList = new ArrayList();
            arrayList.add(Constant.All_photos);
            String[] strArr2 = {"_data", "bucket_display_name"};
            Cursor query = MainActivity.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr2, null, null, "datetaken DESC");
            if (query == null) {
                return "";
            }
            query.moveToFirst();
            while (!query.isAfterLast()) {
                String string = query.getString(query.getColumnIndex(strArr2[0]));
                String string2 = query.getString(query.getColumnIndex("bucket_display_name"));
                ImageData imageData = new ImageData(string, string2);
                ArrayList<ImageData> arrayList2 = hashMap.get(Constant.All_photos);
                arrayList2.add(imageData);
                hashMap.put(Constant.All_photos, arrayList2);
                if (arrayList.contains(string2)) {
                    ArrayList<ImageData> arrayList3 = hashMap.get(string2);
                    arrayList3.add(imageData);
                    hashMap.put(string2, arrayList3);
                } else {
                    ArrayList<ImageData> arrayList4 = new ArrayList<>();
                    arrayList4.add(imageData);
                    hashMap.put(string2, arrayList4);
                    arrayList.add(string2);
                }
                query.moveToNext();
            }
            Constant.folderData = hashMap;
            query.close();
            MainActivity.this.runOnUiThread(new Runnable() { 
                @Override 
                public void run() {
                    MainActivity.this.initPhotosAdapter(Constant.All_photos, 0, Constant.folderData.get(Constant.All_photos));
                    MainActivity.this.initFolderAdapter(Constant.folderData, arrayList);
                }
            });
            return "";
        }
    }

    
    public void initFolderAdapter(HashMap<String, ArrayList<ImageData>> hashMap, ArrayList<String> arrayList) {
        this.rv_foldersList.setItemViewCacheSize(arrayList.size());
        this.foldersAdapter.setData(hashMap, arrayList);
    }

    public void initPhotosAdapter(String str, int i, ArrayList<ImageData> arrayList) {
        this.txt_header.setText(str);
        selected_pos = i;
        this.photosAdapter.setData(arrayList);
    }

    @Override 
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 101) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                Toast.makeText(this, "Please allow this permission...", 0).show();
            } else {
                new AsyncTaskRunner().execute(new String[0]);
            }
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }
}

package pl.sdacademy.grabyourphoto.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pl.sdacademy.grabyourphoto.R;

public class GalleryActivity extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = 1;


    private ArrayList<String> list;


    private String resources;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private static GalleryAdapter galleryAdapter;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        list = new ArrayList<>();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);


        llm = new GridLayoutManager(GalleryActivity.this, 3);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(llm);
        galleryAdapter = new GalleryAdapter(list, GalleryActivity.this);
        recyclerView.setAdapter(galleryAdapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();

        if (Intent.ACTION_SEND.equals(action)) {
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                resources = getRealPathFromURI(GalleryActivity.this, imageUri);
                list.add(resources);

                galleryAdapter.notifyDataSetChanged();

            }
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {
                        showExplanation("Potrzebujemy pozwolenia", "Daj je nam bym załadowałć foto",
                                Manifest.permission.READ_EXTERNAL_STORAGE, RESULT_LOAD_IMAGE);
                    } else {
                        requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, RESULT_LOAD_IMAGE);
                    }
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }

            }
        });

    }


    private void requestPermissions(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }


    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                requestPermissions(permission, permissionRequestCode);
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            String resources = picturePath;
            list.add(resources);

            galleryAdapter.notifyDataSetChanged();
        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] allData = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, allData, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}

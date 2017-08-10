package pl.sdacademy.grabyourphoto.gallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import pl.sdacademy.grabyourphoto.Permission;
import pl.sdacademy.grabyourphoto.R;
import pl.sdacademy.grabyourphoto.gallery.db.DaoMaster;
import pl.sdacademy.grabyourphoto.gallery.db.DaoSession;
import pl.sdacademy.grabyourphoto.gallery.db.Image;
import pl.sdacademy.grabyourphoto.gallery.db.ImageDao;

public class GalleryActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    private List<Image> imageList;

    private Context context;

    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private static GalleryAdapter galleryAdapter;
    private FloatingActionButton fab;

    private DaoSession daoSession;
    private ImageDao imageDao;

    private Intent intent;
    private Bundle extras;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        context = GalleryActivity.this;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);

        DaoMaster.DevOpenHelper helperDB = new DaoMaster.DevOpenHelper(context, "users.db");
        Database db = helperDB.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        imageDao = daoSession.getImageDao();
        imageList = imageDao.queryBuilder().list();

        llm = new GridLayoutManager(GalleryActivity.this, 3);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(llm);
        galleryAdapter = new GalleryAdapter(imageList, context);
        recyclerView.setAdapter(galleryAdapter);

        intent = getIntent();
        extras = intent.getExtras();
        action = intent.getAction();



        if (Intent.ACTION_SEND.equals(action)) {
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                Image image = new Image();
                image.setResources(getRealPathFromURI(context, imageUri));
                imageDao.insert(image);

                Toast.makeText(context, "You picked 1 Images", Toast.LENGTH_LONG).show();

                imageList = imageDao.queryBuilder().list();
                recyclerView.setAdapter(new GalleryAdapter(imageList, context));
            }

        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {

            ClipData mClipData = intent.getClipData();

            Toast.makeText(context, "You picked " +
                            (mClipData.getItemCount() > 1 ? mClipData.getItemCount() + " Images" : mClipData.getItemCount() + " Image"),
                    Toast.LENGTH_LONG).show();

            for (int pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                 pickedImageCount++) {

                Uri imageUri = (Uri) mClipData.getItemAt(pickedImageCount).getUri();
                Image image = new Image();
                image.setResources(getRealPathFromURI(context, imageUri));
                imageDao.insert(image);
            }
            imageList = imageDao.queryBuilder().list();
            recyclerView.setAdapter(new GalleryAdapter(imageList, context));
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (ActivityCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    if ((ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this,
//                            Manifest.permission.READ_EXTERNAL_STORAGE))) {
//                        Permission.showExplanation("Potrzebujemy pozwolenia", "Daj je nam bym załadować foto",
//                                Manifest.permission.READ_EXTERNAL_STORAGE, PICK_IMAGE);
//                    } else {
//                        Permission.requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, PICK_IMAGE);
//                    }
//                } else {}
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(i, PICK_IMAGE);

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            ClipData mClipData = data.getClipData();

            if (mClipData != null) {
                Toast.makeText(context, "You picked " +
                                (mClipData.getItemCount() > 1 ? mClipData.getItemCount() + " Images" :
                                        mClipData.getItemCount() + " Image"),
                        Toast.LENGTH_LONG).show();

                for (int pickedImageCount = 0; pickedImageCount < mClipData.getItemCount(); pickedImageCount++) {

                    Uri imageUri = (Uri) mClipData.getItemAt(pickedImageCount).getUri();
                    Image image = new Image();
                    image.setResources(getRealPathFromURI(context, imageUri));
                    imageDao.insert(image);
                }
            } else {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Toast.makeText(context, "You picked 1 Image", Toast.LENGTH_LONG).show();

                Image image = new Image();
                image.setResources(picturePath);
                imageDao.insert(image);

            }

            imageList = imageDao.queryBuilder().list();
            recyclerView.setAdapter(new GalleryAdapter(imageList, context));

        }

    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] allData = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, allData, null, null, null);
            assert cursor != null;
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

package pl.sdacademy.grabyourphoto.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.DeleteQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.sdacademy.grabyourphoto.R;
import pl.sdacademy.grabyourphoto.gallery.db.DaoMaster;
import pl.sdacademy.grabyourphoto.gallery.db.DaoSession;
import pl.sdacademy.grabyourphoto.gallery.db.Image;
import pl.sdacademy.grabyourphoto.gallery.db.ImageDao;

/**
 * Created by mikr on 14/07/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<Image> images;
    private Context context;

    public GalleryAdapter(List<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_image_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "users.db");
        Database db = helper.getWritableDb();
        final DaoSession daoSession = new DaoMaster(db).newSession();
        final ImageDao imageDao = daoSession.getImageDao();

        final String imageResources = images.get(position).getResources();

        Glide.with(context).load("file://" + imageResources).override(350, 350).into(holder.imageViewSingleImage);

        holder.imageViewSingleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder removeDialog = new AlertDialog.Builder(context);
                removeDialog.setMessage("Remove Image?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DeleteQuery<Image> imageDeleteQuery = daoSession.queryBuilder(Image.class)
                                        .where(ImageDao.Properties.Id.eq(images.get(position).getId()))
                                        .buildDelete();
                                imageDeleteQuery.executeDeleteWithoutDetachingEntities();
                                daoSession.clear();

                                images.remove(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewSingleImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageViewSingleImage = (ImageView) itemView.findViewById(R.id.imageViewSingleImage);
        }
    }
}

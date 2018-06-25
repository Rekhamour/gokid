package com.gokids.yoda_tech.gokids.sos;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.gokids.yoda_tech.gokids.R;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class InfantsAdapter extends RecyclerView.Adapter<InfantsAdapter.MyViewHolder> {
    ArrayList<String> list;
    ArrayList<String> namelist;
    Context context;
    private String obj;

    public InfantsAdapter(Context context, ArrayList<String> list,ArrayList<String> nameList) {
        this.list = list;
        this.namelist = nameList;
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.infant_adapter_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("list ","list size"+list.size());
       if(!list.isEmpty()) {
           String videopath = list.get(position);
//           String name = namelist.get(position);
           Log.e("list ","list elements"+ videopath+ " ");
           // holder.name.setText(name);
          // Uri video = Uri.parse("http://www.servername.com/projects/projectname/videos/1361439400.mp4");
           //String path = "android.resource://" + context.getPackageName() + "/" + R.raw.v1;
           Bitmap bmThumbnail;
           bmThumbnail = ThumbnailUtils.createVideoThumbnail(list.get(position), MediaStore.Images.Thumbnails.MICRO_KIND);
         //  holder.thumbNail.setImageBitmap(bmThumbnail);
           MediaController mediaController= new MediaController(context);
           mediaController.setAnchorView(holder.address);
           holder.address.setVideoURI(Uri.parse(videopath));
           holder.address.requestFocus();
           holder.address.seekTo(10000);
           holder.address.setBackgroundResource(android.R.color.transparent);


           holder.contact_row_view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(holder.address.isPlaying())
                       //holder.address.stopPlayback();
                       holder.address.pause();
                   else
                   holder.address.start();

               }
           });
           holder.name.setText(namelist.get(position));
          // holder.address.pause();
          /* holder.address.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
               @Override
               public void onPrepared(MediaPlayer mp) {
                   mp.setLooping(true);
                   //holder.address.start();
                   mp.pause();
               }
           });
           holder.address.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(holder.address.isPlaying())
                       holder.address.stopPlayback();
                   else
                   holder.address.start();
               }
           });*/
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout contact_row_view;
        private final TextView name;
        private final VideoView address;
       // private final ImageView thumbNail;

        public MyViewHolder(View itemView) {
            super(itemView);
            contact_row_view = itemView.findViewById(R.id.parent_row_LL);
            address = itemView.findViewById(R.id.vdoview);
            name = itemView.findViewById(R.id.videoname);
           // thumbNail = (ImageView) itemView.findViewById(R.id.thumb);

        }
    }


}

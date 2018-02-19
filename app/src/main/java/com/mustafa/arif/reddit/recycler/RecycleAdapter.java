package com.mustafa.arif.reddit.recycler;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mustafa.arif.reddit.R;
import com.mustafa.arif.reddit.backend.ImageDownloader;
import com.mustafa.arif.reddit.backend.model.Children;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by musta on 2/18/2018.
 */


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private ArrayList<Children> children;
    private RecyclerViewListener recyclerViewListener;
    private static final String DIR = "reddit";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, author, count, entry_time;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            count = view.findViewById(R.id.count);
            entry_time = view.findViewById(R.id.entry_time);
            imageView = view.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reddit_layout_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (children == null && children.get(position) == null && children.get(position)
                .getData() == null) return;
        if (position == children.size() - 1) {
            recyclerViewListener.onBottom();
        }


        Children child = children.get(position);
        //special case when no data from server
        if (child.getData().getNum_comments() == -1) {
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.title.setText(child.getData().getTitle());
            holder.title.setTextSize(25.0f);
            holder.title.setTextColor(holder.title.getResources().getColor(R.color.colorPrimaryDark));
        }

        if (child.getData().getTitle() != null) holder.title.setText(child.getData().getTitle());
        if (child.getData().getAuthor() != null) holder.author.setText(child.getData().getAuthor());
        if (child.getData().getNum_comments() >= 0) holder.count.setText(holder.count.getResources()
                .getString(R.string.reddit_count, child.getData().getNum_comments()));
        if (child.getData().getCreated_utc() != null) {
            Date now = new Date();
            Date oldDate = new Date();
            oldDate.setTime(child.getData().getCreated_utc() * 1000);
            holder.entry_time.setText(
                    holder.entry_time.getResources().getString(
                            R.string.reddit_add_time,
                            TimeUnit.MILLISECONDS.toHours(now.getTime() - oldDate.getTime())
                    )
            );
        }
        picassoHandle(holder, child);
    }

    private void picassoHandle(MyViewHolder holder, Children child) {
        String thumbnail = child.getData().getThumbnail();
        if (thumbnail == null || thumbnail.isEmpty()) {

            return;
        }
        final String iPath = createFolder(DIR)
                .concat(createImageFileName(child.getData().getAuthor()));

        File myImageFile = new File(iPath);
        if (myImageFile.exists()) {
            Picasso.with(holder.imageView.getContext())
                    .load(myImageFile).placeholder(holder.imageView.getContext().getResources()
                    .getDrawable(R.mipmap.ic_launcher))
                    .error(holder.imageView.getContext().getResources()
                            .getDrawable(R.mipmap.ic_launcher))
                    .into(holder.imageView);
        } else {
            Picasso.with(holder.imageView.getContext())
                    .load(thumbnail)
                    .placeholder(holder.imageView.getContext().getResources()
                            .getDrawable(R.mipmap.ic_launcher))
                    .error(holder.imageView.getContext().getResources()
                            .getDrawable(R.mipmap.ic_launcher))
                    .into(holder.imageView);
            //download images
            Picasso.with(holder.imageView.getContext())
                    .load(thumbnail)
                    .placeholder(holder.imageView.getContext().getResources()
                            .getDrawable(R.mipmap.ic_launcher))
                    .error(holder.imageView.getContext().getResources()
                            .getDrawable(R.mipmap.ic_launcher))
                    .into(new ImageDownloader(holder.imageView.getContext(),
                            DIR, child.getData().getAuthor()));
        }
    }

    @Override
    public int getItemCount() {
        return children == null ? 0 : children.size();
    }

    /**
     * Add arraylist to recycle adapter.
     *
     * @param children ArrayList to attach
     */
    public void add(ArrayList<Children> children) {
        this.children = children;
    }

    /**
     * set listener to get event from adapter
     *
     * @param recyclerViewListener listener to attach
     */
    public void setOnClickListener(RecyclerViewListener recyclerViewListener) {
        this.recyclerViewListener = recyclerViewListener;

    }

    private static String createImageFileName(String fileName) {
        if (fileName == null) return "";
        return fileName.concat(".png");
    }

    private static String createFolder(String name) {
        String folderName = (Environment.getExternalStorageDirectory().getAbsolutePath().toString())
                .concat("/")
                .concat(name)
                .concat("/");

        File dir = new File(folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return folderName;
    }
}


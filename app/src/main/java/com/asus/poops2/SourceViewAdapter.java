package com.asus.poops2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SourceViewAdapter extends RecyclerView.Adapter<SourceViewAdapter.ViewHolder> {

    private static final String TAG = "SourceViewAdapter";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private Context mContext;

    public SourceViewAdapter(Context context, ArrayList<String> names, ArrayList<String> ImageUrl){
        mNames = names;
        mImageUrl = ImageUrl;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: called.");

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitemdown, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
    Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrl.get(i))
                .into(viewHolder.gambar);
        viewHolder.judul.setText(mNames.get(i));
        viewHolder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: "+mNames.get(i));
                Toast.makeText(mContext, mNames.get(i), Toast.LENGTH_SHORT).show();
                Intent y = new Intent(mContext, IsimateriActivity.class);
                mContext.startActivity(y);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView gambar;
        TextView judul;

        public ViewHolder(View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambarr);
            judul = itemView.findViewById(R.id.judull);
        }

    }
}

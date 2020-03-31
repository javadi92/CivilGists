package com.javadi92.civilgists.view;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.javadi92.civilgists.R;
import com.javadi92.civilgists.model.Gist;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GistAdapter extends RecyclerView.Adapter<GistAdapter.gistViewHolder> {

    List<Gist> gists=new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public GistAdapter(List<Gist> gists){
        this.gists=gists;
    }

    @NonNull
    @Override
    public gistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gists_layout,parent,false);
        return new gistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final gistViewHolder holder, final int position) {
        holder.textViewTitle.setText(gists.get(position).getTitle());
        holder.textViewDate.setText(gists.get(position).getDate());
        Uri uri=Uri.parse(gists.get(position).getImage());
        if(!uri.toString().equals("")){
            //holder.imageViewGist.setImageURI(uri);
            //holder.imageViewGist.setVisibility(View.VISIBLE);
            Picasso.get().load(new File(String.valueOf(uri))).fit().into(holder.imageViewGist);
        }
        else{
            //holder.imageViewGist.setVisibility(View.GONE);
            holder.imageViewGist.setImageResource(R.drawable.no_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null && position!=RecyclerView.NO_POSITION){
                    onItemClickListener.click(gists.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gists.size();
    }


    //methodi baraye eelame taghir dar vaziate adapter
    public void setTask(List<Gist> newGists) {
        gists=newGists;
        Collections.reverse(gists);
        Collections.sort(gists,Gist.GIST_COMPRATOR);
        notifyDataSetChanged();
    }


    //interfaci baraye handle kardane roydade click rooye itemhaye recyclerview
    interface OnItemClickListener{
        void click(Gist gist);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    class gistViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle,textViewDate;
        ImageView imageViewGist;

        public gistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.tv_title_card);
            textViewDate=itemView.findViewById(R.id.textView_date);
            imageViewGist=itemView.findViewById(R.id.imageView_card);
        }
    }

}

package com.charon.opengles30studydemo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {
    private List<MainBean> mList;
    private Context mContext;

    public MainAdapter(Context context, List<MainBean> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_main, null);
        MainHolder holder = new MainHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MainHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        public MainHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
        }

        public void setData(int position) {
            if (position >= mList.size()) {
                return;
            }
            final MainBean mainBean = mList.get(position);
            mName.setText(mainBean.getName());
            mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, mainBean.getStartClazz());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}



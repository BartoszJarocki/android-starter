package com.starter.ui.contributors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.starter.R;
import com.starter.data.model.Contributor;
import com.starter.ui.base.ListAdapter;

public class ContributorsAdapter extends ListAdapter<ContributorsAdapter.ViewHolder, Contributor> {

    private Picasso picasso;

    public ContributorsAdapter(final Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_contributor, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, picasso);
        viewHolder.itemView.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Contributor contributor = getItem(position);

        holder.bindData(contributor);
        holder.itemView.setTag(contributor);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public Contributor getItem(int position) {
        return dataList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Picasso picasso;

        @BindView(R.id.image_avatar) ImageView imageAvatar;
        @BindView(R.id.text_name) TextView textName;
        @BindView(R.id.text_desc) TextView textDesc;

        @BindString(R.string.contributions) String contributionsFormat;

        public ViewHolder(View view, Picasso picasso) {
            super(view);
            this.picasso = picasso;

            ButterKnife.bind(this, view);
        }

        public void bindData(final Contributor model) {
            textName.setText(model.login);
            textDesc.setText(String.format(contributionsFormat, model.contributions));

            picasso.load(model.avatarUrl).into(imageAvatar);
        }
    }
}

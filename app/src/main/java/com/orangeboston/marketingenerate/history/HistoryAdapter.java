package com.orangeboston.marketingenerate.history;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.orangeboston.marketingenerate.R;

import java.util.List;

public class HistoryAdapter
        extends BaseQuickAdapter<String, BaseViewHolder>
        implements DraggableModule {

    public HistoryAdapter(List<String> data) {
        super(R.layout.item_history_list_activity, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, @NonNull String item) {
        helper.setText(R.id.text, item);
    }
}
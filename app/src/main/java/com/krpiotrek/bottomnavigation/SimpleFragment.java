package com.krpiotrek.bottomnavigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krpiotrek.library.BottomNavigation;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragment extends Fragment {

    private class SimpleAdapter extends RecyclerView.Adapter<Holder> {

        public SimpleAdapter(List<Item> items) {
            this.items = items;
        }

        private List<Item> items;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final Item item = items.get(position);
            holder.setText(item.getText());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SimpleAdapter(getItems()));

        MainActivity activity = (MainActivity) getActivity();

        final BottomNavigation nagivation = activity.getNagivation();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                nagivation.passScrollY(dy);
            }
        });
    }

    private List<Item> getItems() {
        final List<Item> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Item("value : " + Math.random() * 100));
        }
        return list;
    }
}

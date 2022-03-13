package com.example.focusstarttest;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ValuteRefresh implements SwipeRefreshLayout.OnRefreshListener {

    private ValutesAdapter valutesAdapter;

    public ValuteRefresh(ValutesAdapter valutesAdapter) {
        this.valutesAdapter = valutesAdapter;
    }

    @Override
    public void onRefresh() {
        valutesAdapter.notifyDataSetChanged();

    }
}

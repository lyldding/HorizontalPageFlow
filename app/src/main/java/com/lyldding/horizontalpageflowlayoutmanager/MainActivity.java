package com.lyldding.horizontalpageflowlayoutmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyldding.library.horizontalpage.HorizontalPageFlowLayoutManager;
import com.lyldding.library.horizontalpage.PagingScrollHelper;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerViewTop;
    RecyclerView mRecyclerViewBottom;
    RecyclerView mRecyclerViewMid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TestAdapter testAdapter = new TestAdapter();

        mRecyclerViewTop = findViewById(R.id.top);
        PagingScrollHelper pagingScrollHelper1 = new PagingScrollHelper();
        pagingScrollHelper1.setUpRecycleView(mRecyclerViewTop);
        mRecyclerViewTop.setLayoutManager(new HorizontalPageFlowLayoutManager());
        mRecyclerViewTop.setAdapter(testAdapter);


        mRecyclerViewMid = findViewById(R.id.mid);
        PagingScrollHelper pagingScrollHelper2 = new PagingScrollHelper();
        pagingScrollHelper2.setUpRecycleView(mRecyclerViewMid);
        mRecyclerViewMid.setLayoutManager(new HorizontalPageFlowLayoutManager(0, 3));
        mRecyclerViewMid.setAdapter(testAdapter);


        mRecyclerViewBottom = findViewById(R.id.bottom);
        PagingScrollHelper pagingScrollHelper3 = new PagingScrollHelper();
        pagingScrollHelper3.setUpRecycleView(mRecyclerViewBottom);
        final HorizontalPageFlowLayoutManager layoutManager = new HorizontalPageFlowLayoutManager(4,4);
        mRecyclerViewBottom.setLayoutManager(layoutManager);
        mRecyclerViewBottom.setAdapter(testAdapter);

    }

    private class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder> {
        @NonNull
        @Override
        public TestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TestHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TestHolder testHolder, int i) {
            String str = "";
            switch (i % 4) {
                case 1:
                    str = "test";
                    break;
                case 2:
                    str = "testHo";
                    break;
                case 3:
                    str = "testHolde";
                    break;
                default:
                    str = "testH";
            }
            testHolder.mText.setText(str + i);
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        class TestHolder extends RecyclerView.ViewHolder {
            TextView mText;

            public TestHolder(@NonNull View itemView) {
                super(itemView);
                mText = itemView.findViewById(R.id.text);
            }
        }
    }
}

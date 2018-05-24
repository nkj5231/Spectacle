package org.androidtown.spectacle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class FragmentTab1 extends Fragment {

    private Context mContext;
    private ArrayList<String> dlistListData = new ArrayList<>();
    private ListView dlistListView;
    //private ArrayAdapter adapter;
    private dlistListViewItemAdapter adapter;
    private DbOpenHelper mDbOpenHelper;
    private View view;

    public static FragmentTab1 newInstance() {
        FragmentTab1 fragment = new FragmentTab1();
        return fragment;
    }

    // getContext가 null을 반환하지 않도록 함
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstBundle) {
        super.onCreate(saveInstBundle);
        setHasOptionsMenu(true);

        //DB Create and Open
        mDbOpenHelper = new DbOpenHelper(getContext());
        mDbOpenHelper.open();

    }

    //플로팅 액션 버튼 이벤트
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        dlistListView = (ListView) view.findViewById(R.id.dlist_view);

        adapter = new dlistListViewItemAdapter();

        // adapter 할당
        dlistListView.setAdapter(adapter);

        String[] category = mDbOpenHelper.getCategory();
        String[] title = mDbOpenHelper.getTitle();
        String[] date = mDbOpenHelper.getDate();

        // adapter를 통한 값 전달
        for (int i = 0; i < category.length; i++) {
            adapter.addItem(category[i], title[i], date[i]);
        }

        // list가 비었을 때 text 설정. 이 위치에서만 작동하니 위치 바꾸지 말 것.
        dlistListView.setEmptyView(view.findViewById(R.id.nonexistent_text));

        // listView 아이템에 대한 터치 이벤트
        dlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String string = (String) parent.getItemAtPosition(position);
            }
        });
        adapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDataActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.make_excel) {

        } else if (id == R.id.initialization) {
            mDbOpenHelper.deleteTable();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }

        return true;
    }

}

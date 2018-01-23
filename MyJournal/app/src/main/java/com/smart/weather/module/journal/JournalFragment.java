package com.smart.weather.module.journal;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smart.weather.R;
import com.smart.weather.base.BaseFragment;
import com.smart.weather.module.journal.adapter.JournalAdapter;
import com.smart.weather.module.write.bean.JournalBeanDBBean;
import com.smart.weather.module.write.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;

    private JournalAdapter journalAdapter;
    private List<JournalBeanDBBean>journalBeans = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Realm realm;

    public JournalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JournalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JournalFragment newInstance() {
        JournalFragment fragment = new JournalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, "");
        args.putString(ARG_PARAM2, "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journal, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        initView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void initView() {
        journalAdapter = new JournalAdapter(R.layout.item_journal,journalBeans);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        journalAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        journalAdapter.setOnItemLongClickListener((adapter, view, position) -> {

            new AlertDialog.Builder(context).setItems(new CharSequence[]{"查看", "删除"}, (dialogInterface, i) -> {

            }).create().show();

            return false;
        });
        recycleView.setAdapter(journalAdapter);
    }

    @Override
    protected void initData() {
        journalBeans.clear();

        journalBeans.addAll(DBHelper.getAllJournals(realm));

        journalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

package com.pg.pgguardkiki.fagment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.adapter.ConstactAdapter;
import com.pg.pgguardkiki.bean.Group;
import com.pg.pgguardkiki.tools.view.TitleBarView;
import com.pg.pgguardkiki.tools.view.TreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzj on 11/9/16.
 */
public class ContactsFagment  extends Fragment {
    private Context mContext;
    private View mBaseView;
    private TitleBarView mTitleBarView;
    private TreeView mTreeView;
    private ConstactAdapter mExpAdapter;
    private List<Group> listGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_contacts, null);
        findView();
        return mBaseView;
    }

    private void findView() {
        mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
        mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitleBarView.setTitleText(R.string.constacts);//标题
        mTitleBarView.setTitleRight("添加");//右按钮-添加好友
        mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(mContext, AddFriendActivity.class);
//                startActivity(intent);
            }
        });
        mTreeView = (TreeView) mBaseView.findViewById(R.id.iphone_tree_view);
        mTreeView.setHeaderView(LayoutInflater.from(mContext).inflate(R.layout.fragment_constact_head_view, mTreeView, false));
        mTreeView.setGroupIndicator(null);
        mTreeView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,int arg3, long arg4) {
//                Intent intent =new Intent(mContext, ChatActivity.class);
//                intent.putExtra("from", listGroup.get(arg2).getChildList().get(arg3).getUsername());
//                startActivity(intent);
                return true;
            }
        });
        initData();
//        if(QQApplication.xmppConnection==null){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    initData();
//                }
//            }, 1000);
//        }else{
//            initData();
//        }
    }

    /**
     * 加载数据
     */
    void initData(){
        findFriends();
        if(listGroup.size()<=0){
            mTreeView.setVisibility(View.GONE);
            return;
        }
        mExpAdapter = new ConstactAdapter(mContext, listGroup, mTreeView);
        mTreeView.setAdapter(mExpAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void findFriends() {
        try {
            listGroup=new ArrayList<Group>();
//            Log.d("111", "===getRoster=====111====aa==");
//            XMPPConnection conn = QQApplication.xmppConnection;
//            Roster roster = conn.getRoster();
//            Log.d("111","===getRoster=====222====aa==");
//            Collection<RosterGroup> groups = roster.getGroups();
//            Log.d("111","===getRoster=====333====aa==");
//            for (RosterGroup group : groups) {
//                Log.d("111","===getRoster=====444====aa==");
//                Group mygroup=new Group();
//                mygroup.setGroupName(group.getName());
//                Collection<RosterEntry> entries = group.getEntries();
//                List<Child> childList=new ArrayList<Child>();
//                for (RosterEntry entry : entries) {
//                    Log.d("111","===getRoster=====555====aa=="+entry.getType().name());
//                    Log.d("111","===getRoster=====555====bb=="+entry.getUser());
//                    Log.d("111","===getRoster=====555====cc=="+entry.getName());
//                    if(entry.getType().name().equals("both")){
//                        Child child=new Child();
//                        child.setUsername(entry.getUser().split("@")[0]);
//                        childList.add(child);
//                    }
//                }
//                mygroup.setChildList(childList);
//                listGroup.add(mygroup);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

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
import com.pg.pgguardkiki.dao.PGDBHelper;
import com.pg.pgguardkiki.dao.PGDBHelperFactory;
import com.pg.pgguardkiki.tools.view.TitleBarView;
import com.pg.pgguardkiki.tools.view.TreeView;
import com.pg.pgguardkiki.bean.Nick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzj on 11/9/16.
 */
public class ContactsFagment  extends Fragment {
    private static final String ClassName = "ContactsFagment";
    private Context mContext;
    private View mBaseView;
    private TreeView mTreeView;
    private ConstactAdapter mExpAdapter;
    private List<Group> listGroup;
    private PGDBHelper mPGDBHelper;
    private String username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_contacts, null);
        mPGDBHelper = PGDBHelperFactory.getDBHelper();
        username= mPGDBHelper.queryUser("PG_User", null, null, null, null);
        findView();
        return mBaseView;
    }

    private void findView() {
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
            String[] columns = {"Roster_Nick"};
            List<Nick> list= mPGDBHelper.query("PG_Roster", columns, "Roster_Username = '" + username + "'", null, Nick.class);
            Log.d(ClassName, "===findFriends=====111====aa=="+list.size());
            for(int i = 0; i < list.size(); i++)
            {
                Log.d(ClassName, "===findFriends=====111====aa=="+list.get(i).getRoster_Nick());
            }

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

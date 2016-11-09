package com.pg.pgguardkiki.adapter;


import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pg.pgguardkiki.bean.Group;
import com.pg.pgguardkiki.tools.view.CircleImageView;
import com.pg.pgguardkiki.tools.view.TreeView.TreeHeaderAdapter;
import com.pg.pgguardkiki.tools.view.TreeView;
import com.pg.pgguardkiki.R;

public class ConstactAdapter extends BaseExpandableListAdapter implements
		TreeHeaderAdapter {

	private Context mContext;
	private List<Group> groupList;
	private TreeView mTreeView;
	private HashMap<Integer, Integer> groupStatusMap;

	@SuppressLint("UseSparseArrays")
	public ConstactAdapter(Context context, List<Group> groupList,TreeView mTreeView) {
		this.mContext = context;
		this.groupList = groupList;
		this.mTreeView = mTreeView;
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	public String getChild(int groupPosition, int childPosition) {
		return groupList.get(groupPosition).getChildList().get(childPosition).getUsername();
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return groupList.get(groupPosition).getChildList().size();
	}

	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	public int getGroupCount() {
		return groupList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	/**
	 * Child
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_constact_child, null);
			holder = new ChildHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.contact_list_item_name);
			holder.feelView = (TextView) convertView.findViewById(R.id.cpntact_list_item_state);
			holder.iconView = (CircleImageView) convertView.findViewById(R.id.circleImageView);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}


		holder.nameView.setText(getChild(groupPosition, childPosition));
		holder.feelView.setText("QQXMPP版...");
		return convertView;
	}

	/**
	 * Group
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		GroupHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_constact_group, null);
			holder = new GroupHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.group_name);
			holder.onLineView = (TextView) convertView.findViewById(R.id.online_count);
			holder.iconView = (ImageView) convertView.findViewById(R.id.group_indicator);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		holder.nameView.setText(groupList.get(groupPosition).getGroupName());
		holder.onLineView.setText(getChildrenCount(groupPosition) + "/"+ getChildrenCount(groupPosition));
		if (isExpanded) {
			holder.iconView.setImageResource(R.mipmap.qb_down);
		} else {
			holder.iconView.setImageResource(R.mipmap.qb_right);
		}
		return convertView;
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			//mSearchView.setVisibility(View.GONE);
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1&& !mTreeView.isGroupExpanded(groupPosition)) {
			//mSearchView.setVisibility(View.VISIBLE);
			return PINNED_HEADER_GONE;
		} else {
			//mSearchView.setVisibility(View.GONE);
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureTreeHeader(View header, int groupPosition,int childPosition, int alpha) {
		((TextView) header.findViewById(R.id.group_name)).setText(groupList.get(groupPosition).getGroupName());//组名
		((TextView) header.findViewById(R.id.online_count)).setText(getChildrenCount(groupPosition) + "/"+ getChildrenCount(groupPosition));//好友上线比例
	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	class GroupHolder {
		TextView nameView;
		TextView onLineView;
		ImageView iconView;
	}

	class ChildHolder {
		TextView nameView;
		TextView feelView;
		CircleImageView iconView;
	}

}

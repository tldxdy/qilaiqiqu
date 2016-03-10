package com.qizhi.qilaiqiqu.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.ChatSingleActivity;
import com.qizhi.qilaiqiqu.activity.MainActivity;
import com.qizhi.qilaiqiqu.adapter.ChatRecordAdapter;
import com.qizhi.qilaiqiqu.model.CertainUserModel;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChatRecordFragment extends Fragment implements OnItemClickListener,OnRefreshListener,OnLoadListener{
	private ListView chatList;
	private View view;
	private List<EMMessage> list;
	private ChatRecordAdapter adapter;
	private Context context;
	private SharedPreferences preferences;
	private int pageIndex = 1;
	
	private RefreshLayout swipeLayout;
	private View header;
	private EMConversation conversation;
	
	private Set<String> chatUserList;
	
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_chat_record,null);
		chatList = (ListView) view.findViewById(R.id.list_fragment_chat_record);
		context = getActivity();
		list = new ArrayList<EMMessage>();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		header = View.inflate(getActivity(),R.layout.header, null);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		chatList.addHeaderView(header);
		
		chatUserList = preferences.getStringSet(preferences.getString("uniqueKey", null), new HashSet<String>());
		System.out.println("chatUserList====>" + chatUserList.size());
		for (String aa : chatUserList) {
			conversation = EMChatManager.getInstance().getConversation(aa);
			if(conversation != null){
				//获取此会话的所有消息
				List<EMMessage> messages = conversation.getAllMessages();
				if(messages.size() > 0 ){
					list.add(messages.get(messages.size() - 1));
				}
			}else{
				List<EMMessage> messages = conversation.loadMoreGroupMsgFromDB(aa, 10);
				System.out.println("12312312aaa"+messages.size());
				if(messages.size() > 0 ){
					list.add(messages.get(messages.size() - 1));
				}
			}
		}
			
		
		
		
		
		//sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
	/*	//获取startMsgId之前的pagesize条消息，此方法获取的messages sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
		List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
		//如果是群聊，调用下面此方法
		List<EMMessage> messages = conversation.loadMoreGroupMsgFromDB(startMsgId, pagesize);*/
		data();
		return view;
	}

	private void data() {
		adapter = new ChatRecordAdapter(context, list);
		chatList.setAdapter(adapter);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		chatList.setOnItemClickListener(this);
	}


	private CertainUserModel certainUserModel;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		System.out.println(position);
		
		try {
		if(list.get(position - 1).getChatType() == ChatType.GroupChat){
			certainUserModel = new CertainUserModel();
			certainUserModel.setImUserName(list.get(position - 1).getUserName());
			certainUserModel.setUserName(list.get(position - 1).getStringAttribute("conversationOtherUserNameExpand"));
			certainUserModel.setUserImage(list.get(position - 1 ).getStringAttribute("conversationOtherUserImageExpand"));
			certainUserModel.setUserId(list.get(position - 1).getIntAttribute("ConversationOtherUserIdentifier"));
			startActivity(new Intent(context, ChatSingleActivity.class).putExtra(
					"certainUserModel", certainUserModel));
		}else{
				/*System.out.println(list.get(position).getIntAttribute("ConversationOtherUserIdentifier")
						+ "-" + list.get(position).getStringAttribute("conversationOtherUserNameExpand")
						+ "-" + list.get(position).getStringAttribute("conversationOtherUserImageExpand")
						+ "-" + list.get(position).getUserName());*/
			certainUserModel = new CertainUserModel();
			certainUserModel.setImUserName(list.get(position - 1).getUserName());
			certainUserModel.setUserName(list.get(position - 1).getStringAttribute("IMUserNameExpand"));
			certainUserModel.setUserImage(list.get(position - 1).getStringAttribute("IMUserImageExpand"));
			certainUserModel.setUserId(list.get(position - 1).getIntAttribute("IMUserIdentifierExpand"));
			startActivity(new Intent(context, ChatSingleActivity.class).putExtra(
					"certainUserModel", certainUserModel));
		}
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setRefreshing(false);
				pageIndex = 1;
					//dataJ();
				
			}
		}, 1500);

	}

	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setLoading(false);
				pageIndex = pageIndex + 1;
				//dataJ();
			}
		}, 1500);
	}

}

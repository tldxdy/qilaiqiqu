package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class ChatActivity extends Activity {

	ArrayList<HashMap<String, Object>> chatList = null;
	String[] from = { "image", "text", "textImg" };
	int[] to = { R.id.chatlist_image_me, R.id.chatlist_text_me,
			R.id.chatlist_img_me, R.id.chatlist_image_other,
			R.id.chatlist_text_other, R.id.chatlist_img_other,
			R.id.chatlist_text_username };
	int[] layout = { R.layout.item_list_chat_me, R.layout.item_list_chat_other };
	String userQQ = null;
	/**
	 * 这里两个布局文件使用了同一个id，测试一下是否管用 TT事实证明这回产生id的匹配异常！所以还是要分开。。
	 * 
	 * userQQ用于接收Intent传递的qq号，进而用来调用数据库中的相关的联系人信息，这里先不讲 先暂时使用一个头像
	 */

	public final static int OTHER = 1;
	public final static int ME = 0;
	private static final boolean String = false;

	protected ListView chatListView = null;
	protected Button chatSendButton = null;
	protected ImageView chatAddPicture = null;
	protected EditText editText = null;

	protected MyChatAdapter adapter = null;

	// /**
	// * 消息送达BroadcastReceiver
	// */
	// private BroadcastReceiver deliveryAckMessageReceiver = new
	// BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// abortBroadcast();
	//
	// String msgid = intent.getStringExtra("msgid");
	// String from = intent.getStringExtra("from");
	// EMConversation conversation = EMChatManager.getInstance()
	// .getConversation(from);
	// if (conversation != null) {
	// // 把message设为已读
	// EMMessage msg = conversation.getMessage(msgid);
	// if (msg != null) {
	// msg.isDelivered = true;
	// }
	// }
	// }
	// };

	private boolean isGroup = true;
	private String username;

	String chat;

	EMMessage messageTXT;
	EMMessage messageVOICE;
	EMMessage messageIMAGE;
	private EMConversation conversation;
	private SharedPreferences preferences;
	private ArrayList<String> stringArrayListExtra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		chatList = new ArrayList<HashMap<String, Object>>();

		// if (chat.equals(getIntent().getStringExtra("Group") == "Group")) {
		// isGroup = true;
		// } else {
		// isGroup = false;
		// }

		// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
		username = getIntent().getStringExtra("username");
		System.out.println(username);
		conversation = EMChatManager.getInstance().getConversation(username);

		// IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(
		// EMChatManager.getInstance()
		// .getDeliveryAckMessageBroadcastAction());
		// deliveryAckMessageIntentFilter.setPriority(5);
		// registerReceiver(deliveryAckMessageReceiver,
		// deliveryAckMessageIntentFilter);

		chatSendButton = (Button) findViewById(R.id.chat_bottom_sendbutton);
		editText = (EditText) findViewById(R.id.chat_bottom_edittext);
		chatListView = (ListView) findViewById(R.id.list_chatActivity_chatList);
		chatAddPicture = (ImageView) findViewById(R.id.img_chatActivity_picture);

		adapter = new MyChatAdapter(this, chatList, layout, from, to);

		chatSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String myWord = null;

				/**
				 * 这是一个发送消息的监听器，注意如果文本框中没有内容，那么getText()的返回值可能为
				 * null，这时调用toString()会有异常！所以这里必须在后面加上一个""隐式转换成String实例
				 * ，并且不能发送空消息。
				 */
				myWord = (editText.getText() + "").toString();
				if (myWord.length() == 0)
					return;
				addTextToList(myWord, ME, null,
						preferences.getString("userImage", null), null);

				updateList();

				// 创建一条文本消息
				messageTXT = EMMessage.createSendMessage(EMMessage.Type.TXT);
				// 如果是群聊，设置chattype,默认是单聊
				messageTXT.setChatType(ChatType.GroupChat);
				// 设置消息body
				TextMessageBody txtBody = new TextMessageBody((editText
						.getText() + "").toString());
				new SystemUtil().makeToast(ChatActivity.this,
						(editText.getText() + "").toString());

				messageTXT.setAttribute("IMUserIdentifierExpand", username);
				messageTXT.setAttribute("IMUserNameExpand",
						preferences.getString("userName", null));
				messageTXT.setAttribute("IMUserImageExpand",
						preferences.getString("userImage", null));

				System.out.println("ChatActivity-----Attribute:"
						+ preferences.getString("userName", null) + "......."
						+ preferences.getString("userImage", null));

				// 清空输入框
				editText.setText("");

				messageTXT.addBody(txtBody);
				// 设置接收人
				messageTXT.setReceipt(username);
				// 把消息加入到此会话对象中
				conversation.addMessage(messageTXT);
				// 发送消息
				EMChatManager.getInstance().sendMessage(messageTXT,
						new EMCallBack() {

							@Override
							public void onError(int arg0, String arg1) {
								// new SystemUtil().makeToast(ChatActivity.this,
								// "发送失败:" + arg0 + "," + arg1);
								System.out.println("发送失败:" + arg0 + "," + arg1);
							}

							@Override
							public void onProgress(int arg0, String arg1) {

							}

							@Override
							public void onSuccess() {
								// new SystemUtil().makeToast(ChatActivity.this,
								// "发送成功");
								System.out.println("发送成功");
							}
						});
			}

		});

		chatAddPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(ChatActivity.this,
						NativeImagesActivity.class).putExtra("falg", true), 1);

			}
		});

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() > 1) {
					chatSendButton.setVisibility(View.VISIBLE);
					chatAddPicture.setVisibility(View.GONE);
				} else {
					chatSendButton.setVisibility(View.GONE);
					chatAddPicture.setVisibility(View.VISIBLE);
				}
			}
		});

		chatListView.setAdapter(adapter);
		getChatHistory();
		addListener();
	}

	/**
	 * 更新数据列表，并且通过setSelection方法使ListView始终滚动在最底端
	 */
	private void updateList() {
		adapter.notifyDataSetChanged();
		chatListView.setSelection(chatList.size() - 1);
		chatListView.smoothScrollToPosition(chatListView.getCount() - 1);
	}

	private void getChatHistory() {
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(username);
		// 获取此会话的所有消息
		List<EMMessage> message = conversation.getAllMessages();

		// List<EMMessage> messages =
		// conversation.loadMoreGroupMsgFromDB(message
		// .get(message.size() - 1).toString(), message.size() - 1);

		System.out.println(message + "!!!!" + message.size());

		ImageMessageBody imageMessageBody = new ImageMessageBody();
		
		for (int i = 0; i < message.size(); i++) {

			if (message.get(i).getFrom()
					.equals(preferences.getString("imUserName", null))) {
				String na = message.get(i).getBody().toString().split(":")[1];
				String substring = na.substring(1, na.length() - 1);
				addTextToList(substring, ME, message.get(i).getFrom(),
						preferences.getString("userImage", null), null);
				
			} else {
				String na = message.get(i).getBody().toString().split(":")[1];
				String substring = na.substring(1, na.length() - 1);
				addTextToList(
						substring,
						OTHER,
						message.get(i).getStringAttribute("IMUserNameExpand",
								null),
						message.get(i).getStringAttribute("IMUserImageExpand",
								"null"), null);
			}
		}

		if (message != null) {
			// System.out.println(messages.get(0).getFrom() + "++++"
			// + messages.get(0).getBody().toString());
		}

		// sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
		// 获取startMsgId之前的pagesize条消息，此方法获取的messages
		// sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
		// List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId,
		// pagesize);
		// 如果是群聊，调用下面此方法
		// List<EMMessage> messages =
		// conversation.loadMoreGroupMsgFromDB(startMsgId, 1);
	}

	public void addListener() {
		EMChatManager.getInstance().registerEventListener(
				new EMEventListener() {

					@Override
					public void onEvent(EMNotifierEvent event) {
						EMMessage message = (EMMessage) event.getData();
						System.out.println("ChatActivity-----MESSAGE:"
								+ message);

						String na = message.getBody().toString().split(":")[1];
						String substring = na.substring(1, na.length() - 1);

						addTextToList(substring, OTHER, message
								.getStringAttribute("IMUserNameExpand", null),
								message.getStringAttribute("IMUserImageExpand",
										null), null);
						if("".equals(message.getFrom())){
							
						}
						
						updateList();
						System.out.println("ChatActivity-----MESSAGE:"
								+ message.getStringAttribute(
										"IMUserNameExpand", null)
								+ "......."
								+ message.getStringAttribute(
										"IMUserImageExpand", null));
					}
				});

	}

	protected void addTextToList(String text, int who, String name,
			String imgPath, String textImgPath) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("person", who);
		map.put("image",
				who == ME ? "http://weride.oss-cn-hangzhou.aliyuncs.com/"
						+ imgPath
						: "http://weride.oss-cn-hangzhou.aliyuncs.com/"
								+ imgPath);
		map.put("text", text);
		map.put("name", name);
		map.put("textImg", textImgPath);
		chatList.add(map);
	}

	private class MyChatAdapter extends BaseAdapter {

		Context context = null;
		ArrayList<HashMap<String, Object>> chatList = null;
		int[] layout;
		String[] from;
		int[] to;

		public MyChatAdapter(Context context,
				ArrayList<HashMap<String, Object>> chatList, int[] layout,
				String[] from, int[] to) {
			super();
			this.context = context;
			this.chatList = chatList;
			this.layout = layout;
			this.from = from;
			this.to = to;
		}

		@Override
		public int getCount() {
			return chatList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public ImageView imageView = null;
			public TextView textView = null;
			public TextView nameView = null;
			public ImageView textImgView = null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int who = (Integer) chatList.get(position).get("person");

			convertView = LayoutInflater.from(context).inflate(
					layout[who == ME ? 0 : 1], null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(to[who * 3 + 0]);
			holder.textView = (TextView) convertView
					.findViewById(to[who * 3 + 1]);
			holder.textImgView = (ImageView) convertView
					.findViewById(to[who * 3 + 2]);
			if (who == 1) {
				holder.nameView = (TextView) convertView.findViewById(to[6]);
				holder.nameView.setText(chatList.get(position).get("name")
						.toString()
						+ ":");

			}
			Picasso.with(context)
					.load((chatList.get(position).get(from[0])).toString())
					.into(holder.imageView);
			
			if (chatList.get(position).get(from[2]) != null) {
				Picasso.with(context)
				.load((chatList.get(position).get(from[2])).toString())
				.into(holder.textImgView);
			}else {
				holder.textView.setText(chatList.get(position).get(from[1])
						.toString());
			}
			
			return convertView;
		}
	}

	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case 1:
				stringArrayListExtra = data
						.getStringArrayListExtra("photoList");

				for (int i = 0; i < stringArrayListExtra.size(); i++) {

					System.out.println(new File(stringArrayListExtra.get(i)));
					// ImageMessageBody body = new ImageMessageBody(new
					// File(stringArrayListExtra.get(i)));

					EMConversation conversation = EMChatManager.getInstance()
							.getConversation(username);
					EMMessage message = EMMessage
							.createSendMessage(EMMessage.Type.IMAGE);
					
					// 如果是群聊，设置chattype,默认是单聊
					message.setChatType(ChatType.GroupChat);

					ImageMessageBody body = new ImageMessageBody(new File(
							stringArrayListExtra.get(i)));
					// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
					// body.setSendOriginalImage(true);
					message.addBody(body);
					message.setReceipt(username);
					conversation.addMessage(message);

					EMChatManager.getInstance().sendMessage(message,
							new EMCallBack() {

								@Override
								public void onError(int arg0,
										java.lang.String arg1) {
									System.out.println("图片发送失败:" + arg0 + ","
											+ arg1);
								}

								@Override
								public void onProgress(int arg0,
										java.lang.String arg1) {

								}

								@Override
								public void onSuccess() {
									System.out.println("图片发送成功");
								}
							});

				}

				break;

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {

		EMChatManager.getInstance().unregisterEventListener(
				new EMEventListener() {

					@Override
					public void onEvent(EMNotifierEvent event) {
						System.out.println("解除监听");
					}
				});

		super.onPause();
	}

}

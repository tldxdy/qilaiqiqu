package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.util.VoiceRecorder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.CommonUtils;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.VoicePlayClickListener;
import com.squareup.picasso.Picasso;

public class ChatSingleActivity extends HuanxinLogOutActivity {

	ArrayList<HashMap<String, Object>> chatList = null;
	String[] from = { "image", "text", "textImg" };
	int[] to = { R.id.chatlist_image_me, R.id.chatlist_text_me,
			R.id.chatlist_img_me, R.id.chatlist_voice_me,
			R.id.chatlist_voiceLength_me, R.id.chatlist_voiceCoentent_me,
			R.id.chatlist_unread_me, R.id.chatlist_image_other,
			R.id.chatlist_text_other, R.id.chatlist_img_other,
			R.id.chatlist_voice_other, R.id.chatlist_voiceLength_other,
			R.id.chatlist_voiceCoentent_other, R.id.chatlist_unread_other,
			R.id.chatlist_text_username };
	int[] layout = { R.layout.item_list_chat_me, R.layout.item_list_chat_other };
	String userQQ = null;

	public final static int OTHER = 1;
	public final static int ME = 0;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;

	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;

	private int chatType;
	private VoiceRecorder voiceRecorder;

	public String playMsgId;

	String myUserNick = "";
	String myUserAvatar = "";

	protected ListView chatListView = null;
	protected Button chatSendButton = null;
	protected ImageView chatAddPicture = null;

	protected EditText edtContent = null;
	protected TextView txtVoice = null;

	protected MyChatAdapter adapter = null;

	private String username;

	String chat;

	EMMessage messageTXT;
	EMMessage messageVOICE;
	EMMessage messageIMAGE;

	MediaPlayer mediaPlayer = null;

	private EMConversation conversation;
	private SharedPreferences preferences;
	private ArrayList<String> stringArrayListExtra;

	private LinearLayout backLayout;

	private TextView txtTitle;

	private ImageView imgVoice;
	boolean isText = true;

	private NewMessageBroadcastReceiver receiver;

	private Drawable[] micImages;
	
	public Set<String> chatUserList;
	private Gson gson;
	private Type type;

	@SuppressLint("HandlerLeak")
	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	private String otherUserName;
	private String otherUserImage;
	private String otherUserId;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat_single);

		// 注册接收消息广播
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(5);
		registerReceiver(receiver, intentFilter);

		chatList = new ArrayList<HashMap<String, Object>>();

		// if (chat.equals(getIntent().getStringExtra("Group") == "Group")) {
		// isGroup = true;
		// } else {
		// isGroup = false;
		// }

		// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此

		username = getIntent().getStringExtra("username");
		otherUserName = getIntent().getStringExtra("otherUserName");
		otherUserImage = getIntent().getStringExtra("otherUserImage");
		otherUserId = getIntent().getIntExtra("otherUserId", -1) +"";

		conversation = EMChatManager.getInstance().getConversation(username);

		imgVoice = (ImageView) findViewById(R.id.img_chatActivity_voice);
		backLayout = (LinearLayout) findViewById(R.id.layout_ChatActivity_back);
		chatSendButton = (Button) findViewById(R.id.chat_bottom_sendbutton);

		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);

		voiceRecorder = new VoiceRecorder(micImageHandler);

		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");

		// 动画资源文件,用于录制语音时
		micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };

		txtVoice = (TextView) findViewById(R.id.txt_chatActivity_voice);
		edtContent = (EditText) findViewById(R.id.edt_chatActivity_content);

		chatListView = (ListView) findViewById(R.id.list_chatActivity_chatList);
		chatAddPicture = (ImageView) findViewById(R.id.img_chatActivity_picture);

		txtTitle = (TextView) findViewById(R.id.txt_chatActivity_title);

		adapter = new MyChatAdapter(this, chatList, layout, from, to);

		txtTitle.setText(otherUserName);

		imgVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isText) {
					edtContent.setVisibility(View.GONE);
					txtVoice.setVisibility(View.VISIBLE);
					isText = false;
				} else {
					txtVoice.setVisibility(View.GONE);
					edtContent.setVisibility(View.VISIBLE);
					isText = true;
				}
			}
		});

		txtVoice.setOnTouchListener(new PressToSpeakListen());

		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		chatSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String myWord = null;

				/**
				 * 这是一个发送消息的监听器，注意如果文本框中没有内容，那么getText()的返回值可能为
				 * null，这时调用toString()会有异常！所以这里必须在后面加上一个""隐式转换成String实例
				 * ，并且不能发送空消息。
				 */
				myWord = (edtContent.getText() + "").toString();
				if (myWord.length() == 0)
					return;
				addTextToList("TXT", myWord, ME, "",
						preferences.getString("userImage", null), "", "", "",
						messageTXT);

				updateList();

				// 创建一条文本消息
				messageTXT = EMMessage.createSendMessage(EMMessage.Type.TXT);
				// 设置消息body
				TextMessageBody txtBody = new TextMessageBody((edtContent
						.getText() + "").toString());
				// 拓展中的User ID 如果是群组,则为对应的群组ID
				messageTXT.setAttribute("ConversationMyUserIdentifier",
						preferences.getInt("userId", -1)+"");
				// 拓展中的用户头像
				messageTXT.setAttribute("IMUserImageExpand",
						preferences.getString("userImage", null));
				// 拓展中的用户名
				messageTXT.setAttribute("IMUserNameExpand",
						preferences.getString("userName", null));
				// 拓展中的对方用户头像
				messageTXT.setAttribute("conversationOtherUserNameExpand",
						otherUserName);
				// 拓展中的对方用户名
				messageTXT.setAttribute("conversationOtherUserImageExpand",
						otherUserImage);
				// 拓展中的对方用户ID
				messageTXT.setAttribute("ConversationOtherUserIdentifier",
						otherUserId);

				// IMConversationUserImageExpand
				// IMConversationUserNameExpand 群组名称 私聊

				// 清空输入框
				edtContent.setText("");

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
				startActivityForResult(new Intent(ChatSingleActivity.this,
						NativeImagesActivity.class).putExtra("falg", true), 1);

			}
		});

		edtContent.addTextChangedListener(new TextWatcher() {

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
				if (e.length() > 0) {
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
		// addListener();

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
		chatUserList = new HashSet<String>();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		gson = new Gson();
		type = new TypeToken<HashSet<String>>() {
		}.getType();
		String chat = preferences.getString(
				"Chat" + preferences.getString("uniqueKey", null), null);
		if (chat != null) {
			chatUserList = gson.fromJson(chat, type);
		}
		
		
		
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(username);
		// 获取此会话的所有消息
		List<EMMessage> messageList = conversation.getAllMessages();
		// List<EMMessage> messages =
		// conversation.loadMoreGroupMsgFromDB(message
		// .get(message.size() - 1).toString(), message.size() - 1);

		// List<EMMessage> messages =
		// conversation.loadMoreGroupMsgFromDB(messageList.get(messageList.size()-1).getBody().toString(),
		// 20);

		for (int i = 0; i < messageList.size(); i++) {

			if (messageList.get(i).getFrom()
					.equals(preferences.getString("imUserName", null))) {

				if (messageList.get(i).getType().toString().equals("TXT")) {
					String na = messageList.get(i).getBody().toString()
							.split(":")[1];
					String substring = na.substring(1, na.length() - 1);
					addTextToList(messageList.get(i).getType().toString(),
							substring, ME,
							preferences.getString("userName", null),
							preferences.getString("userImage", null), "", "",
							"", messageList.get(i));
				} else if (messageList.get(i).getType().toString()
						.equals("IMAGE")) {
					ImageMessageBody imgBody = (ImageMessageBody) messageList
							.get(i).getBody();
					String localPath = imgBody.getLocalUrl();

					addTextToList(messageList.get(i).getType().toString(), "",
							ME, preferences.getString("userName", null),
							preferences.getString("userImage", null),
							localPath, "", "", messageList.get(i));
				} else {
					VoiceMessageBody vioceBody = (VoiceMessageBody) messageList
							.get(i).getBody();
					addTextToList(messageList.get(i).getType().toString(), "",
							ME, preferences.getString("userName", null),
							preferences.getString("userImage", null), "",
							vioceBody.getLocalUrl(),
							vioceBody.getLength() + "", messageList.get(i));
				}

			} else {
				if (messageList.get(i).getType().toString().equals("TXT")) {
					String na = messageList.get(i).getBody().toString()
							.split(":")[1];
					String substring = na.substring(1, na.length() - 1);
					addTextToList(
							messageList.get(i).getType().toString(),
							substring,
							OTHER,
							messageList.get(i).getStringAttribute(
									"IMUserNameExpand", null),
							messageList.get(i).getStringAttribute(
									"IMUserImageExpand", null), "", "", "",
							messageList.get(i));
				} else if (messageList.get(i).getType().toString()
						.equals("IMAGE")) {
					ImageMessageBody imgBody = (ImageMessageBody) messageList
							.get(i).getBody();
					String thumbRemoteUrl = imgBody.getThumbnailUrl();

					addTextToList(
							messageList.get(i).getType().toString(),
							"",
							OTHER,
							messageList.get(i).getStringAttribute(
									"IMUserNameExpand", null),
							messageList.get(i).getStringAttribute(
									"IMUserImageExpand", null), thumbRemoteUrl,
							"", "", messageList.get(i));
				} else {

					VoiceMessageBody vioceBody = (VoiceMessageBody) messageList
							.get(i).getBody();
					addTextToList(
							messageList.get(i).getType().toString(),
							"",
							OTHER,
							messageList.get(i).getStringAttribute(
									"IMUserNameExpand", null),
							messageList.get(i).getStringAttribute(
									"IMUserImageExpand", null), "",
							vioceBody.getLocalUrl(),
							vioceBody.getLength() + "", messageList.get(i));
				}

			}
		}

		// sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
		// 获取startMsgId之前的pagesize条消息，此方法获取的messages
		// sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
		// List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId,
		// pagesize);
		// 如果是群聊，调用下面此方法
		// List<EMMessage> messages =
		// conversation.loadMoreGroupMsgFromDB(startMsgId, 1);

		chatListView.setSelection(chatListView.getCount() - 1);
	}

	/**
	 * 消息广播接收者
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 记得把广播给终结掉
			abortBroadcast();

			String from = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);
			
				chatUserList.add(message.getFrom());
			String chat = gson.toJson(chatUserList);
			SharedPreferences sharedPreferences = getSharedPreferences(
					"userLogin", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();// 获取编辑器
			editor.putString(
					"Chat" + sharedPreferences.getString("uniqueKey", null),
					chat);
			editor.commit();
			
			
			

			if (from.equals(username)) {

				if (message.getType().toString().equals("TXT")) {
					String na = message.getBody().toString().split(":")[1];
					String substring = na.substring(1, na.length() - 1);
					addTextToList(message.getType().toString(), substring,
							OTHER, message.getStringAttribute(
									"IMUserNameExpand", null),
							message.getStringAttribute("IMUserImageExpand",
									null), "", "", "", message);

				} else if (message.getType().toString().equals("IMAGE")) {
					ImageMessageBody imgBody = (ImageMessageBody) message
							.getBody();
					String thumbRemoteUrl = imgBody.getThumbnailUrl();

					addTextToList(
							message.getType().toString(),
							"",
							OTHER,
							message.getStringAttribute("IMUserNameExpand", null),
							message.getStringAttribute("IMUserImageExpand",
									null), thumbRemoteUrl, "", "", message);
				} else {
					VoiceMessageBody vioceBody = (VoiceMessageBody) message
							.getBody();
					addTextToList(
							message.getType().toString(),
							"",
							OTHER,
							message.getStringAttribute("IMUserNameExpand", null),
							message.getStringAttribute("IMUserImageExpand",
									null), "", vioceBody.getLocalUrl(),
							vioceBody.getLength() + "", message);

				}
			}

			// 通知adapter有新消息，更新ui
			adapter.refresh();
			chatListView.setSelection(chatListView.getCount() - 1);

			if (!from.equals(username)) {
				// 消息不是发给当前会话，return
				notifyNewMessage(message);
				return;
			}

		}
	}

	protected void addTextToList(String type, String text, int who,
			String name, String imgPath, String textImgPath, String voicePath,
			String voiceLength, EMMessage message) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("person", who);
		map.put("image", who == ME ? SystemUtil.IMGPHTH + imgPath
				: SystemUtil.IMGPHTH + imgPath);
		map.put("text", text);
		map.put("name", name);
		map.put("textImg", textImgPath);
		map.put("voicePath", voicePath);
		map.put("voiceLength", voiceLength);
		map.put("message", message);

		chatList.add(map);
	}

	private class MyChatAdapter extends BaseAdapter {

		Context context = null;
		ArrayList<HashMap<String, Object>> chatList = null;
		int[] layout;
		String[] from;
		int[] to;
		private ViewHolder holder;

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
		public Object getItem(int position) {
			return conversation.getMessage(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public ImageView imageView = null;
			public TextView nameView = null;
			public TextView textView = null;
			public ImageView textImgView = null;

			public LinearLayout voiceLayout = null;
			public TextView voicelength = null;
			public ImageView voiceImg = null;
			public ImageView isRead = null;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int who = (Integer) chatList.get(position).get("person");
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					layout[who == ME ? 0 : 1], null);
			holder.imageView = (ImageView) convertView
					.findViewById(to[who * 7 + 0]);
			holder.textView = (TextView) convertView
					.findViewById(to[who * 7 + 1]);
			holder.textImgView = (ImageView) convertView
					.findViewById(to[who * 7 + 2]);
			holder.voiceLayout = (LinearLayout) convertView
					.findViewById(to[who * 7 + 3]);
			holder.voicelength = (TextView) convertView
					.findViewById(to[who * 7 + 4]);
			holder.voiceImg = (ImageView) convertView
					.findViewById(to[who * 7 + 5]);
			holder.isRead = (ImageView) convertView
					.findViewById(to[who * 7 + 6]);
			if (who == 1) {
				holder.nameView = (TextView) convertView.findViewById(to[14]);
				holder.nameView.setText(chatList.get(position).get("name")
						.toString()
						+ ":");
			}
			convertView.setTag(holder);

			SystemUtil.Imagexutils((chatList.get(position).get(from[0])).toString(),holder.imageView, context);
			/*Picasso.with(context)
					.load((chatList.get(position).get(from[0])).toString())
					.into(holder.imageView);*/

			if (chatList.get(position).get("type").toString().equals("TXT")) {
				holder.textView.setVisibility(View.VISIBLE);
				holder.voiceLayout.setVisibility(View.GONE);
				holder.textImgView.setVisibility(View.GONE);

				holder.textView.setText(chatList.get(position).get(from[1])
						.toString());

			} else if (chatList.get(position).get("type").toString()
					.equals("IMAGE")) {
				holder.textView.setVisibility(View.GONE);
				holder.voiceLayout.setVisibility(View.GONE);
				holder.textImgView.setVisibility(View.VISIBLE);

				if (who == OTHER) {
					SystemUtil.Imagexutils((chatList.get(position).get(from[2]).toString()),holder.imageView, context);
					/*c.with(context)
							.load((chatList.get(position).get(from[2]))
									.toString()).into(holder.textImgView);*/
				} else {
					File file = new File(
							(chatList.get(position).get(from[2])).toString());
					Picasso.with(context).load(file).into(holder.textImgView);
				}

			} else {
				holder.textView.setVisibility(View.GONE);
				holder.textImgView.setVisibility(View.GONE);
				holder.voiceLayout.setVisibility(View.VISIBLE);
				holder.isRead.setVisibility(View.GONE);

				holder.voicelength.setText(chatList.get(position)
						.get("voiceLength").toString()
						+ "\"");

				holder.voiceLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						holder.isRead.setVisibility(View.GONE);
						playVoice(chatList.get(position).get("voicePath")
								.toString(), (EMMessage) chatList.get(position)
								.get("message"), holder.voiceImg,
								holder.isRead, position);
					}
				});
			}

			return convertView;
		}

		/**
		 * 刷新页面
		 */
		public void refresh() {
			notifyDataSetChanged();
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

					// System.out.println(new
					// File(stringArrayListExtra.get(i)));
					// ImageMessageBody body = new ImageMessageBody(new
					// File(stringArrayListExtra.get(i)));

					EMConversation conversation = EMChatManager.getInstance()
							.getConversation(username);
					messageIMAGE = EMMessage
							.createSendMessage(EMMessage.Type.IMAGE);

					final ImageMessageBody body = new ImageMessageBody(
							new File(stringArrayListExtra.get(i)));

					// 拓展中的User ID 如果是群组,则为对应的群组ID
					messageIMAGE.setAttribute("ConversationMyUserIdentifier",
							preferences.getInt("userId", -1)+"");
					// 拓展中的用户头像
					messageIMAGE.setAttribute("IMUserImageExpand",
							preferences.getString("userImage", null));
					// 拓展中的用户名
					messageIMAGE.setAttribute("IMUserNameExpand",
							preferences.getString("userName", null));
					// 拓展中的对方用户头像
					messageIMAGE.setAttribute(
							"conversationOtherUserNameExpand", otherUserName);
					// 拓展中的对方用户名
					messageIMAGE.setAttribute(
							"conversationOtherUserImageExpand", otherUserImage);
					// 拓展中的对方用户ID
					messageIMAGE.setAttribute(
							"ConversationOtherUserIdentifier", otherUserId);

					// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
					// body.setSendOriginalImage(true);
					messageIMAGE.addBody(body);
					messageIMAGE.setReceipt(username);
					conversation.addMessage(messageIMAGE);

					EMChatManager.getInstance().sendMessage(messageIMAGE,
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

									addTextToList("IMAGE", "", ME, "",
											preferences.getString("userImage",
													null), body.getLocalUrl(),
											"", "", messageIMAGE);
									adapter.refresh();
									chatListView.setSelection(chatListView
											.getCount() - 1);
								}
							});

				}

				break;

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private PowerManager.WakeLock wakeLock;

	/**
	 * 按住说话listener
	 * 
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@SuppressLint({ "ClickableViewAccessibility", "Wakelock" })
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					Toasts.show(ChatSingleActivity.this, "发送语音需要sdcard支持！", 0);
					/*Toast.makeText(ChatSingleActivity.this, "发送语音需要sdcard支持！",
							Toast.LENGTH_SHORT).show();*/
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener
								.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, username,
							getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					/*Toast.makeText(ChatSingleActivity.this,
							R.string.recoding_fail, Toast.LENGTH_SHORT).show();*/
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint
							.setText(getString(R.string.release_to_cancel));
					recordingHint
							.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(),
									voiceRecorder.getVoiceFileName(username),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							Toasts.show(getApplicationContext(), "无录音权限", 0);
						} else {
							Toasts.show(getApplicationContext(), "录音时间太短", 0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toasts.show(getApplicationContext(), "发送失败，请检测服务器是否连接", 0);
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 发送语音
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length,
			boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage messageVOICE = EMMessage
					.createSendMessage(EMMessage.Type.VOICE);

			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP)
				messageVOICE.setReceipt(username);

			// 拓展中的User ID 如果是群组,则为对应的群组ID
			messageVOICE.setAttribute("ConversationMyUserIdentifier",
					preferences.getInt("userId", -1)+"");
			// 拓展中的用户头像
			messageVOICE.setAttribute("IMUserImageExpand",
					preferences.getString("userImage", null));
			// 拓展中的用户名
			messageVOICE.setAttribute("IMUserNameExpand",
					preferences.getString("userName", null));
			// 拓展中的对方用户头像
			messageVOICE.setAttribute("conversationOtherUserNameExpand",
					otherUserName);
			// 拓展中的对方用户名
			messageVOICE.setAttribute("conversationOtherUserImageExpand",
					otherUserImage);
			// 拓展中的对方用户ID
			messageVOICE.setAttribute("ConversationOtherUserIdentifier",
					otherUserId);

			int len = Integer.parseInt(length);
			final VoiceMessageBody body = new VoiceMessageBody(new File(
					filePath), len);
			messageVOICE.addBody(body);

			conversation.addMessage(messageVOICE);

			EMChatManager.getInstance().sendMessage(messageVOICE,
					new EMCallBack() {

						@Override
						public void onError(int arg0, String arg1) {
							System.out.println("语音发送失败,code:" + arg0 + ",msg:"
									+ arg1);
						}

						@Override
						public void onProgress(int arg0, String arg1) {

						}

						@Override
						public void onSuccess() {
							System.out.println("语音发送成功,body:" + body);

							addTextToList("VOICE", "", ME, "",
									preferences.getString("userImage", null),
									"", body.getLocalUrl(), body.getLength()
											+ "", messageVOICE);

							adapter.refresh();
							chatListView.setSelection(chatListView.getCount() - 1);
							setResult(RESULT_OK);
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 播放语音
	 * 
	 * @param filePath
	 */
	private ChatType msgType;
	public static ChatSingleActivity currentPlayListener = null;

	public void playVoice(String filePath, final EMMessage message,
			final ImageView v, ImageView isread, int position) {
		if (!(new File(filePath).exists())) {
			return;
		}
		playMsgId = message.getMsgId();
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		if (EMChatManager.getInstance().getChatOptions().getUseSpeaker()) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		}
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							mediaPlayer.release();
							mediaPlayer = null;
							stopPlayVoice(message, v); // stop animation
						}

					});
			isPlaying = true;
			currentPlayListener = this;
			mediaPlayer.start();
			showAnimation(message, v, position);

			// 如果是接收的消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				try {
					if (!message.isAcked) {
						message.isAcked = true;
						// 告知对方已读这条消息
						msgType = message.getChatType();
						if (msgType != ChatType.GroupChat)
							EMChatManager.getInstance().ackMessageRead(
									message.getFrom(), message.getMsgId());
					}
				} catch (Exception e) {
					message.isAcked = false;
				}
				if (!message.isListened() && isread != null
						&& isread.getVisibility() == View.VISIBLE) {
					// 隐藏自己未播放这条语音消息的标志
					isread.setVisibility(View.INVISIBLE);
					EMChatManager.getInstance().setMessageListened(message);
				}

			}

		} catch (Exception e) {
		}
	}

	// show the voice playing animation
	private void showAnimation(EMMessage message, ImageView v, int position) {
		// play voice, and start animation
		if ((Integer) chatList.get(position).get("person") == OTHER) {
			v.setImageResource(R.anim.voice_from_icon);
		} else {
			v.setImageResource(R.anim.voice_to_icon);
		}
		voiceAnimation = (AnimationDrawable) v.getDrawable();
		voiceAnimation.start();
	}

	private AnimationDrawable voiceAnimation = null;
	public static boolean isPlaying = false;

	public void stopPlayVoice(EMMessage message, ImageView v) {
		voiceAnimation.stop();
		if (message.direct == EMMessage.Direct.RECEIVE) {
			v.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			v.setImageResource(R.drawable.chatto_voice_playing);
		}
		// stop play voice
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
		playMsgId = null;
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

}

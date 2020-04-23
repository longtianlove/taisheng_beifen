package com.taisheng.now.view.biaoqing;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.liji.imagezoom.util.ImageZoom;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.bean.result.PictureBean;
import com.taisheng.now.bussiness.me.SelectAvatarSourceDialog;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.chat.ColorUtils;
import com.taisheng.now.chat.ImageLookActivity;
import com.taisheng.now.chat.MLOC;
import com.taisheng.now.chat.MessageBean;
import com.taisheng.now.chat.RemoteChatMessage;
import com.taisheng.now.chat.websocket.WebSocketManager;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.yuyin.manager.MediaManager;
import com.taisheng.now.yuyin.util.CommonsUtils;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * Created by SiberiaDante
 * Describe:
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */
public class Watch_EmotionMainFragment extends BaseFragment implements AdapterView.OnItemLongClickListener {
    public static String mTargetId = "-1";
    public static String doctorName;
    public static String doctorAvator;


    private static final String TAG = Watch_EmotionMainFragment.class.getSimpleName();
    private CheckBox mCBEmotionBtn;
    private EditText vEditText;
    private Button vSendBtn;
    private NoHorizontalScrollerViewPager mNoHorizontalVP;
    //底部水平tab
    private RecyclerView mBottomRecyclerView;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG = "CURRENT_POSITION_FLAG";
    private int CurrentPosition = 0;
    private List<Fragment> fragments = new ArrayList<>();


    private ListView vMsgList;
    private View contentView;
    View iv_sendimg;
    private Watch_EmotionKeyboard mEmotionKeyboard;
    private GlobalOnItemClickManagerUtils globalOnItemClickManager;


    private View ll_emotion_layout;
    private List<MessageBean> mDatas;

    private com.taisheng.now.yuyin.manager.AudioRecordButton yuyin_text;
    private View bar_edit_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_watch_emotion_main, container, false);
        initView(layout);
        ll_emotion_layout = layout.findViewById(R.id.ll_emotion_layout);
        yuyin_text = layout.findViewById(R.id.yuyin_text);
        bar_edit_text = layout.findViewById(R.id.bar_edit_text);
        //初始化EmotionKeyboard
        mEmotionKeyboard = Watch_EmotionKeyboard.with(getActivity())
                .setEmotionView(layout.findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(((EditText) layout.findViewById(R.id.bar_edit_text)))//判断绑定那种EditView
                .bindToEmotionButton(layout.findViewById(R.id.emotion_button))//绑定表情按钮
                .bindToYuyinButton(layout.findViewById(R.id.yuyin_button), yuyin_text, bar_edit_text)
                .build();
        Watch_EmotionKeyboard.yuyinButtonisCheck = false;
        yuyin_text.setVisibility(View.GONE);
        bar_edit_text.setVisibility(View.VISIBLE);
        initData();
        initListener();

//        点击表情的全局监听管理类
        globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance();
        //绑定EditText
        globalOnItemClickManager.attachToEditText(vEditText);
        EventBus.getDefault().register(this);
        return layout;
    }

    private void initListener() {
        yuyin_text.setHasRecordPromission(true);
        yuyin_text.setAudioFinishRecorderListener((seconds, filePath) -> {
//            Record recordModel = new Record();
//            recordModel.setSecond((int) seconds <= 0 ? 1 : (int) seconds);
//            recordModel.setPath(filePath);
//            recordModel.setPlayed(false);
            String rawAudiomessage = "audio[";
            rawAudiomessage += ((seconds <= 0 ? 1 : (int) seconds) + "," + filePath + "," + "1]");
            sendYuyinMsg(rawAudiomessage);
            uploadYuyin(filePath);
//
//            //添加到数据库
//            mgr.add(recordModel);
        });
    }

    public void uploadYuyin(String path) {
        try {

            File fYuyin = new File(path);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fYuyin);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", fYuyin.getName(), requestFile);
            ApiUtils.getApiService().microcharVoice(UserInstance.getInstance().getUid(), WatchInstance.getInstance().deviceId, body).enqueue(new TaiShengCallback<BaseBean<PictureBean>>() {

                                                                                                                                                 @Override
                                                                                                                                                 public void onSuccess(Response<BaseBean<PictureBean>> response, BaseBean<PictureBean> message) {
                                                                                                                                                     switch (message.code) {
                                                                                                                                                         case Constants.HTTP_SUCCESS:

                                                                                                                                                             break;
//                                                                              case 4031:
                                                                                                                                                         default:
                                                                                                                                                             MessageBean messageBean = mDatas.get(mDatas.size() - 1);
                                                                                                                                                             int length = messageBean.msg.length();
                                                                                                                                                             messageBean.msg = messageBean.msg.substring(0, length - 2);
                                                                                                                                                             messageBean.msg += "0]";
                                                                                                                                                             MLOC.updateMessage(messageBean);
                                                                                                                                                             mAdapter.notifyDataSetChanged();
                                                                                                                                                             break;


                                                                                                                                                     }


                                                                                                                                                 }

                                                                                                                                                 @Override
                                                                                                                                                 public void onFail(Call<BaseBean<PictureBean>> call, Throwable t) {
                                                                                                                                                     MessageBean messageBean = mDatas.get(mDatas.size() - 1);
                                                                                                                                                     int length = messageBean.msg.length();
                                                                                                                                                     messageBean.msg = messageBean.msg.substring(0, length - 2);
                                                                                                                                                     messageBean.msg += "0]";
                                                                                                                                                     MLOC.updateMessage(messageBean);
                                                                                                                                                     mAdapter.notifyDataSetChanged();
                                                                                                                                                 }
                                                                                                                                             }
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void yicangbiaoqingban() {
        ll_emotion_layout.setVisibility(View.VISIBLE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void uploadImageSuccess(EventManage.uploadChatPictureSuccess event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri source = FileProvider.getUriForFile(getActivity(), "com.taisheng.now.fileprovider", new File(Environment
                    .getExternalStorageDirectory(), "temp_picture.jpg"));
            getActivity().getContentResolver().delete(source, null, null);
        } else {

            File picture = new File(Environment.getExternalStorageDirectory()
                    , "temp_picture.jpg");
            if (picture.exists() && picture.isFile()) {
                picture.delete();
            }
        }
    }

    /**
     * 绑定内容view
     *
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView) {
        this.contentView = contentView;
    }

    private MyChatroomListAdapter mAdapter;

    private void initView(View layout) {

        mCBEmotionBtn = ((CheckBox) layout.findViewById(R.id.emotion_button));

        vEditText = ((EditText) layout.findViewById(R.id.bar_edit_text));
        vSendBtn = ((Button) layout.findViewById(R.id.bar_btn_send));
        vSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + vEditText.getText().toString());

//                ((TextView) contentView).setText(SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
//                        getActivity(), ((TextView) contentView), mEdtContent.getText().toString()));
                sendBiaoqingMsg((SpanStringUtils.megetEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                        getActivity(), vEditText.getText().toString())).toString());
                vEditText.setText("");
            }
        });
        iv_sendimg = layout.findViewById(R.id.iv_sendimg);
        vEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    vSendBtn.setVisibility(View.VISIBLE);
                    iv_sendimg.setVisibility(View.GONE);
                } else {
                    vSendBtn.setVisibility(View.GONE);
                    iv_sendimg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        iv_sendimg = layout.findViewById(R.id.iv_sendimg);
        iv_sendimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modifyAvatar();
            }
        });


        mNoHorizontalVP = ((NoHorizontalScrollerViewPager) layout.findViewById(R.id.vp_emotionview_layout));
        mBottomRecyclerView = ((RecyclerView) layout.findViewById(R.id.recyclerview_horizontal));

        mDatas = new ArrayList<>();
        mAdapter = new MyChatroomListAdapter();
        vMsgList = (ListView) layout.findViewById(R.id.msg_list);
        vMsgList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        vMsgList.setOnItemLongClickListener(this);
        mAdapter = new MyChatroomListAdapter();
        vMsgList.setAdapter(mAdapter);
        vMsgList.setOverScrollMode(View.OVER_SCROLL_NEVER);


        vMsgList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

                if ((event.getAction() == MotionEvent.ACTION_DOWN)
                        && (view.getId() == R.id.msg_list)) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });


    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(mDatas.get(position).getMsg());
        Toast.makeText(getActivity(), "消息已复制", Toast.LENGTH_LONG).show();
        return false;
    }

    private final int REQ_CODE_PHOTO_SOURCE = 6;//选择方式
    private final int REQ_CODE_GET_PHOTO_FROM_GALLERY = 10;//从相册获取
    private final int REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO = 11;//拍照完

    public void modifyAvatar() {


        Intent intent = new Intent(getActivity(), SelectAvatarSourceDialog.class);
        startActivityForResult(intent, REQ_CODE_PHOTO_SOURCE);
    }


    public final static int REQUEST_CAMERA = 1;

    public final static int REQUEST_WRITEEXTRENAL_STOR = 2;

    public final static int REQUEST_AUDIO = 3;

    private void onPhotoSource(int mode) {
        if (mode == R.id.btn_pick_from_library) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, REQ_CODE_GET_PHOTO_FROM_GALLERY);

        } else if (mode == R.id.btn_take_photo) {


            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            } else {

                permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITEEXTRENAL_STOR);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.taisheng.now.fileprovider", new File(Environment
                                .getExternalStorageDirectory(), "temp_picture.jpg"));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_picture.jpg")));
                    }
                    startActivityForResult(intent, REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO);
                }
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_PHOTO_SOURCE:
                if (data != null) {
                    int mode = data.getIntExtra(SelectAvatarSourceDialog.TAG_MODE, -1);
                    onPhotoSource(mode);
                }
                break;
            case REQ_CODE_GET_PHOTO_FROM_GALLERY:
                if (data != null && data.getData() != null) {
                    Bundle bundle = new Bundle();
                    // 选择图片后进入裁剪
                    String path = data.getData().getPath();
                    Uri source = data.getData();

//                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

//                    String path = FileUtilcll.saveFile(this, "pic1.jpg", bitmap);
                    getRealFilePath(getActivity(), source);

                    uploadPicture(getRealFilePath(getActivity(), source));

//                    beginCrop(source, bundle);

                }
                break;
            case REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO:
                // 判断相机是否有返回

                Uri source;
                Bundle bundle = new Bundle();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    source = FileProvider.getUriForFile(getActivity(), "com.taisheng.now.fileprovider", new File(Environment
                            .getExternalStorageDirectory(), "temp_picture.jpg"));
                } else {
                    // 选择图片后进入裁剪
                    File picture = new File(Environment.getExternalStorageDirectory()
                            , "temp_picture.jpg");
                    if (!picture.exists()) {
                        return;
                    }
                    source = Uri.fromFile(picture);
                }


                dealPicture(source);


//                beginCrop(source, bundle);


                break;

        }
    }


    private String dirPath = Apputil.sdNormalPath + "/watchchatImage";// 存储裁剪图片目录


    public void dealPicture(Uri source) {


        InputStream is = null;

        try {
            is = getActivity().getContentResolver().openInputStream(source);

            File dir = new File(dirPath);// 裁剪图片目录
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, getFileNameByTime());
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                int len = 0;
                byte[] bys = new byte[1024];
                while ((len = is.read(bys)) != -1) {
                    bos.write(bys);
                }
                bos.flush();
                uploadPicture(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                CrashReport.postCatchedException(e);
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
            CrashReport.postCatchedException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static String getFileNameByTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date()) + ".jpg";
    }


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void uploadPicture(String path) {
        try {

            //把Bitmap保存到sd卡中
            File fImage = new File(path);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fImage);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", fImage.getName(), requestFile);
            ApiUtils.getApiService_hasdialog(getActivity()).uploadLogo(body).enqueue(new TaiShengCallback<BaseBean<PictureBean>>() {

                                                                            @Override
                                                                            public void onSuccess(Response<BaseBean<PictureBean>> response, BaseBean<PictureBean> message) {
                                                                                switch (message.code) {
                                                                                    case Constants.HTTP_SUCCESS:
                                                                                        String path = message.result.path;


                                                                                        EventBus.getDefault().post(new EventManage.uploadChatPictureSuccess(path));

                                                                                        sendImgMsg(path);

                                                                                        break;
                                                                                }


                                                                            }

                                                                            @Override
                                                                            public void onFail(Call<BaseBean<PictureBean>> call, Throwable t) {

                                                                            }
                                                                        }
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initData() {

        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.drawable.ic_emotion);
                model1.flag = "经典笑脸";
                model1.isSelected = true;
                list.add(model1);
            } else {//创建其他的表情fragment，不需要可以注释
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.drawable.ic_plus);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }


        //记录底部默认选中第一个
        CurrentPosition = 0;
        SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        mBottomRecyclerView.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        mBottomRecyclerView.setAdapter(horizontalRecyclerviewAdapter);
        mBottomRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SharedPreferencedUtils.getInteger(getActivity(), CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                mNoHorizontalVP.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });


    }

    /**
     * 创建其他的表情fragment，不需要可以注释
     */
    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotionComplateFragment f1 = (EmotionComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        Bundle b = null;
        for (int i = 0; i < 7; i++) {
            b = new Bundle();
            b.putString("Interge", "Fragment-" + i);
            FragmentOther fg = FragmentOther.newInstance(FragmentOther.class, b);
            fragments.add(fg);
        }

        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(), fragments);
        mNoHorizontalVP.setAdapter(adapter);
    }

    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        return mEmotionKeyboard.interceptBackPress();
    }


    @Override
    public void onResume() {
        super.onResume();
        doctorName = WatchInstance.getInstance().realName;
        doctorAvator = WatchInstance.getInstance().headUrl;
        mDatas.clear();
        mTargetId = WatchInstance.getInstance().deviceId;
        List<MessageBean> list = MLOC.getMessageList(mTargetId);
        if (list != null && list.size() > 0) {
            mDatas.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
        vMsgList.setSelection(mAdapter.getCount() - 1);

    }


//    private void sendWenziMsg(String msg) {
//        String rawMessage = ",fhadmin-msg," + UserInstance.getInstance().getUid() + ",fh," + mTargetId + ",fh,"
//                + UserInstance.getInstance().getNickname() + ",fh,普通用户,fh," + UserInstance.getInstance().getRealname()
//                + ",fh,friend,fh," + UserInstance.getInstance().userInfo.avatar + ",fh," + msg;
//
//        WebSocketManager.getInstance().sendMessage(rawMessage);
//
//        RemoteChatMessage message = new RemoteChatMessage();
//        message.contentData = msg;
//        message.targetId = mTargetId;
//        message.fromId = UserInstance.getInstance().getUid();
//
//        HistoryBean historyBean = new HistoryBean();
//        historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
//        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
//        historyBean.setLastMsg(message.contentData);
//        historyBean.setConversationId(message.targetId);
//        historyBean.setNewMsgCount(1);
//        historyBean.doctorAvator = doctorAvator;
//        historyBean.doctorName = doctorName;
//        MLOC.addHistory(historyBean, true);
//
//        MessageBean messageBean = new MessageBean();
//        messageBean.setConversationId(message.targetId);
//        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
//        messageBean.setMsg(message.contentData);
//        messageBean.setFromId(message.fromId);
//        MLOC.saveMessage(messageBean);
//
//        ColorUtils.getColor(getActivity(), message.fromId);
//        mDatas.add(messageBean);
//        mAdapter.notifyDataSetChanged();
//    }

    private void sendImgMsg(String path) {
        String pathString = "img[" + path + "]";
        String rawMessage = ",fhadmin-msg," + UserInstance.getInstance().getUid() + ",fh," + mTargetId + ",fh,"
                + UserInstance.getInstance().getNickname() + ",fh,普通用户,fh," + UserInstance.getInstance().getRealname()
                + ",fh,friend,fh," + UserInstance.getInstance().userInfo.avatar + ",fh," + pathString;

        WebSocketManager.getInstance().sendMessage(rawMessage);

        RemoteChatMessage message = new RemoteChatMessage();
        message.contentData = pathString;
        message.targetId = mTargetId;
        message.fromId = UserInstance.getInstance().getUid();

//        HistoryBean historyBean = new HistoryBean();
//        historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
//        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
//        historyBean.setLastMsg(message.contentData);
//        historyBean.setConversationId(message.targetId);
//        historyBean.setNewMsgCount(1);
//        historyBean.doctorAvator = doctorAvator;
//        historyBean.doctorName = doctorName;
//        MLOC.addHistory(historyBean, true);

        MessageBean messageBean = new MessageBean();
        messageBean.setConversationId(message.targetId);
        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        messageBean.setMsg(message.contentData);
        messageBean.setFromId(message.fromId);
        MLOC.saveMessage(messageBean);

        ColorUtils.getColor(getActivity(), message.fromId);
        mDatas.add(messageBean);
        mAdapter.notifyDataSetChanged();
    }


    //发送加表情的文字
    private void sendBiaoqingMsg(String biaoqing) {

        String pathString = biaoqing.replaceAll("\\[", "face[");
        String rawMessage = ",fhadmin-msg," + UserInstance.getInstance().getUid() + ",fh," + mTargetId + ",fh,"
                + UserInstance.getInstance().getNickname() + ",fh,普通用户,fh," + UserInstance.getInstance().getRealname()
                + ",fh,friend,fh," + UserInstance.getInstance().userInfo.avatar + ",fh," + pathString;

        WebSocketManager.getInstance().sendMessage(rawMessage);

        RemoteChatMessage message = new RemoteChatMessage();
        message.contentData = biaoqing;
        message.targetId = mTargetId;
        message.fromId = UserInstance.getInstance().getUid();

//        HistoryBean historyBean = new HistoryBean();
//        historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
//        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
//        historyBean.setLastMsg(message.contentData);
//        historyBean.setConversationId(message.targetId);
//        historyBean.setNewMsgCount(1);
//        historyBean.doctorAvator = doctorAvator;
//        historyBean.doctorName = doctorName;
//        MLOC.addHistory(historyBean, true);

        MessageBean messageBean = new MessageBean();
        messageBean.setConversationId(message.targetId);
        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        messageBean.setMsg(message.contentData);
        messageBean.setFromId(message.fromId);
        MLOC.saveMessage(messageBean);

        ColorUtils.getColor(getActivity(), message.fromId);
        mDatas.add(messageBean);
        mAdapter.notifyDataSetChanged();
    }

    //发送语音
    private void sendYuyinMsg(String data) {

//        String pathString = biaoqing.replaceAll("\\[", "face[");
//        String rawMessage = ",fhadmin-msg," + UserInstance.getInstance().getUid() + ",fh," + mTargetId + ",fh,"
//                + UserInstance.getInstance().getNickname() + ",fh,普通用户,fh," + UserInstance.getInstance().getRealname()
//                + ",fh,friend,fh," + UserInstance.getInstance().userInfo.avatar + ",fh," + pathString;
//
//        WebSocketManager.getInstance().sendMessage(rawMessage);

        RemoteChatMessage message = new RemoteChatMessage();
        message.contentData = data;
        message.targetId = mTargetId;
        message.fromId = UserInstance.getInstance().getUid();

//        HistoryBean historyBean = new HistoryBean();
//        historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
//        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
//        historyBean.setLastMsg(message.contentData);
//        historyBean.setConversationId(message.targetId);
//        historyBean.setNewMsgCount(1);
//        historyBean.doctorAvator = doctorAvator;
//        historyBean.doctorName = doctorName;
//        MLOC.addHistory(historyBean, true);

        MessageBean messageBean = new MessageBean();
        messageBean.setConversationId(message.targetId);
        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        messageBean.setMsg(message.contentData);
        messageBean.setFromId(message.fromId);
        MLOC.saveMessage(messageBean);

        ColorUtils.getColor(getActivity(), message.fromId);
        mDatas.add(messageBean);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void recevieMessage(EventManage.Watch_AEVENT_C2C_REV_MSG eventObj) {
        MLOC.d("IM_C2C", "||" + eventObj);
        final RemoteChatMessage revMsg = (RemoteChatMessage) eventObj.message;

        String contentData = revMsg.contentData.replace("face[", "[");
//            HistoryBean historyBean = new HistoryBean();
//            historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
//            historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
//            historyBean.setLastMsg(contentData);
//            historyBean.setConversationId(revMsg.fromId);
//            historyBean.setNewMsgCount(1);
//            historyBean.doctorName = doctorName;
//            historyBean.doctorAvator = doctorAvator;
//            MLOC.addHistory(historyBean, true);

        MessageBean messageBean = new MessageBean();
        messageBean.setConversationId(revMsg.fromId);
        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        messageBean.setMsg(contentData);
        messageBean.setFromId(revMsg.fromId);
        if (revMsg.fromId.equals(mTargetId)) {
            //只刷新当前手表的聊天窗口
            mDatas.add(messageBean);
            mAdapter.notifyDataSetChanged();
        }
    }


    public class MyChatroomListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyChatroomListAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mAnimationDrawables = new ArrayList<>();
        }

        @Override
        public int getCount() {
            if (mDatas == null) return 0;
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            if (mDatas == null)
                return null;
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (mDatas == null)
                return 0;
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getFromId().equals(MLOC.userId) ? 0 : 1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int currLayoutType = getItemViewType(position);
            if (currLayoutType == 0) { //自己的信息
                final ViewHolder itemSelfHolder;
                if (convertView == null) {
                    itemSelfHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_watch_chat_msg_list_right, null);
                    itemSelfHolder.vUserId = (TextView) convertView.findViewById(R.id.item_user_id);
                    itemSelfHolder.vMsg = (TextView) convertView.findViewById(R.id.item_msg);
                    itemSelfHolder.sdw_pic = convertView.findViewById(R.id.sdw_pic);
                    itemSelfHolder.ll_yuyin = convertView.findViewById(R.id.ll_yuyin);
                    itemSelfHolder.ieaIvVoiceLine = (ImageView) convertView.findViewById(R.id.iea_iv_voiceLine);
                    itemSelfHolder.ieaLlSinger = (LinearLayout) convertView.findViewById(R.id.iea_ll_singer);
                    itemSelfHolder.ieaTvVoicetime1 = (TextView) convertView.findViewById(R.id.iea_tv_voicetime1);
                    itemSelfHolder.iea_iv_sendfail = (ImageView) convertView.findViewById(R.id.iea_iv_sendfail);
//                    itemSelfHolder.vHeadBg = convertView.findViewById(R.id.head_bg);
                    itemSelfHolder.sdv_header = convertView.findViewById(R.id.sdv_header);
//                    itemSelfHolder.vHeadCover = (CircularCoverView) convertView.findViewById(R.id.head_cover);
//                    itemSelfHolder.vHeadImage = (ImageView) convertView.findViewById(R.id.head_img);
                    convertView.setTag(itemSelfHolder);
                } else {
                    itemSelfHolder = (ViewHolder) convertView.getTag();
                }
                itemSelfHolder.vUserId.setText(UserInstance.getInstance().getNickname());
                String rawmessage = mDatas.get(position).getMsg();
                if (rawmessage.startsWith("img[") && rawmessage.endsWith("]")) {
                    rawmessage = rawmessage.replace("img[", "");
                    rawmessage = rawmessage.replace("]", "");
                    itemSelfHolder.sdw_pic.setVisibility(View.VISIBLE);
                    itemSelfHolder.vMsg.setVisibility(View.GONE);
                    itemSelfHolder.ll_yuyin.setVisibility(View.GONE);

                    itemSelfHolder.sdw_pic.setImageURI(Uri.parse(Constants.Url.File_Host + rawmessage));
                    String finalRawmessage = rawmessage;
                    itemSelfHolder.sdw_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                            //组织数据
//                            ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>(); // 这个最好定义成成员变量
//                            ThumbViewInfo item;
//                            mThumbViewInfoList.clear();
////                            for (int i = 0;i < resultList.size(); i++) {
//                            Rect bounds = new Rect();
//                            //new ThumbViewInfo(图片地址);
//                            item = new ThumbViewInfo(Constants.Url.File_Host + finalRawmessage);
//                            item.setBounds(bounds);
//                            mThumbViewInfoList.add(item);
////                            }
//
////打开预览界面
//                            GPreviewBuilder.from(getActivity())
//                                    //是否使用自定义预览界面，当然8.0之后因为配置问题，必须要使用
//                                    .to(ImageLookActivity.class)
//                                    .setData(mThumbViewInfoList)
//                                    .setCurrentIndex(0)
//                                    .setSingleFling(true)
//                                    .setType(GPreviewBuilder.IndicatorType.Number)
//                                    // 小圆点
////  .setType(GPreviewBuilder.IndicatorType.Dot)
//                                    .start();//启动
                            String url = Constants.Url.File_Host + finalRawmessage;
                            ArrayList<String> urls = new ArrayList<>();
                            urls.add(url);
                            ImageZoom.show(getActivity(), url, urls);

                        }
                    });

                } else if (rawmessage.startsWith("audio[") && rawmessage.endsWith("]")) {
                    itemSelfHolder.sdw_pic.setVisibility(View.GONE);
                    itemSelfHolder.vMsg.setVisibility(View.GONE);
                    itemSelfHolder.ll_yuyin.setVisibility(View.VISIBLE);

                    rawmessage = rawmessage.replace("audio[", "");
                    rawmessage = rawmessage.replace("]", "");

//                    String rawAudiomessage="audio[";
//                    rawAudiomessage+=((seconds <= 0 ? 1 : (int) seconds)+","+filePath+","+"1]");
                    String[] temp = rawmessage.split(",");

                    String seconds = temp[0];
                    int secondstemp = Integer.parseInt(seconds);
                    String filePath = temp[1];
                    String isSendFail = temp[2];
                    //设置显示时长
                    itemSelfHolder.ieaTvVoicetime1.setText(secondstemp <= 0 ? 1 + "''" : seconds + "''");
                    if (!"1".equals(isSendFail)) {
                        itemSelfHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
                    } else {
                        itemSelfHolder.iea_iv_sendfail.setVisibility(View.GONE);
                    }
                    itemSelfHolder.iea_iv_sendfail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                itemSelfHolder.iea_iv_sendfail.setVisibility(View.GONE);
                                File fYuyin = new File(filePath);

                                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fYuyin);
                                MultipartBody.Part body = MultipartBody.Part.createFormData("file", fYuyin.getName(), requestFile);
                                ApiUtils.getApiService().microcharVoice(UserInstance.getInstance().getUid(), WatchInstance.getInstance().deviceId, body).enqueue(new TaiShengCallback<BaseBean<PictureBean>>() {

                                                                                                                                                                     @Override
                                                                                                                                                                     public void onSuccess(Response<BaseBean<PictureBean>> response, BaseBean<PictureBean> message) {
                                                                                                                                                                         switch (message.code) {
                                                                                                                                                                             case Constants.HTTP_SUCCESS:

                                                                                                                                                                                 int length = mDatas.get(position).msg.length();
                                                                                                                                                                                 mDatas.get(position).msg = mDatas.get(position).msg.substring(0, length - 2);
                                                                                                                                                                                 mDatas.get(position).msg += "1]";
                                                                                                                                                                                 MLOC.updateMessage(mDatas.get(position));
                                                                                                                                                                                 break;
                                                                                                                                                                             case 4031:
                                                                                                                                                                                 itemSelfHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
                                                                                                                                                                                 break;
                                                                                                                                                                             default:
                                                                                                                                                                                 itemSelfHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
                                                                                                                                                                                 break;
                                                                                                                                                                         }


                                                                                                                                                                     }

                                                                                                                                                                     @Override
                                                                                                                                                                     public void onFail(Call<BaseBean<PictureBean>> call, Throwable t) {
                                                                                                                                                                         itemSelfHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
                                                                                                                                                                     }
                                                                                                                                                                 }
                                );

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    //更改并显示录音条长度
                    RelativeLayout.LayoutParams ps = (RelativeLayout.LayoutParams) itemSelfHolder.ieaIvVoiceLine.getLayoutParams();
                    ps.width = CommonsUtils.getVoiceLineWight(getActivity(), secondstemp);
                    itemSelfHolder.ieaIvVoiceLine.setLayoutParams(ps); //更改语音长条长度
                    //开始设置监听
                    final LinearLayout ieaLlSinger = itemSelfHolder.ieaLlSinger;
                    itemSelfHolder.ieaIvVoiceLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //只要点击就设置为已播放状态（隐藏小红点）
//                            record.setPlayed(true);
//

//                            notifyDataSetChanged();
                            //这里更新数据库小红点。这里不知道为什么可以强转建议复习复习基础~
//                            ((ExampleActivity) mContext).getMgr().updateRecord(record);


                            final AnimationDrawable animationDrawable = (AnimationDrawable) ieaLlSinger.getBackground();
                            //重置动画
                            resetAnim(animationDrawable);
                            animationDrawable.start();


                            //记录当前位置正在播放。
                            pos = position;


                            //播放前重置。
                            MediaManager.release();
                            //开始实质播放
                            MediaManager.playSound(filePath,
                                    new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            animationDrawable.selectDrawable(0);//显示动画第一帧
                                            animationDrawable.stop();

                                            //播放完毕，当前播放索引置为-1。
                                            pos = -1;
                                        }
                                    });
                        }
                    });


                } else {
                    itemSelfHolder.sdw_pic.setVisibility(View.GONE);
                    itemSelfHolder.vMsg.setVisibility(View.VISIBLE);
                    itemSelfHolder.ll_yuyin.setVisibility(View.GONE);
                    itemSelfHolder.vMsg.setText(SpanStringUtils.megetEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                            getActivity(), (mDatas.get(position).getMsg()).toString()));
                }

                itemSelfHolder.sdv_header.setImageURI(Uri.parse(Constants.Url.File_Host + UserInstance.getInstance().userInfo.avatar));
//                itemSelfHolder.vHeadBg.setBackgroundColor(ColorUtils.getColor(C2CActivity.this,mDatas.get(position).getFromId()));
//                itemSelfHolder.vHeadCover.setCoverColor(Color.parseColor("#f6f6f6"));
//                int cint = DensityUtil.dip2px(C2CActivity.this,20);
//                itemSelfHolder.vHeadCover.setRadians(cint, cint, cint, cint,0);
//                itemSelfHolder.vHeadImage.setImageResource(MLOC.getHeadImage(C2CActivity.this,mDatas.get(position).getFromId()));
            } else if (currLayoutType == 1) {//别人的信息
                final ViewHolder itemOtherHolder;
                if (convertView == null) {
                    itemOtherHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_watch_chat_msg_list_left, null);
                    itemOtherHolder.vUserId = (TextView) convertView.findViewById(R.id.item_user_id);
                    itemOtherHolder.vMsg = (TextView) convertView.findViewById(R.id.item_msg);
                    itemOtherHolder.sdw_pic = convertView.findViewById(R.id.sdw_pic);
                    itemOtherHolder.ll_yuyin = convertView.findViewById(R.id.ll_yuyin);
                    itemOtherHolder.ieaIvVoiceLine = (ImageView) convertView.findViewById(R.id.iea_iv_voiceLine);
                    itemOtherHolder.ieaLlSinger = (LinearLayout) convertView.findViewById(R.id.iea_ll_singer);
                    itemOtherHolder.ieaTvVoicetime1 = (TextView) convertView.findViewById(R.id.iea_tv_voicetime1);
                    itemOtherHolder.iea_iv_sendfail = (ImageView) convertView.findViewById(R.id.iea_iv_red);
//                    itemOtherHolder.vHeadBg = convertView.findViewById(R.id.head_bg);
                    itemOtherHolder.sdv_header = convertView.findViewById(R.id.sdv_header);
//                    itemOtherHolder.vHeadCover = (CircularCoverView) convertView.findViewById(R.id.head_cover);
//                    itemOtherHolder.vHeadImage = (ImageView) convertView.findViewById(R.id.head_img);
                    convertView.setTag(itemOtherHolder);
                } else {
                    itemOtherHolder = (ViewHolder) convertView.getTag();
                }
                itemOtherHolder.vUserId.setText(doctorName);
                String rawmessage = mDatas.get(position).getMsg();
                if (rawmessage.startsWith("img[") && rawmessage.endsWith("]")) {
                    rawmessage = rawmessage.replace("img[", "");
                    rawmessage = rawmessage.replace("]", "");
                    itemOtherHolder.sdw_pic.setVisibility(View.VISIBLE);
                    itemOtherHolder.vMsg.setVisibility(View.GONE);
                    itemOtherHolder.sdw_pic.setImageURI(Uri.parse(Constants.Url.File_Host + rawmessage));
                    String finalRawmessage = rawmessage;
                    itemOtherHolder.sdw_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            //组织数据
//                            ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>(); // 这个最好定义成成员变量
//                            ThumbViewInfo item;
//                            mThumbViewInfoList.clear();
////                            for (int i = 0;i < resultList.size(); i++) {
//                            Rect bounds = new Rect();
//                            //new ThumbViewInfo(图片地址);
//                            item = new ThumbViewInfo(Constants.Url.File_Host + finalRawmessage);
//                            item.setBounds(bounds);
//                            mThumbViewInfoList.add(item);
////                            }
//
////打开预览界面
//                            GPreviewBuilder.from(getActivity())
//                                    //是否使用自定义预览界面，当然8.0之后因为配置问题，必须要使用
//                                    .to(ImageLookActivity.class)
//                                    .setData(mThumbViewInfoList)
//                                    .setCurrentIndex(0)
//                                    .setSingleFling(true)
//                                    .setType(GPreviewBuilder.IndicatorType.Number)
//                                    // 小圆点
////  .setType(GPreviewBuilder.IndicatorType.Dot)
//                                    .start();//启动
                            String url = Constants.Url.File_Host + finalRawmessage;
                            ArrayList<String> urls = new ArrayList<>();
                            urls.add(url);
                            ImageZoom.show(getActivity(), url, urls);
                        }
                    });

                } else if (rawmessage.startsWith("audio[") && rawmessage.endsWith("]")) {
                    itemOtherHolder.sdw_pic.setVisibility(View.GONE);
                    itemOtherHolder.vMsg.setVisibility(View.GONE);
                    itemOtherHolder.ll_yuyin.setVisibility(View.VISIBLE);

                    rawmessage = rawmessage.replace("audio[", "");
                    rawmessage = rawmessage.replace("]", "");

//                    String rawAudiomessage="audio[";
//                    rawAudiomessage+=((seconds <= 0 ? 1 : (int) seconds)+","+filePath+","+"1]");
                    String[] temp = rawmessage.split(",");

                    String seconds = temp[0];
                    int secondstemp = Integer.parseInt(seconds);
                    String filePath = temp[1];
                    String isRead = temp[2];
                    //设置显示时长
                    itemOtherHolder.ieaTvVoicetime1.setText(secondstemp <= 0 ? 1 + "''" : seconds + "''");
                    if ("1".equals(isRead)) {
                        itemOtherHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
                    } else {
                        itemOtherHolder.iea_iv_sendfail.setVisibility(View.GONE);
                    }
//                    itemOtherHolder.iea_iv_sendfail.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            try {
//                                itemOtherHolder.iea_iv_sendfail.setVisibility(View.GONE);
//                                File fYuyin = new File(filePath);
//
//                                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fYuyin);
//                                MultipartBody.Part body = MultipartBody.Part.createFormData("file", fYuyin.getName(), requestFile);
//                                ApiUtils.getApiService().microcharVoice(UserInstance.getInstance().getUid(), WatchInstance.getInstance().deviceId, body).enqueue(new TaiShengCallback<BaseBean<PictureBean>>() {
//
//                                                                                                                                                                     @Override
//                                                                                                                                                                     public void onSuccess(Response<BaseBean<PictureBean>> response, BaseBean<PictureBean> message) {
//                                                                                                                                                                         switch (message.code) {
//                                                                                                                                                                             case Constants.HTTP_SUCCESS:
//
//                                                                                                                                                                                 int length = mDatas.get(position).msg.length();
//                                                                                                                                                                                 mDatas.get(position).msg = mDatas.get(position).msg.substring(0, length - 2);
//                                                                                                                                                                                 mDatas.get(position).msg += "1]";
//                                                                                                                                                                                 MLOC.updateMessage(mDatas.get(position));
//                                                                                                                                                                                 break;
//                                                                                                                                                                             case 4031:
//                                                                                                                                                                                 itemSelfHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
//                                                                                                                                                                                 break;
//                                                                                                                                                                             default:
//                                                                                                                                                                                 itemSelfHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
//                                                                                                                                                                                 break;
//                                                                                                                                                                         }
//
//
//                                                                                                                                                                     }
//
//                                                                                                                                                                     @Override
//                                                                                                                                                                     public void onFail(Call<BaseBean<PictureBean>> call, Throwable t) {
//                                                                                                                                                                         itemSelfHolder.iea_iv_sendfail.setVisibility(View.VISIBLE);
//                                                                                                                                                                     }
//                                                                                                                                                                 }
//                                );
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });

                    //更改并显示录音条长度
                    RelativeLayout.LayoutParams ps = (RelativeLayout.LayoutParams) itemOtherHolder.ieaIvVoiceLine.getLayoutParams();
                    ps.width = CommonsUtils.getVoiceLineWight(getActivity(), secondstemp);
                    itemOtherHolder.ieaIvVoiceLine.setLayoutParams(ps); //更改语音长条长度
                    //开始设置监听
                    final LinearLayout ieaLlSinger = itemOtherHolder.ieaLlSinger;
                    itemOtherHolder.ieaIvVoiceLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //只要点击就设置为已播放状态（隐藏小红点）
//                            record.setPlayed(true);
//
                            if ("1".equals(isRead)) {
                                itemOtherHolder.iea_iv_sendfail.setVisibility(View.GONE);
                                int length = mDatas.get(position).msg.length();
                                mDatas.get(position).msg = mDatas.get(position).msg.substring(0, length - 2);
                                mDatas.get(position).msg += "0]";
                                MLOC.updateMessage(mDatas.get(position));
                            }
//
//                            notifyDataSetChanged();
                            //这里更新数据库小红点。这里不知道为什么可以强转建议复习复习基础~
//                            ((ExampleActivity) mContext).getMgr().updateRecord(record);


                            final AnimationDrawable animationDrawable = (AnimationDrawable) ieaLlSinger.getBackground();
                            //重置动画
                            resetAnim(animationDrawable);
                            animationDrawable.start();


                            //记录当前位置正在播放。
                            pos = position;


                            //播放前重置。
                            MediaManager.release();
                            //开始实质播放
                            MediaManager.playSound(filePath,
                                    new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            animationDrawable.selectDrawable(0);//显示动画第一帧
                                            animationDrawable.stop();

                                            //播放完毕，当前播放索引置为-1。
                                            pos = -1;
                                        }
                                    });
                        }
                    });


                } else {
                    itemOtherHolder.sdw_pic.setVisibility(View.GONE);
                    itemOtherHolder.vMsg.setVisibility(View.VISIBLE);
                    String faceWords = mDatas.get(position).getMsg().toString();
                    faceWords = faceWords.replace("face[", "[");
                    itemOtherHolder.vMsg.setText(SpanStringUtils.megetEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                            getActivity(), (faceWords)));
                }
//                itemOtherHolder.vMsg.setText(mDatas.get(position).getMsg());
                if (doctorAvator != null && !"".equals(doctorAvator)) {
                    Uri uri = Uri.parse(Constants.Url.File_Host+doctorAvator);
                    itemOtherHolder.sdv_header.setImageURI(uri);
                }
//                itemOtherHolder.vHeadBg.setBackgroundColor(ColorUtils.getColor(C2CActivity.this,mDatas.get(position).getFromId()));
//                itemOtherHolder.vHeadCover.setCoverColor(Color.parseColor("#f6f6f6"));
//                int cint = DensityUtil.dip2px(C2CActivity.this,20);
//                itemOtherHolder.vHeadCover.setRadians(cint, cint, cint, cint,0);
//                itemOtherHolder.vHeadImage.setImageResource(MLOC.getHeadImage(C2CActivity.this,mDatas.get(position).getFromId()));
            }
            return convertView;
        }


        private void resetAnim(AnimationDrawable animationDrawable) {
            if (!mAnimationDrawables.contains(animationDrawable)) {
                mAnimationDrawables.add(animationDrawable);
            }
            for (AnimationDrawable ad : mAnimationDrawables) {
                ad.selectDrawable(0);
                ad.stop();
            }
        }

        List<AnimationDrawable> mAnimationDrawables;
        int pos = -1;//标记当前录音索引，默认没有播放任何一个


    }

    public class ViewHolder {
        public TextView vUserId;
        public TextView vMsg;
        public SimpleDraweeView sdw_pic;

        public SimpleDraweeView sdv_header;

        public View ll_yuyin;
        ImageView ieaIvVoiceLine;
        LinearLayout ieaLlSinger;
        TextView ieaTvVoicetime1;
        ImageView ieaIvRed;
        ImageView iea_iv_sendfail;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        globalOnItemClickManager.unAttachToEditText();
        EventBus.getDefault().unregister(this);

    }
}

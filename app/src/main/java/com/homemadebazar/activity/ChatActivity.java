package com.homemadebazar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.ChatConversationAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.UploadFileTask;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetChatMessageApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import id.zelory.compressor.Compressor;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    public static String KEY_TARGET_USERID="KEY_TARGET_USERID";
    private ImageView ivBack, ivUserProfile, ivAttachment;
    private EditText etChatMessage;
    private RecyclerView recyclerView;
    private UserModel userModel;
    private UserModel targetUserModel;
    private ArrayList<ChatMessageModel> chatMessageModelArrayList = new ArrayList<>();
    private ChatConversationAdapter chatConversationAdapter;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout llAttachmentLayout;
    private Uri chatImageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public static Intent getChatIntent(Context context,UserModel targetUserId){
        Intent intent=new Intent(context,ChatActivity.class);
        intent.putExtra(KEY_TARGET_USERID,targetUserId);
        return intent;
    }

    private void getBundleData(){
        targetUserModel= (UserModel) getIntent().getSerializableExtra(KEY_TARGET_USERID);
    }

    @Override
    protected void initUI() {
        getBundleData();
        linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        userModel = SharedPreference.getUserModel(ChatActivity.this);
        ivBack = findViewById(R.id.iv_back);
        ivUserProfile = findViewById(R.id.iv_user_profile);
        ivAttachment = findViewById(R.id.iv_attachment);
        etChatMessage = findViewById(R.id.et_chat_message);
        recyclerView = findViewById(R.id.recycler_view);
        llAttachmentLayout = findViewById(R.id.ll_attachment_layout);
    }

    @Override
    protected void initialiseListener() {
        ivAttachment.setOnClickListener(this);
        findViewById(R.id.iv_chat_send).setOnClickListener(this);
        findViewById(R.id.ll_camera_attachment).setOnClickListener(this);
        findViewById(R.id.ll_gallery_attachment).setOnClickListener(this);
        findViewById(R.id.ll_location_attachment).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        chatConversationAdapter = new ChatConversationAdapter(ChatActivity.this, chatMessageModelArrayList, userModel.getUserId());
        recyclerView.setAdapter(chatConversationAdapter);
        getMessagesApi(userModel.getUserId(), targetUserModel.getUserId(), Constants.MessageSequeceOrder.CURRENT, getTimeStamp(Constants.MessageSequeceOrder.CURRENT));
    }

    private void setUpToolbar() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chat_send:
                if (isValid()) {
                    sendChatMessage(userModel.getUserId(), targetUserModel.getUserId(), etChatMessage.getText().toString().trim(), "", Constants.FileType.NONE,
                            Constants.MessageType.TEXT, "", "");
                    etChatMessage.setText("");
                }
                break;
            case R.id.iv_attachment:
                if (llAttachmentLayout.getVisibility() == View.VISIBLE) {
                    llAttachmentLayout.setVisibility(View.GONE);
                } else {
                    llAttachmentLayout.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.ll_camera_attachment:
                Utils.cameraIntent(ChatActivity.this);
                break;
            case R.id.ll_gallery_attachment:
                Utils.gallaryIntent(ChatActivity.this);
                break;
            case R.id.ll_location_attachment:

                break;
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etChatMessage.getText().toString().trim())) {
            DialogUtils.showAlert(ChatActivity.this, "You cannot send empty message");
            return false;
        } else {
            return true;
        }
    }


    private String getChatSendUrl(String userId, String receiverId, String text, String fileType, String msgType, String latitude, String longitude) {
        String url = Constants.ServerURL.SEND_MESSAGE + "SndrId=" + userId + "&RcrId=" + receiverId + "&text=" + text + "&FileType=" + fileType + "&MsgType=" + msgType
                + "&Lati=" + latitude + "&Longi=" + longitude;
        System.out.println(Constants.ServiceTAG.URL + url);
        return url;
    }


    public void sendChatMessage(final String userId, final String receiverId, String text, String imagePath, String fileType, String msgType, String latitude, String longitude) {
        String url = getChatSendUrl(userId, receiverId, text, fileType, msgType, latitude, longitude);
        try {
            File compressImageFile = null;
            if (!TextUtils.isEmpty(imagePath))
                compressImageFile = new Compressor(this).compressToFile(new File(imagePath));

            Hashtable<String, String> multipartParams = new Hashtable<>();

            final UploadFileTask fileTask = new UploadFileTask(this, url, compressImageFile != null ? compressImageFile.getPath() : "", multipartParams, "image_url", new UploadFileTask.FileUploadListener() {
                @Override
                public void onComplete(String response) {
                    System.out.println("SendChatMessage Response:-" + response);
                    try {

                        JSONObject object = new JSONObject(response);
                        if (object.optInt("StatusCode") == Constants.ServerResponseCode.SUCCESS) {
                            System.out.println("Message Sent");
                            getMessagesApi(userId, receiverId, Constants.MessageSequeceOrder.NEW, getTimeStamp(Constants.MessageSequeceOrder.NEW));

                        } else {
                            DialogUtils.showAlert(ChatActivity.this, object.optString("StatusMessage"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            fileTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Keys.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri uri = Utils.getCameraUri();
                    System.out.println("Camera URI:-" + uri);
                    if (uri != null) {
//                        ivProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        ivProductImage.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(this);
                    }

                } else {
                    DialogUtils.showAlert(this, "Camera Cancelled");
                }
                break;
            case Constants.Keys.REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    System.out.println();
                    Uri uri = data.getData();
                    System.out.println("Gallary URI:-" + uri);
                    if (uri != null) {
//                        ivProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        ivProductImage.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(this);
                    }

                }

                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    chatImageUri = result.getUri();
                    sendChatMessage(userModel.getUserId(), targetUserModel.getUserId(), "", chatImageUri.getPath(), Constants.FileType.IMAGE, Constants.MessageType.FILE, "", "");
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getTimeStamp(String messageSequenceOrder) {
        if (messageSequenceOrder.equals(Constants.MessageSequeceOrder.CURRENT))
            return "";
        else if (messageSequenceOrder.equals(Constants.MessageSequeceOrder.OLD)) {
            if (chatMessageModelArrayList.size() > 0)
                return chatMessageModelArrayList.get(0).getTimeStamp();
            else
                return "";
        } else if (messageSequenceOrder.equals(Constants.MessageSequeceOrder.NEW)) {
            if (chatMessageModelArrayList.size() > 0)
                return chatMessageModelArrayList.get(chatMessageModelArrayList.size() - 1).getTimeStamp();
            else
                return "";
        }
        return "";
    }

    private void getMessagesApi(String userId, String targetUserId, final String sequenceOrder, String timeStamp) {
        try {

            final GetChatMessageApiCall apiCall = new GetChatMessageApiCall(userId, targetUserId, sequenceOrder, timeStamp);
            HttpRequestHandler.getInstance(getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<ChatMessageModel> tempMessageModelArrayList = apiCall.getResult();
                                if (sequenceOrder.equals(Constants.MessageSequeceOrder.CURRENT)) {
                                    chatMessageModelArrayList.clear();
                                    chatMessageModelArrayList.addAll(tempMessageModelArrayList);
                                    chatConversationAdapter.notifyDataSetChanged();
                                } else if (sequenceOrder.equals(Constants.MessageSequeceOrder.NEW)) {
                                    chatMessageModelArrayList.addAll(tempMessageModelArrayList);
                                    chatConversationAdapter.notifyDataSetChanged();
                                } else if (sequenceOrder.equals(Constants.MessageSequeceOrder.OLD)) {
                                    chatMessageModelArrayList.addAll(0, tempMessageModelArrayList);
                                    chatConversationAdapter.notifyDataSetChanged();
                                }

                            } else {
                                DialogUtils.showAlert(ChatActivity.this, userModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), ChatActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), ChatActivity.this, null);
        }
    }
}

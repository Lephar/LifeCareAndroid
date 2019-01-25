package fit.lifecare.lifecare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import fit.lifecare.lifecare.ObjectClasses.ChatListMembers;
import fit.lifecare.lifecare.ObjectClasses.Messages;

public class DanismanimChatActivity extends AppCompatActivity {

    //Layout Views
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ImageView mPhotoPickerButton;
    private EditText mMessageEditText;
    private TextView PersonName;
    private ImageView PersonPhoto;
    private ImageView mSendButton;
    private String currentUserId;

    private String chatStatus;
    private String dietitianName;
    private String dietitianPhotoUrl;
    private String dietitianID;

    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatDatabaseReference;
    private DatabaseReference mChatIDReference;
    private ValueEventListener mSingleValueEventListener;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diyetisyenim_chat);

        // Initialize views
        mMessageListView = findViewById(R.id.messages_view);
        mPhotoPickerButton = findViewById(R.id.pick_photo);
        mMessageEditText = findViewById(R.id.mMessageEditText);
        mMessageEditText.setEnabled(false);
        mMessageEditText.setClickable(false);
        mSendButton = findViewById(R.id.send_message);
        PersonName = findViewById(R.id.person_name);
        PersonPhoto = findViewById(R.id.person_photo);

        ChatListMembers mychatListMember = (ChatListMembers) getIntent().getSerializableExtra("clickedchatlistmember");

        dietitianID = mychatListMember.getdietitianID();
        dietitianName = mychatListMember.getdietitianName();
        dietitianPhotoUrl = mychatListMember.getdietitianPhotoUrl();

        PersonName.setText(dietitianName);
        Glide.with(this)
                .load(dietitianPhotoUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().dontAnimate().dontTransform())
                .into(PersonPhoto);


        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChatIDReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo").child("ChatIDs").child(dietitianID);
        mFirebaseStorage = FirebaseStorage.getInstance();

        // Initialize message ListView and its adapter
        List<Messages> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.app_user_messages, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        initializeListViewListener();


        attachPersonalDatabaseReadListener();

    }

    private void initializeListViewListener() {

        registerForContextMenu(mMessageListView);

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Messages mychatmessage = (Messages) mMessageListView.getItemAtPosition(i);
                ImageView myimageView = view.findViewById(R.id.photoImageView);

                if (mychatmessage.getPhotoUrl() != null) {

                    Bitmap bitmap = ((BitmapDrawable)myimageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();

                    Intent intent = new Intent(DanismanimChatActivity.this, FullScreenPhoto.class);
                    intent.putExtra("picture", b);
                    startActivity(intent);
                }

            }
        });

    }

    //it checks chatID changed or not (chatID changes when dietitian code added succesfully)
    //if chatID changes set visibility of layouts to get chat mode and attach a new listener for chat
    private void attachPersonalDatabaseReadListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String chatID = dataSnapshot.getValue(String.class);
                    if (chatID != null) {
                        mChatDatabaseReference = mFirebaseDatabase.getReference()
                                .child("Chats").child(chatID);
                        mChatPhotosStorageReference = mFirebaseStorage.getReference()
                                .child("chat_photos").child(chatID);
                        attachChatDatabaseMessagesListener();
                        attachChatDatabaseStatusListener();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mChatIDReference.addValueEventListener(mValueEventListener);
        }
    }

    private void attachChatDatabaseStatusListener() {
        if (mSingleValueEventListener == null) {
            mSingleValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatStatus = dataSnapshot.getValue().toString();
                    if (chatStatus.equals("active")) {
                        initializeButtonsListeners();
                        initializeEditTextListener();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mChatDatabaseReference.child("connection_status").addListenerForSingleValueEvent(mSingleValueEventListener);
        }
    }

    private void attachChatDatabaseMessagesListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Messages friendlyMessage = dataSnapshot.getValue(Messages.class);
                    mMessageAdapter.add(friendlyMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mChatDatabaseReference.child("messages").addChildEventListener(mChildEventListener);
        }
    }

    //TODO
    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mChatDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            final StorageReference photoReference = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            photoReference.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return photoReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        //save last message time to update GUI accordingly
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        //create messages object and push it to firebase database
                        Messages messages = new Messages(null, downloadUri.toString(), timestamp.getTime(), false);
                        mChatDatabaseReference.child("messages").push().setValue(messages);

                        //increment DietitianUnreadedMessages by one for each new message send.
                        mChatDatabaseReference.child("DietitianUnreadedMessages").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    Long value = (Long) dataSnapshot.getValue();
                                    value += 1;
                                    dataSnapshot.getRef().setValue(value);
                                } else {
                                    dataSnapshot.getRef().setValue(1);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //save last message time to update GUI accordingly
                        mChatDatabaseReference.child("last_message_time").setValue(timestamp.getTime());

                    } else {
                        Toast.makeText(DanismanimChatActivity.this, ":Fotoğraf yüklenemedi ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void initializeButtonsListeners() {

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });

        //Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //increment DietitianUnreadedMessages by one for each new message send.
                mChatDatabaseReference.child("DietitianUnreadedMessages").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Long value = (Long) dataSnapshot.getValue();
                            value += 1;
                            dataSnapshot.getRef().setValue(value);
                        } else {
                            dataSnapshot.getRef().setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //save last message time to update GUI accordingly
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                mChatDatabaseReference.child("last_message_time").setValue(timestamp.getTime());

                //create messages object and push it to firebase database
                Messages messages = new Messages(mMessageEditText.getText().toString(), null, timestamp.getTime(), false);
                mChatDatabaseReference.child("messages").push().setValue(messages);

                // Clear input box
                mMessageEditText.setText("");
            }
        });
    }

    // Enable Send button when there's text to send
    private void initializeEditTextListener() {

        mMessageEditText.setClickable(true);
        mMessageEditText.setEnabled(true);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }
}

package fit.lifecare.lifecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fit.lifecare.lifecare.Dialogs.DanismanimCodeAddDialog;
import fit.lifecare.lifecare.ObjectClasses.ChatListMembers;

public class DanismanimFragment extends Fragment {

    //Layout views
    private ListView mChatListView;
    private FloatingActionButton fab;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String dietitianName;
    private String dietitianPhotoUrl;
    private String dietitianID;
    private Long unreaded_messages;
    private String chatID;
    private String currentUserId;
    private ArrayAdapter<ChatListMembers> mChatAdapter;
    private ArrayList<String> dietitianUserIDs = new ArrayList<>();
    private ArrayList<String> chatIDs = new ArrayList<>();

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatDatabaseReference;
    private DatabaseReference mChatClickedDatabaseReference;
    private DatabaseReference mChatIDReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private ValueEventListener mValueEventListenerForDietitianDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflate the fragment layout
        return inflater.inflate(R.layout.fragment_danismanim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize layout views
        mChatListView = getView().findViewById(R.id.chat_list);
        fab = getView().findViewById(R.id.fab);

        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        editor = preferences.edit();

        // Initialize message ListView and its adapter
        List<ChatListMembers> chat_list_members = new ArrayList<>();
        mChatAdapter = new ChatListAdapter(getContext(), R.layout.chat_list_item_layout, chat_list_members);
        mChatListView.setAdapter(mChatAdapter);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChatIDReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo").child("ChatIDs");

        // set new_message value to false to make disappear new message alert
        //mFirebaseDatabase.getReference().child("AppUsers").child(currentUserId).child("PersonalInfo").child("new_message").setValue(false);

        mChatDatabaseReference = mFirebaseDatabase.getReference().child("Chats");

        attachPersonalDatabaseReadListener();
        initializeFabButtonListeners();
        initializeListviewListener();
    }

    private void initializeListviewListener() {

        registerForContextMenu(mChatListView);

        mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ChatListMembers mychatListMember = (ChatListMembers) mChatListView.getItemAtPosition(i);
                String chatid = mychatListMember.getChatID();
                mChatDatabaseReference.child(chatid).child("advisee_unreaded_messages").setValue(0);

                Intent intent = new Intent(getActivity(), DanismanimChatActivity.class);
                Bundle mBundle = new Bundle();

                mBundle.putSerializable("clickedchatlistmember", mychatListMember);

                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });
    }

    private void initializeFabButtonListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245,166,35)));
                new DanismanimCodeAddDialog().show(getChildFragmentManager(), "Code Adder");
            }
        });
    }

    //it checks chatID changed or not (chatID changes when dietitian code added succesfully)
    private void attachPersonalDatabaseReadListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        dietitianID = postSnapshot.getKey();
                        chatID = postSnapshot.getValue().toString();
                        if (dietitianID != null) {

                            dietitianUserIDs.add(dietitianID);
                            chatIDs.add(chatID);
                            Log.d("bak burda 2 :", dietitianID);

                        }
                    }
                    attachChatDatabaseReadListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mChatIDReference.addValueEventListener(mValueEventListener);
        }
    }

    //TODO
    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mChatDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void attachChatDatabaseReadListener() {
        if (mValueEventListenerForDietitianDatabase == null) {
            mValueEventListenerForDietitianDatabase = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mChatAdapter.clear();

                    for (int i = 0; i < chatIDs.size(); i++) {
                        
                        // clearing global variables to prevent getting previous dietitian info.
                        dietitianPhotoUrl = null;
                        dietitianName = null;
                        dietitianID = null;
                        unreaded_messages = null;
                        
                        String chat_id = chatIDs.get(i);
                        DataSnapshot dataSnapshot1 = dataSnapshot.child(chat_id);

                        for (DataSnapshot postSnapshot : dataSnapshot1.getChildren()) {

                            Log.d("haciii", postSnapshot.getValue().toString());

                            String childrenKey = postSnapshot.getKey();
                            if (childrenKey.equals("dietitian_name")) {
                                dietitianName = (String) postSnapshot.getValue();
                            }
                            if (childrenKey.equals("dietetian_photo_url")) {
                                dietitianPhotoUrl = (String) postSnapshot.getValue();
                            }
                            if (childrenKey.equals("dietitian_id")) {
                                dietitianID = (String) postSnapshot.getValue();
                            }
                            if (childrenKey.equals("advisee_unreaded_messages")) {
                                unreaded_messages = (Long) postSnapshot.getValue();
                            }
                        }

                        ChatListMembers chatListMember = new ChatListMembers(dietitianPhotoUrl,"Dyt. " + dietitianName, chat_id, dietitianID, unreaded_messages);
                        mChatAdapter.add(chatListMember);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mChatDatabaseReference.addValueEventListener(mValueEventListenerForDietitianDatabase);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.chat_list) {
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            ChatListMembers chatListMembers = (ChatListMembers) listView.getItemAtPosition(acmi.position);

            String id = chatListMembers.getChatID();
            mChatClickedDatabaseReference = mChatDatabaseReference.child(id);

            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.menu_layout, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setMessage(getString(R.string.sure_disconnect))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                mChatClickedDatabaseReference.child("connection_status").setValue("passive");
                                Toast.makeText(getActivity(), "İletişiminiz başarıyla koparılmıştır", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

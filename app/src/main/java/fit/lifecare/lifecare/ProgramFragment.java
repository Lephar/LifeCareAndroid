package fit.lifecare.lifecare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fit.lifecare.lifecare.DatabaseClasses.ProgramlarimData;
import fit.lifecare.lifecare.ObjectClasses.ProgramItems;

public class ProgramFragment extends Fragment {


    // Layout Views
    private ListView program_list;

    private ArrayList<ProgramlarimData> mGeneralProgramlarimDataList = new ArrayList<>();
    private ArrayList<ProgramlarimData> mDailyProgramlarimSubDataList = new ArrayList<>();
    private ArrayList<ArrayList<ProgramlarimData>> mDailyProgramlarimDataList = new ArrayList<>();
    private ArrayAdapter<ProgramItems> mProgramItemsAdapter;
    private String currentUserId;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGeneralProgramDatabaseReference;
    private DatabaseReference mDailyProgramDatabaseReference;
    private ValueEventListener mGeneralValueEventListener;
    private ValueEventListener mDailyValueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        // initialize layout views
        program_list = view.findViewById(R.id.program_list);

        // Initialize ListView and its adapter
        List<ProgramItems> program_items = new ArrayList<>();
        mProgramItemsAdapter = new ProgramListAdapter(getContext(), R.layout.program_list_item_layout, program_items);
        program_list.setAdapter(mProgramItemsAdapter);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGeneralProgramDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Programlar").child("Genel");

        mDailyProgramDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Programlar").child("Günlük");

        attachGeneralProgramFirebaseListener();
        attachDailyProgramFirebaseListener();

        initializeListviewListener();

        return view;
    }


    private void attachGeneralProgramFirebaseListener() {

        if (mGeneralValueEventListener == null) {
            mGeneralValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mGeneralProgramlarimDataList.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        ProgramlarimData program_data = postSnapshot.getValue(ProgramlarimData.class);

                        Integer index = mGeneralProgramlarimDataList.size();
                        mGeneralProgramlarimDataList.add(program_data);

                        String program_date = postSnapshot.getKey();
                        String program_name = program_data.getProgram_name();
                        String dietitian_name = program_data.getName();

                        // adding program_item to array adapter
                        ProgramItems program_item = new ProgramItems(program_date, program_name, dietitian_name, "general", index);
                        mProgramItemsAdapter.insert(program_item, 0);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mGeneralProgramDatabaseReference.addValueEventListener(mGeneralValueEventListener);
        }

    }

    private void attachDailyProgramFirebaseListener() {

        if (mDailyValueEventListener == null) {
            mDailyValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mDailyProgramlarimDataList.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String program_date = postSnapshot.getKey();

                        for (DataSnapshot subSnapshot : postSnapshot.getChildren()) {

                            // parsing firebase key to get program_name and dietitian_name
                            String head = subSnapshot.getKey();
                            int index = head.lastIndexOf("*");
                            String program_name = head.substring(0, index);
                            String dietitian_name = head.substring(index + 1);


                            mDailyProgramlarimSubDataList.clear();
                            for (DataSnapshot daySnapshots : subSnapshot.getChildren()) {

                                ProgramlarimData programlarimData = daySnapshots.getValue(ProgramlarimData.class);
                                mDailyProgramlarimSubDataList.add(programlarimData);

                            }
                            Integer index_program = mDailyProgramlarimDataList.size();
                            mDailyProgramlarimDataList.add(mDailyProgramlarimSubDataList);

                            // adding program_item to array adapter
                            ProgramItems program_item = new ProgramItems(program_date, program_name, dietitian_name, "daily",index_program);
                            mProgramItemsAdapter.insert(program_item, 0);

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mDailyProgramDatabaseReference.addValueEventListener(mDailyValueEventListener);
        }

    }

    private void initializeListviewListener() {

        program_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                ProgramItems programItem = (ProgramItems) program_list.getItemAtPosition(i);

                if (programItem.getType().equals("general")) {

                    int index = programItem.getIndex();
                    ProgramlarimData programlarimData = mGeneralProgramlarimDataList.get(index);

                    Intent intent = new Intent(getActivity(), ProgramGeneralActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("ProgramItem", programItem);
                    mBundle.putSerializable("ProgramlarimData", programlarimData);

                    intent.putExtras(mBundle);
                    startActivity(intent);

                } else {

                    int index = programItem.getIndex();
                    ArrayList<ProgramlarimData> programlarimData = mDailyProgramlarimDataList.get(index);

                    Intent intent = new Intent(getActivity(), ProgramDailyActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("ProgramItem", programItem);
                    mBundle.putSerializable("DailyProgramlarimData", programlarimData);

                    intent.putExtras(mBundle);
                    startActivity(intent);

                }

            }
        });

    }
}

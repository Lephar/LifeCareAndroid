package fit.lifecare.lifecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fit.lifecare.lifecare.DatabaseClasses.ProgramlarimData;
import fit.lifecare.lifecare.ObjectClasses.ProgramItems;

public class ProgramFragment extends Fragment {
    
    
    // Layout Views
    private ListView program_list;
    
    private ArrayList<ProgramlarimData> mGeneralProgramlarimDataList = new ArrayList<>();
    private ArrayList<ProgramlarimData> mDailyProgramlarimDataList = new ArrayList<>();
    private ArrayAdapter<ProgramItems> mProgramItemsAdapter;
    private String currentUserId;
    
    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProgramDatabaseReference;
    private ValueEventListener mValueEventListener;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        //inflate the fragment layout
        return inflater.inflate(R.layout.fragment_program, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize layout views
        program_list = getView().findViewById(R.id.program_list);

        // Initialize ListView and its adapter
        List<ProgramItems> program_items = new ArrayList<>();
        mProgramItemsAdapter = new ProgramListAdapter(getContext(), R.layout.program_list_item_layout, program_items);
        program_list.setAdapter(mProgramItemsAdapter);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // set new_message value to false to make disappear new message alert
        //mFirebaseDatabase.getReference().child("AppUsers").child(currentUserId).child("PersonalInfo").child("new_meal_schedule").setValue(false);

        mProgramDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Programlar");

        attachProgramFirebaseListener();

        initializeListviewListener();
    }

    private void attachProgramFirebaseListener() {
        
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    
                    mGeneralProgramlarimDataList.clear();
                    mDailyProgramlarimDataList.clear();
                    mProgramItemsAdapter.clear();
                    
                    for (DataSnapshot dataSnapshotDailyGeneral : dataSnapshot.getChildren()) {
                        
                        if (dataSnapshotDailyGeneral.getKey().equals("Genel")) {
                            
                            for (DataSnapshot postSnapshot : dataSnapshotDailyGeneral.getChildren()) {
                                
                                String program_date = postSnapshot.getKey();
                                
                                String[] date_array = program_date.split("-");
                                String str_date1 = date_array[0] + "-" + date_array[1] + "-" + date_array[2].trim();
                                String str_date2 = date_array[3].trim() + "-" + date_array[4] + "-" + date_array[5];
                                
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = format.parse(str_date1);
                                    String newstring1 = new SimpleDateFormat("dd-MM-yyyy").format(date1);
                                    
                                    Date date2 = format.parse(str_date2);
                                    String newstring2 = new SimpleDateFormat("dd-MM-yyyy").format(date2);
                                    
                                    program_date = newstring1 + " / " + newstring2;
                                } catch (ParseException e) {
                                    //handle exception
                                }
                                
                                
                                ProgramlarimData program_data = new ProgramlarimData();
                                
                                for (DataSnapshot pSnapshot : postSnapshot.getChildren()) {
                                    
                                    String key_name = pSnapshot.getKey();
                                    
                                    if (key_name.equals("aksamYemegi")) {
                                        ArrayList<String> aksamyemegi = new ArrayList<>();
                                        for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                            aksamyemegi.add(p2Snapshot.getValue().toString());
                                        }
                                        program_data.setAksamYemegi(aksamyemegi);
                                    } else if (key_name.equals("aksamYemegiCalory")) {
                                        program_data.setAksamYemegiCalory(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("alternatif")) {
                                        ArrayList<String> alternatif = new ArrayList<>();
                                        for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                            alternatif.add(p2Snapshot.getValue().toString());
                                        }
                                        program_data.setAlternatif(alternatif);
                                    } else if (key_name.equals("alternatifCalory")) {
                                        program_data.setAlternatifCalory(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("birinciAraOgun")) {
                                        ArrayList<String> birinciAraOgun = new ArrayList<>();
                                        for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                            birinciAraOgun.add(p2Snapshot.getValue().toString());
                                        }
                                        program_data.setBirinciAraOgun(birinciAraOgun);
                                    } else if (key_name.equals("birinciAraOgunCalory")) {
                                        program_data.setBirinciAraOgunCalory(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("ikinciAraOgun")) {
                                        ArrayList<String> ikinciAraOgun = new ArrayList<>();
                                        for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                            ikinciAraOgun.add(p2Snapshot.getValue().toString());
                                        }
                                        program_data.setIkinciAraOgun(ikinciAraOgun);
                                    } else if (key_name.equals("ikinciAraOgunCalory")) {
                                        program_data.setIkinciAraOgunCalory(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("ogleYemegi")) {
                                        ArrayList<String> ogleYemegi = new ArrayList<>();
                                        for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                            ogleYemegi.add(p2Snapshot.getValue().toString());
                                        }
                                        program_data.setOgleYemegi(ogleYemegi);
                                    } else if (key_name.equals("ogleYemegiCalory")) {
                                        program_data.setOgleYemegiCalory(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("sabahKahvaltisi")) {
                                        ArrayList<String> sabahKahvaltisi = new ArrayList<>();
                                        for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                            sabahKahvaltisi.add(p2Snapshot.getValue().toString());
                                        }
                                        program_data.setSabahKahvaltisi(sabahKahvaltisi);
                                    } else if (key_name.equals("sabahKahvaltisiCalory")) {
                                        program_data.setSabahKahvaltisiCalory(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("ucuncuAraOgun")) {
                                        ArrayList<String> ucuncuAraOgun = new ArrayList<>();
                                        for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                            ucuncuAraOgun.add(p2Snapshot.getValue().toString());
                                        }
                                        program_data.setUcuncuAraOgun(ucuncuAraOgun);
                                    } else if (key_name.equals("ucuncuAraOgunCalory")) {
                                        program_data.setUcuncuAraOgunCalory(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("name")) {
                                        program_data.setName(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("program_name")) {
                                        program_data.setProgram_name(pSnapshot.getValue().toString());
                                    } else if (key_name.equals("toplamCalory")) {
                                        program_data.setToplamCalory(pSnapshot.getValue().toString());
                                    }
                                    
                                    
                                }
                                
                                mGeneralProgramlarimDataList.add(program_data);
                                
                                String program_name = program_data.getProgram_name();
                                String dietitian_name = program_data.getName();
                                
                                // adding program_item to array adapter
                                ProgramItems program_item = new ProgramItems(program_date, program_name, dietitian_name, "general", program_data, null);
                                mProgramItemsAdapter.insert(program_item, 0);
                                
                            }
                        } else if (dataSnapshotDailyGeneral.getKey().equals("Günlük")) {
                            
                            for (DataSnapshot postSnapshot : dataSnapshotDailyGeneral.getChildren()) {
                                
                                String program_date = postSnapshot.getKey();
    
                                String[] date_array = program_date.split("-");
                                String str_date1 = date_array[0] + "-" + date_array[1] + "-" + date_array[2].trim();
                                String str_date2 = date_array[3].trim() + "-" + date_array[4] + "-" + date_array[5];
    
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = format.parse(str_date1);
                                    String newstring1 = new SimpleDateFormat("dd-MM-yyyy").format(date1);
        
                                    Date date2 = format.parse(str_date2);
                                    String newstring2 = new SimpleDateFormat("dd-MM-yyyy").format(date2);
        
                                    program_date = newstring1 + " / " + newstring2;
                                } catch (ParseException e) {
                                    //handle exception
                                }
                                
                                for (DataSnapshot subSnapshot : postSnapshot.getChildren()) {
                                    
                                    // parsing firebase key to get program_name and dietitian_name
                                    String head = subSnapshot.getKey();
                                    int index = head.lastIndexOf("*");
                                    String program_name = head.substring(0, index);
                                    String dietitian_name = head.substring(index + 1);
                                    
                                    mDailyProgramlarimDataList.clear();
                                    
                                    for (DataSnapshot daySnapshots : subSnapshot.getChildren()) {
                                        
                                        ProgramlarimData program_data = new ProgramlarimData();
                                        
                                        for (DataSnapshot pSnapshot : daySnapshots.getChildren()) {
                                            
                                            String key_name = pSnapshot.getKey();
                                            
                                            if (key_name.equals("aksamYemegi")) {
                                                ArrayList<String> aksamyemegi = new ArrayList<>();
                                                for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                                    aksamyemegi.add(p2Snapshot.getValue().toString());
                                                }
                                                program_data.setAksamYemegi(aksamyemegi);
                                            } else if (key_name.equals("aksamYemegiCalory")) {
                                                program_data.setAksamYemegiCalory(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("alternatif")) {
                                                ArrayList<String> alternatif = new ArrayList<>();
                                                for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                                    alternatif.add(p2Snapshot.getValue().toString());
                                                }
                                                program_data.setAlternatif(alternatif);
                                            } else if (key_name.equals("alternatifCalory")) {
                                                program_data.setAlternatifCalory(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("birinciAraOgun")) {
                                                ArrayList<String> birinciAraOgun = new ArrayList<>();
                                                for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                                    birinciAraOgun.add(p2Snapshot.getValue().toString());
                                                }
                                                program_data.setBirinciAraOgun(birinciAraOgun);
                                            } else if (key_name.equals("birinciAraOgunCalory")) {
                                                program_data.setBirinciAraOgunCalory(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("ikinciAraOgun")) {
                                                ArrayList<String> ikinciAraOgun = new ArrayList<>();
                                                for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                                    ikinciAraOgun.add(p2Snapshot.getValue().toString());
                                                }
                                                program_data.setIkinciAraOgun(ikinciAraOgun);
                                            } else if (key_name.equals("ikinciAraOgunCalory")) {
                                                program_data.setIkinciAraOgunCalory(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("ogleYemegi")) {
                                                ArrayList<String> ogleYemegi = new ArrayList<>();
                                                for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                                    ogleYemegi.add(p2Snapshot.getValue().toString());
                                                }
                                                program_data.setOgleYemegi(ogleYemegi);
                                            } else if (key_name.equals("ogleYemegiCalory")) {
                                                program_data.setOgleYemegiCalory(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("sabahKahvaltisi")) {
                                                ArrayList<String> sabahKahvaltisi = new ArrayList<>();
                                                for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                                    sabahKahvaltisi.add(p2Snapshot.getValue().toString());
                                                }
                                                program_data.setSabahKahvaltisi(sabahKahvaltisi);
                                            } else if (key_name.equals("sabahKahvaltisiCalory")) {
                                                program_data.setSabahKahvaltisiCalory(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("ucuncuAraOgun")) {
                                                ArrayList<String> ucuncuAraOgun = new ArrayList<>();
                                                for (DataSnapshot p2Snapshot : pSnapshot.getChildren()) {
                                                    ucuncuAraOgun.add(p2Snapshot.getValue().toString());
                                                }
                                                program_data.setUcuncuAraOgun(ucuncuAraOgun);
                                            } else if (key_name.equals("ucuncuAraOgunCalory")) {
                                                program_data.setUcuncuAraOgunCalory(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("name")) {
                                                program_data.setName(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("program_name")) {
                                                program_data.setProgram_name(pSnapshot.getValue().toString());
                                            } else if (key_name.equals("toplamCalory")) {
                                                program_data.setToplamCalory(pSnapshot.getValue().toString());
                                            }
                                            
                                        }
                                        
                                        mDailyProgramlarimDataList.add(program_data);
                                    }
    
    
                                    
                                    
                                    ProgramlarimData[] pd = mDailyProgramlarimDataList.toArray(new ProgramlarimData[mDailyProgramlarimDataList.size()]);
                                    
                                    // adding program_item to array adapter
                                    ProgramItems program_item = new ProgramItems(program_date, program_name, dietitian_name, "daily",null, pd);
                                    mProgramItemsAdapter.insert(program_item, 0);
    
                                    mDailyProgramlarimDataList.clear();
                                    
                                }
                            }
                            
                        }
                        
                    }
                    
                    mProgramItemsAdapter.sort(new Comparator<ProgramItems>() {
                        @Override
                        public int compare(ProgramItems o1, ProgramItems o2) {
                            String str_date1 = o1.getDate();
                            String str_date2 = o2.getDate();
                            
                            String[] date1_array = str_date1.split("/");
                            str_date1 = date1_array[0].trim();
                            String[] date2_array = str_date2.split("/");
                            str_date2 = date2_array[0].trim();
    
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                Date date1 = format.parse(str_date1);
                                str_date1 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
        
                                Date date2 = format.parse(str_date2);
                                str_date2 = new SimpleDateFormat("yyyy-MM-dd").format(date2);
        
                            } catch (ParseException e) {
                                //handle exception
                            }
                            
                            return str_date2.compareTo(str_date1);
                            
                        }
                    });
                    
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                
                }
            };
            mProgramDatabaseReference.addValueEventListener(mValueEventListener);
        }
    }
    
    private void initializeListviewListener() {
        
        program_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                
                
                ProgramItems programItem = (ProgramItems) program_list.getItemAtPosition(i);
                
                if (programItem.getType().equals("general")) {
    
                    ProgramlarimData programlarimData = programItem.getProgram_data();
                    
                    Intent intent = new Intent(getActivity(), ProgramGeneralActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("ProgramItem", programItem);
                    mBundle.putSerializable("ProgramlarimData", programlarimData);
                    
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    
                } else {
                    
                    ProgramlarimData[] programlarimData = programItem.getProgram_data_daily();
                    
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

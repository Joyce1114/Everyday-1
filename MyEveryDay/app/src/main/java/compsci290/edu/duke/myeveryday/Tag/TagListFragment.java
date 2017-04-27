package compsci290.edu.duke.myeveryday.Tag;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import compsci290.edu.duke.myeveryday.Listeners.TagSelectedListener;
import compsci290.edu.duke.myeveryday.Models.JournalEntry;
import compsci290.edu.duke.myeveryday.Models.Tag;
import compsci290.edu.duke.myeveryday.R;
import compsci290.edu.duke.myeveryday.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagListFragment extends Fragment implements TagSelectedListener {
    private List<JournalEntry> mjournals;
    private List<Tag> mtags;
    private ListAdapter mAdapter;
    private View mrootview;

    @BindView(R.id.tag_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_text)
    TextView mEmptytext;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mdatabase;
    private DatabaseReference mcloudReference;
    private DatabaseReference mTagCloudReference;




    public TagListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mrootview =  inflater.inflate(R.layout.fragment_tag_list, container, false);
        ButterKnife.bind(this, mrootview);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        mcloudReference = mdatabase.child(Constants.USERS_CLOUD_END_POINT + mFirebaseUser.getUid() + Constants.NOTE_CLOUD_END_POINT);
        mTagCloudReference = mdatabase.child(Constants.USERS_CLOUD_END_POINT + mFirebaseUser.getUid() + Constants.CATEGORY_CLOUD_END_POINT);

        mjournals = new ArrayList<>();
        mtags = new ArrayList<>();

        mcloudReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot journalsnapshot: dataSnapshot.getChildren()){
                    JournalEntry journal = journalsnapshot.getValue(JournalEntry.class);
                    mjournals.add(journal);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mTagCloudReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadtags(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new ListAdapter(mtags, getContext(),this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        return mrootview;


    }

    private void loadtags(DataSnapshot dataSnapshot){
        if(dataSnapshot != null) {
            mtags.clear();
            for (DataSnapshot tagsnapshot : dataSnapshot.getChildren()) {
                Tag mtag = null;
                try {
                    mtag = tagsnapshot.getValue(Tag.class);
                    mtags.add(mtag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(mtags.size() > 0)
        {
            hideemptytext();
            for(Tag tag: mtags)
            {
                tag.setJournalcount(getjournalcount(tag.getmTagID()));
            }
            showtags(mtags);
        }
        else
        {
            showemptytext();
        }
    }

    private void showtags(List<Tag> mtags) {
        mAdapter.replaceData(mtags);

    }

    public void hideemptytext()
    {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptytext.setVisibility(View.GONE);
    }

    public void showemptytext()
    {
        mRecyclerView.setVisibility(View.GONE);
        mEmptytext.setVisibility(View.VISIBLE);
    }

    public int getjournalcount(String tagId)
    {
        int count = 0;
        for(JournalEntry journal: mjournals)
        {
            if(!TextUtils.isEmpty(journal.getmTagID()))
            {
                if(journal.getmTagID().equals(tagId))
                {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void onTagSelected(Tag TagSelected) {

    }

    @Override
    public void onEditButtionClicked(Tag TagSelected) {

    }

    @Override
    public void onDeleteButtionBlicked(Tag TagSelected) {

    }
}

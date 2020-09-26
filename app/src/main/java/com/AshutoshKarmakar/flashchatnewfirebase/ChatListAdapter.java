package com.AshutoshKarmakar.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotlist;
    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d("FlashChat", "onChildAdded: is called");
            mSnapshotlist.add(dataSnapshot);
            notifyDataSetChanged();



        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            Log.d("FlashChat", "onChildChanged: is called" + mSnapshotlist.toString());


        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity,DatabaseReference refs,String name){
        mActivity = activity;
        mDatabaseReference = refs.child("messages");
        mDisplayName = name;
        mDatabaseReference.addChildEventListener(mListener);
        mSnapshotlist = new ArrayList<>();
    }
    static class ViewHolder
    {
        TextView authorname;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {//denotes the number of list present in the list
        return mSnapshotlist.size();
    }

    @Override//list view ask the adapter by giving the position
    public InstanceMessage getItem(int position) {//provides the item according to the position
        DataSnapshot snapshot = mSnapshotlist.get(position);
        return snapshot.getValue(InstanceMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//covertView denotes the list items
        //if convertView is null then we create a new list or else modify the data inside it
        if(convertView == null){
            // to creat a view from xml file we need inflater:-
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row,parent,false);
            final ViewHolder holder = new ViewHolder();
            holder.authorname = (TextView) convertView.findViewById(R.id.author);
            holder.body = (TextView) convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorname.getLayoutParams();
            convertView.setTag(holder);

        }

        final InstanceMessage message = getItem(position);
        Log.d("FlashChat", "getView: position"+ position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        boolean isitMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isitMe,holder);

        String author = message.getAuthor();
        holder.authorname.setText(author);
        Log.d("FlashChat", "getView:1 "+ author);
        String msg = message.getMessage();
        holder.body.setText(msg);
        Log.d("FlashChat", "getView:2 "+ msg);

        return convertView;
    }
    private void setChatRowAppearance(boolean isItMe , ViewHolder holder){
        if(isItMe){
            holder.params.gravity = Gravity.END;
            holder.authorname.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }
        else{
            holder.params.gravity = Gravity.START;
            holder.authorname.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }
        holder.authorname.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);


    }


    public void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }
}

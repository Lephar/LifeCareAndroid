package fit.lifecare.lifecare;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import fit.lifecare.lifecare.ObjectClasses.ChatListMembers;

public class ChatListAdapter extends ArrayAdapter {

    public ChatListAdapter(Context context, int resource, List<ChatListMembers> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatListMembers Dietitian = (ChatListMembers) getItem(position);

        String name = Dietitian.getdietitianName();
        String photoUrl = Dietitian.getdietitianPhotoUrl();
        Long unreaded_messages = Dietitian.getUnreaded_messages();

        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.chat_list_item_layout, parent, false);
        ImageView adviseePhoto = convertView.findViewById(R.id.advisee_photo);
        TextView adviseeName = convertView.findViewById(R.id.advisee_name);
        TextView unreadedMessages = convertView.findViewById(R.id.message_count);
        ImageView circle = convertView.findViewById(R.id.circle);

        Glide.with(adviseePhoto.getContext())
                .load(photoUrl)
                .into(adviseePhoto);

        adviseeName.setText(name);
        if (unreaded_messages != null) {
            unreadedMessages.setText(unreaded_messages.toString());
            if (unreaded_messages.equals((long) 0)) {
                circle.setVisibility(View.GONE);
                unreadedMessages.setVisibility(View.GONE);
            } else {
                circle.setVisibility(View.VISIBLE);
                unreadedMessages.setVisibility(View.VISIBLE);
            }
        }


        return convertView;
    }

}

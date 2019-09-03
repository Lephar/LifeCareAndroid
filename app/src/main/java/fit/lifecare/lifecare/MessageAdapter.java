package fit.lifecare.lifecare;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fit.lifecare.lifecare.ObjectClasses.Messages;


public class MessageAdapter extends ArrayAdapter {

    public MessageAdapter(Context context, int resource, List<Messages> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Messages message = (Messages) getItem(position);

        if (message.isDietitianMessage()) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.dietitian_messages, parent, false);
        } else {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.app_user_messages, parent, false);
        }

        ConstraintLayout image_frame = convertView.findViewById(R.id.image_frame);
        ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
        LinearLayout messageTextView = convertView.findViewById(R.id.messageTextView);
        TextView messageText = convertView.findViewById(R.id.messageText);
        TextView messageClock = convertView.findViewById(R.id.messageClock);
        TextView img_messageClock = convertView.findViewById(R.id.img_messageClock);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            image_frame.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);

            DateFormat df = new SimpleDateFormat("HH:mm d/M/yy");
            Date date = new Date(message.getMessageTime());
            String messageTime = df.format(date);
            img_messageClock.setText(messageTime);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            image_frame.setVisibility(View.GONE);
            messageText.setText(message.getMessageText());

            DateFormat df = new SimpleDateFormat("HH:mm d/M/yy");
            Date date = new Date(message.getMessageTime());
            String messageTime = df.format(date);
            messageClock.setText(messageTime);
        }

        return convertView;
    }

}

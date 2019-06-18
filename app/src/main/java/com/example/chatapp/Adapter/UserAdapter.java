package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.MessageActivity;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;

import org.w3c.dom.Text;

import java.util.List;

// We use RecyclerView because we are dealing with a scrolling list of elements based on large data sets.
// RecycleView widget is more advanced and flexible version of ListView.
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    // We make two variables one is context and the other one is a list of users.
    private Context mContext;
    private List<User> mUsers;


    public UserAdapter(Context mContext, List<User> mUsers){
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    // Each viewholder is in charge of displaying a single item with a view.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // Instantiates a layout XML file into its corresponding View objects.
        // From = Obtains the Layoutinflater from a given context.
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    // onBindViewHolder is called by RecyclerView to display the data at the specified position.
    // This method should update the contents of the itemView to reflect the item at the given position.
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Here we get the position of the user.
        final User user = mUsers.get(position);
        // Here we set the username.
        holder.username.setText(user.getUsername());

        // if the user does not have uploaded a photo, we will display the default image.
        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            // else we will display the profile the user have uploaded.
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }

        // When the user clicks on the itemview they will be moved to the MessageActivity.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // The size of the list will be the same size of the users list.
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);


        }
    }
}

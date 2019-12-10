package android.project.booksapp.Activities;

import android.content.Context;
import android.project.booksapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder>{

    private Context mContext;
    private List<CreateAccountModel> mAccount;
    private List <CreateAccountModel> SearchAccountList;
    private OnItemClickListner mListner;

    public AccountAdapter(Context mContext, List<CreateAccountModel> mAccount){

        this.mContext = mContext;
        this.mAccount = mAccount;
        SearchAccountList = new ArrayList<>(mAccount);
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.account_list_layout, viewGroup, false);
        return new AccountAdapter.AccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder accountViewHolder, int i) {
        
        CreateAccountModel accountCurrnt = mAccount.get(i);

        accountViewHolder.cname.setText(accountCurrnt.getName());
        accountViewHolder.cusername.setText(accountCurrnt.getUsername());
        accountViewHolder.cemail.setText(accountCurrnt.getEmail());
        //Picasso.with(mContext).load(accountCurrnt.getProfile()).fit().centerInside().into(accountViewHolder.imageView);
        Picasso.with(mContext).load(accountCurrnt.getProfile()).fit().centerInside().into(accountViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mAccount.size();
    }


    public class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView cname, cemail, cusername;
        public CircleImageView imageView;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);

            cname = itemView.findViewById(R.id.cname_z);
            cemail = itemView.findViewById(R.id.cemail_z);
            cusername = itemView.findViewById(R.id.cusername_z);
            imageView = itemView.findViewById(R.id.ImageView_uploads);


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListner != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListner.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Option");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");

            Delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListner != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){

                        case 2:
                            mListner.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListner {

        void onItemClick (int position);
        void onDeleteClick (int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){

        mListner = listner;

    }

}

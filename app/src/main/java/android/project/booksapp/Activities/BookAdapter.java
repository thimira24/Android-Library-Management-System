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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context mContext;
    private List<Book> mBooks;
    private List<Book> SearchBookList;
    private OnItemClickListner mListner;




    public BookAdapter(Context mContext, List<Book> mBooks) {
        this.mContext = mContext;
        this.mBooks = mBooks;
        SearchBookList = new ArrayList<>(mBooks);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.book_list_layout, viewGroup, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder,  int i) {

        Book bookCurrnt = mBooks.get(i);

        bookViewHolder.BookViewName.setText(bookCurrnt.getBookname());
        bookViewHolder.BookAuthor.setText(bookCurrnt.getBookauthor());
        bookViewHolder.BookLocation.setText(bookCurrnt.booklocation);
        Picasso.with(mContext).load(bookCurrnt.getmImageUrl()).fit().centerInside().into(bookViewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public Filter getFilter(){
        return examplefilter;
    }

    public Filter getAuthorFilter(){

        return examplefilterauthor;
    }

    public Filter getLocationFilter(){

      return examplefilterlocation;
    }

    private Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(SearchBookList);
            } else {
                String filterPatern = constraint.toString().toLowerCase().trim();
                for (Book item : SearchBookList){
                    if (item.getBookname().toLowerCase().contains(filterPatern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBooks.clear();
            mBooks.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    // search author
    private Filter examplefilterauthor = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(SearchBookList);
            } else {
                String filterPatern = constraint.toString().toLowerCase().trim();
                for (Book item : SearchBookList){
                    if (item.getBookauthor().toLowerCase().contains(filterPatern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBooks.clear();
            mBooks.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    //search location

    private Filter examplefilterlocation = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(SearchBookList);
            } else {
                String filterPatern = constraint.toString().toLowerCase().trim();
                for (Book item : SearchBookList){
                    if (item.getBooklocation() .toLowerCase().contains(filterPatern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBooks.clear();
            mBooks.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };


    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView BookViewName, BookAuthor, BookLocation;
        public ImageView imageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            BookViewName = itemView.findViewById(R.id.book_name_id);
            BookAuthor = itemView.findViewById(R.id.book_isbn_id);
            BookLocation = itemView.findViewById(R.id.book_name_location_id);
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
            menu.setHeaderTitle("Options");
            MenuItem BookEdit = menu.add(Menu.NONE, 1,1, "Edit");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");

            BookEdit.setOnMenuItemClickListener(this);
            Delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListner != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                   switch (item.getItemId()){
                       case 1:
                           mListner.onEditClick(position);
                           return true;
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
        void onEditClick (int position);
        void onDeleteClick (int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner = listner;
    }


}

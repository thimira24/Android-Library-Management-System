package android.project.booksapp.Activities;

import com.google.firebase.database.Exclude;

public class Book {

    public String bookname, bookauthor, bookisbn, bookdate, bookcopies, bookcategory, booklocation, bookedition, mImageUrl;
    private String mKey;

    public Book() {

        // an empty constructor needed
    }

    public Book(String bookname, String bookauthor, String bookisbn, String bookdate, String bookcopies, String bookcategory, String booklocation, String bookedition, String mImageUrl) {

        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.bookisbn = bookisbn;
        this.bookdate = bookdate;
        this.bookcopies = bookcopies;
        this.bookcategory = bookcategory;
        this.booklocation = booklocation;
        this.bookedition = bookedition;
        this.mImageUrl = mImageUrl;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookisbn() {
        return bookisbn;
    }

    public void setBookisbn(String bookisbn) {
        this.bookisbn = bookisbn;
    }

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    public String getBookcopies() {
        return bookcopies;
    }

    public void setBookcopies(String bookcopies) {
        this.bookcopies = bookcopies;
    }

    public String getBookcategory() {
        return bookcategory;
    }

    public void setBookcategory(String bookcategory) {
        this.bookcategory = bookcategory;
    }

    public String getBooklocation() {
        return booklocation;
    }

    public void setBooklocation(String booklocation) {
        this.booklocation = booklocation;
    }

    public String getBookedition() {
        return bookedition;
    }

    public void setBookedition(String bookedition) {
        this.bookedition = bookedition;
    }

    // image getter and setter

    public String getmImageUrl()  {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    // mkey getter and setter

    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String Key) {
        this.mKey = Key;
    }
}

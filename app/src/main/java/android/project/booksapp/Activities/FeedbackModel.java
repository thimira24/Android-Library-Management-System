package android.project.booksapp.Activities;

public class FeedbackModel {

    public String title, message;

    public FeedbackModel() {
    }

    public FeedbackModel(String title, String message) {

        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

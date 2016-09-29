package self.harmony.bashrandomh;

public class Quote {
    private int rating;
    private String text;
    private String id;
    private String date;

    public Quote(int rating, String text, String id, String date) {
        this.rating = rating;
        this.text = text;
        this.id = id;
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

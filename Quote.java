package self.harmony.bashrandomh;

public class Quote {
    private int rating;
    private String text;

    public Quote(int rating, String text) {
        this.rating = rating;
        this.text = text;
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

    public boolean theSameAs(Quote input) {
        if (input.getText().equals(text) && input.getRating() == rating){
            return true;
        }
        return false;
    }
}

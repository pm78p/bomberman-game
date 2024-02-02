public class Massage {

    private String name;
    private String text;
    private double id ;

    public Massage(String name, String text, Double id) {
        this.id = id ;
        this.name = name;
        this.text = text;
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.name + " " + this.text + " \n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }
}

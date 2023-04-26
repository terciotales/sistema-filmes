package sicof.model;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Filme {
    private int id;
    private String title;
    private Date releaseDate;
    private Categoria category;
    private ArrayList<Ator> actors = new ArrayList<>();

    public Filme() {
    }

    public Filme(int id, String title, Date releaseDate, Categoria category, ArrayList<Ator> actors) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
        this.actors = actors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseDateFormatted() {
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(String.valueOf(releaseDate), new ParsePosition(0));
        sdf.applyPattern("dd/MM/yyyy");
        newDateString = sdf.format(d);
        return newDateString;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Categoria getCategory() {
        return category;
    }

    public void setCategory(Categoria category) {
        this.category = category;
    }

    public ArrayList<Ator> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Ator> actors) {
        this.actors = actors;
    }

    public void addActor(Ator actor) {
        this.actors.add(actor);
    }

    public void removeActor(Ator actor) {
        this.actors.remove(actor);
    }
}

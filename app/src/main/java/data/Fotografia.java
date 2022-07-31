package data;

public class Fotografia {
    private String nomeFoto;
    private String urlFoto;

    public Fotografia() {
    }

    public Fotografia(String nomeFoto, String urlFoto) {
        this.nomeFoto = nomeFoto;
        this.urlFoto = urlFoto;
    }

    public String getNomeFoto() {
        return nomeFoto;
    }

    public void setNomeFoto(String nomeFoto) {
        this.nomeFoto = nomeFoto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}

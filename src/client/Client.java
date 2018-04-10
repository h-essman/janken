package client;

public class Client {

    private String pseudo, state, commande;
    private int id;

    public Client(){

    }

    public boolean connexion(String host, int port, String pseudo){
        try{
            new Thread(new ThreadCient(host,port,this)).start();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    

    public String getPseudo() {
        return this.pseudo;
    }
    public void setPseudo(String pseudo){ this.pseudo=pseudo; }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommande() {
        return commande;
    }

    public void setCommande(String commande) {
        this.commande = commande;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

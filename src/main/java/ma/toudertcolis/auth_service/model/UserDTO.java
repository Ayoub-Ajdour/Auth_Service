package ma.toudertcolis.auth_service.model;

public class UserDTO {

    private String id;
    private String nomComplet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    private String telephone;
    private String email;
    private String cin;
    private String ville;
    private String password; // Add this field if needed

    // Constructors, Getters, and Setters
    public UserDTO(String id, String nomComplet, String telephone, String email, String cin, String ville, String password) {
        this.id = id;
        this.nomComplet = nomComplet;
        this.telephone = telephone;
        this.email = email;
        this.cin = cin;
        this.ville = ville;
        this.password = password; // Initialize password
    }

    // Getters and Setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Other Getters and Setters...
}

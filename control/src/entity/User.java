package entity;

public class User {
    private String username;
    private String password;
    private boolean isAdmin; // true管理员 false普通用户
    private String major;    // 用户专业

    public User(String username, String password, boolean isAdmin, String major) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.major = major;
    }

    // getter setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
}

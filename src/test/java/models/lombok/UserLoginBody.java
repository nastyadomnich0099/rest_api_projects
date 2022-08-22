package models.lombok;


import lombok.Data;

@Data
public class UserLoginBody {
    private String email;
    private String password;
}
package np.com.oskarshrestha.springsecurityclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matchingPassword;

    /*
     * TODO: add logic to validate password and matchingPassword before saving the data.
     */
}

package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;

public interface UserService {
    public User registerUser(UserRegisterRequest userRegisterRequest);
}

package np.com.oskarshrestha;

import org.springframework.stereotype.Component;

@Component
public class ShoppingCart {
    public void checkout(String status){
        //logging
        //authentication & authorization
        //sanitize the data
        System.out.println("Checkout method from ShoppingCart called");
    }

    public int quantity(){
        return 2;
    }
}

package customer.upskilling.helper;

import com.sap.cloud.sdk.cloudplatform.connectivity.DefaultHttpDestination;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import org.springframework.util.Assert;
import com.sap.cds.services.request.UserInfo;

public class ConnectivityHelper {

    public static HttpDestination getDestination(String destinationName,UserInfo userinfo) {
        Assert.isTrue(userinfo.isAuthenticated(), "The user is not authenticated");
        
        Destination dest = DestinationAccessor.getDestination(getDestinationName(destinationName));
        return dest.asHttp();
    }
    
    public static HttpDestination getDest(String destinationName,UserInfo userinfo) {
        Assert.isTrue(userinfo.isAuthenticated(), "The user is not authenticated");
        HttpDestination dest = DestinationAccessor.getDestination(getDestinationName(destinationName)).asHttp();
        return dest;
    }

    private static String getDestinationName(String destinationName) {
        return System.getenv(destinationName);
    }
}

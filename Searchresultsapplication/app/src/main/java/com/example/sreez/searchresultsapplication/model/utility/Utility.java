package com.example.sreez.searchresultsapplication.model.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by sreez on 4/30/2016.
 */
public class Utility {
    private static String apikey = "AIzaSyB0JpvFK0DJWLQtZHFas_hjLit2RmvXFsg"; //apikey="AIzaSyA3OWCKNTwRN_ILiBx2gFeJ5U7sCNC67vw";

    private static Utility utility = new Utility();

    public static Utility getInstance()
    {
        return  utility;
    }

   public  String getSearchPlacesUrl(String query)
   {
       String searchPlacesUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+query+"&key="+apikey;
       return searchPlacesUrl;
   }
    public String getSearchDetailsUrl(String placeid)
    {
        String searchDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeid+"&key="+apikey;
        return searchDetailsUrl;
    }

    public boolean checkStringIsNullOrEmpty(String query)
    {
        if(query!=null && !query.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

//   public boolean isNetworkAvailable() {
//
//       ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//        boolean isAvailable = false;
//        if(networkInfo!=null && networkInfo.isConnected())
//        {
//            isAvailable = true;
//        }
//
//        return isAvailable;
//    }

}

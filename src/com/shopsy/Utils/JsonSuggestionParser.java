package com.shopsy.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.shopsy.Iconstant.Iconstant;
import com.shopsy.bean.SuggestGetSet;

public class JsonSuggestionParser 
{
	
	    public List<SuggestGetSet> getParseJsonWCF(String sName, String seller_id)
	    {
	        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
	        try 
	        {
	           String temp=sName.replace(" ", "%20");
	           
	           System.out.println("------------search editior sellerId----------"+seller_id);
	           System.out.println("------------search editior value----------"+temp);
	           Log.d("SEarch_Url-------->", "" + Iconstant.youritem_search_url+temp+"&sellerId="+seller_id);
	           
	           URL js = new URL(Iconstant.youritem_search_url+temp+"&sellerId="+seller_id);
	           URLConnection jc = js.openConnection();
	           jc.setRequestProperty("User-Agent", "shopsymobileapp");
	           BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
	           String line = reader.readLine();
	           JSONObject jsonResponse = new JSONObject(line);
	           JSONArray jsonArray = jsonResponse.getJSONArray("Listings");
	           for(int i = 0; i < jsonArray.length(); i++)
	           {
	               JSONObject r = jsonArray.getJSONObject(i);
	               ListData.add(new SuggestGetSet(r.getString("productName"), r.getString("productId")));
	           }
	       } catch (Exception e1) {
	           // TODO Auto-generated catch block
	           e1.printStackTrace();
	       }
	        return ListData;
	       }

}

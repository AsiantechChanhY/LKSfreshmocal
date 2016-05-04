package com.shopsy.Iconstant;

public interface Iconstant 
{
	
	//String Baseurl="http://192.168.1.251:8081/jayaprakash/shopsy-app/";
	//String Baseurl="http://192.168.1.253/sivaprakash/shopsy-package/responsive/";
	//String Baseurl="http://www.aarinshop.com/";

	//String Baseurl="http://192.168.1.253/shopsy-v2/";

	String Baseurl="http://etsyclone.zoplay.com/";

	//String Baseurl="http://www.zpjo.com/";
	//String Baseurl="http://quickiz.com/deliverio/";
	
	
	
	public static String loginurl = Baseurl+"json/user/login?commonId=";
	public static String registerurl = Baseurl+"json/user/register?commonId=";
	public static String forgotpasswordurl = Baseurl+"json/forgot_password";
	public static String terms_and_conditions = "http://etsyclone.zoplay.com/pages/privacy-policy";
	public static String trending_url = Baseurl+"json/trending?pageId=";
	public static String productimageurl = Baseurl+"images/product/";
	public static String cartcounturl = Baseurl+"json/cartCount?commonId=";
	
	public static String yourfeedpageurl = Baseurl+"json/feed/";
	public static String yourfeeduserimageurl = Baseurl+"";

	public static String categoriespageurl = Baseurl+"json/category?commonId=";
	public static String subcategoriesurl = Baseurl+"json/category?catId=";
	
	public static String subcategories_producturl = Baseurl+"json/product/pagecount?catid=";
	public static String subcategories_filterurl = Baseurl+"json/filter?pageId=";
	public static String subcategories_sortingurl = Baseurl+"json/filter?pageId=";
	
	public static String detailpageurl = Baseurl+"json/products/detailspage?productid=";
	public static String detailpageimageurl = Baseurl+"images/product/";
	public static String detailpagereviewimageurl = Baseurl+"";
	
	public static String heartaddurl = Baseurl+"json/favorite/add-remove?type=";
	public static String heartremoveurl = Baseurl+"json/favorite/add-remove?type=";

	public static String allshopsurl = Baseurl+"json/shops?pageId=";
	public static String allshopsimageurl = Baseurl+"";  

	public static String allshop_producturl = Baseurl+"json/product/pagecount?shopname=";
	public static String addtocarturl = Baseurl+"json/cartadd";
	public static String userprofileurl = Baseurl+"json/user/profile/";
	
	public static String search_pageurl = Baseurl+"json/search/pagecount?value=";

	public static String pagecount = Baseurl+"json/product/pagecount";
	
	/*---------------Cart Page Url---------------------*/
	
	public static String cartpage_url = Baseurl+"json/yourcart/";
	public static String checkout_url = Baseurl+"mobile";
	public static String paymenturl = Baseurl+"mobile/shipping-address?mobileId=";
	
	public static String cartproductremove = Baseurl+"json/cart/removeProduct?cartId=";
	public static String cartshopremove = Baseurl+"json/cart/removeShop?shop=";
	
	public static String currencylisturl = Baseurl+"json/currency-list";
	public static String currencyselectedurl = Baseurl+"json/currency-list/";
	
	public static String seeallreview_url = Baseurl+"json/review?shopid=";
	public static String userprofileuploadurl = Baseurl+"json/user/change-image?userId=";
	
	
	/*---------------Coupon code Url---------------------*/
	public static String coupon_add_url = Baseurl+"json/checkCode";
	public static String coupon_remove_url = Baseurl+"json/checkCodeRemove";
	
	
	/*public static String orderpurchaseurl = Baseurl+"json/user/purchases?userId=";
	public static String ordercanceledurl = Baseurl+"json/user/purchases?userId=";
	public static String orderdetailurl = Baseurl+"json/view-order?userId=";
	public static String orderdetailimageurl = Baseurl+"";*/
	
	public static String facebookurl = Baseurl+"json/facebooklogin?commonId=";
	public static String googleplusurl = Baseurl+"json/googlelogin?commonId=";

	/*---------------Conversation Page Url---------------------*/
	public static String conversation_url = Baseurl+"json/conversation?userId=";
	public static String conversationDetail_url = Baseurl+"json/view-conversation?userId=";
	public static String post_conversation_url = Baseurl+"json/send-message";
	
	
	/*---------------Dashboard Page Url---------------------*/
	public static String activityTab_url = Baseurl+"seller/activity/";
	public static String statsTab_url = Baseurl+"seller/stats?sellerId=";
	
	
	/*---------------Order Page Url---------------------*/
	public static String order_open_url = Baseurl+"seller/order?sellerId=";
	public static String order_completed_url = Baseurl+"seller/order?sellerId=";
	public static String order_detail_url = Baseurl+"seller/view-order?sellerId=";
	public static String manage_order_url = Baseurl+"seller/manage-order?sellerId=";
	public static String dispute_url = Baseurl+"seller/view-discussion?sellerId=";
	public static String dispute_postmsg_url = Baseurl+"seller/post-msg";
	
	
	/*---------------Purchase Page Url---------------------*/
	public static String order_purchase_url = Baseurl+"json/user/purchases?userId=";
	public static String order_purchase_completed_url = Baseurl+"json/user/purchases?userId=";
	
	
	/*---------------User Profile Url---------------------*/
	public static String userProfile_url = Baseurl+"json/user/profile/";
	public static String userProfile_see_favorite_shop_url = Baseurl+"json/user/favorite/";
	
	
	/*---------------Recommended Url---------------------*/
	public static String recommended_url = Baseurl+"json/homepage?commonId=";
	
	/*---------------Picks Url---------------------*/
	public static String picks_url = Baseurl+"json/pickspage?commonId=";
	
	
	/*---------------Picks Url---------------------*/
	public static String aboutus_webview_url = Baseurl+"android/about-us";
	public static String help_webview_url = Baseurl+"android/help";
	
	
	/*---------------YourItems Url---------------------*/
	public static String yourItem_url = Baseurl+"seller/list-items?sellerId=";
	public static String youritem_search_url = Baseurl+"seller/searchpage?value=";
	
	
	/*---------------YourItems Filter Url---------------------*/
	
	public static String filter_All ="All";
	public static String filter_Active ="Active";
	public static String filter_SoldOut ="Soldout";
	public static String filter_InActive ="Inactive";
	
	public static String List_All ="&list=all";
	public static String List_Active ="&list=active";
	public static String List_SoldOut ="&list=soldout";
	public static String List_InActive ="&list=inactive";
	public static String List_Tittle_Asc ="&sort=title_asc";
	public static String List_Tittle_Dsc ="&sort=title_desc";
	public static String List_Stock_Asc ="&sort=stock_asc";
	public static String List_Stock_Dsc ="&sort=stock_desc";
	public static String List_Price_Asc ="&sort=price_asc";
	public static String List_Price_Dsc ="&sort=price_desc";    
	
	public static String Tittle_A_to_Z ="Tittle: A to Z";
	public static String Tittle_Z_to_A ="Tittle: Z to A";
	public static String Stock_Low_to_High ="Stock: Low to High";
	public static String Stock_High_to_Low ="Stock: High to Low";
	public static String Price_Low_to_High ="Price: Low to High";
	public static String Price_High_to_Low ="Price: High to Low";
	
	
	
	/*---------------Reviews Url---------------------*/
	
	public static String reviews_list =Baseurl+"seller/review-list?sellerId=";
	public static String report_review =Baseurl+"seller/report-review?sellerId=";
	
	
	/*---------------Dispute Orders Url---------------------*/
	
	public static String dispute_order_open_url =Baseurl+"seller/list-claims?sellerId=";
	public static String dispute_order_closed_url =Baseurl+"seller/list-claims?sellerId=";
	
	
	
	
	
	
	
	
	
	

	//-----------------------------------seller--------------------------------//
	
	public static String profile_url = Baseurl+"seller/add-product";
	public static String country_list = Baseurl+"seller/country-list";
	public static String subcategory_url = Baseurl+"seller/category?catId=";
	public static String category_url = Baseurl+"seller/category";


	public static String copy_item =Baseurl+"seller/copy-item?pid=";
	public static String imageurl1 =Baseurl+"images/product/";
	public static String imageurl =Baseurl;
	public static String edit_pro = Baseurl+"seller/edit-item?pid=";
	
	
	
	public static String delete_msg_Id ="&msgId=";
	public static String delete_con_Id ="&convId=";
	public static String detail_chat_con_Id ="&cID=";
	public static String inbox_all_page_id ="&pageId=";
	public static String edit_seller_Id ="&sid=";
	public static String vote_id ="&voteId=";
	public static String search_Id ="&sellerId=";
	public static String Completed ="&type=completed";
	public static String Order_Details_UserId ="&userId=";
	public static String Order_Details_OrderId ="&orderId=";
	public static String Order_Manager_OrderId ="&orderId=";
	public static String Order_Status_OrderId ="&status=";
	public static String Stats_ViewBy ="&viewby=";
	public static String Activity_Details_UserId ="&userId=";
	public static String Dispute_SellerId ="sellerId=";
	public static String Dispute_Type_Open ="&type=open";
	public static String Dispute_Type_Close ="&type=close";
	public static String Discussion_SellerId ="sellerId=";
	
	public static String FollowSub ="follow?sellerId=";
	public static String UnFollowSub ="unfollow?sellerId=";

//Live
//	public static String profile_url_live = "http://www.etsyclone.zoplay.com/seller/add-product";
	
//sharedprefrencename
	public static String sharedPre_tour ="session_tour";
	public static String sharedPre_tour_edit ="session_tour_edit";
	public static String sharedPre_tour_copy ="session_tour_copy";
	public static String sharedPre_tour_main ="session_tour_main";
	
	public static String sharedPre_ProfileImage ="image";
	public static String sharedPre_Name ="Shopcy";
	public static String shared_SellerId ="SellerId";
	public static String shared_UserName ="UserName";
	public static String shared_Rating_Value ="rating_val";
	public static String shared_Photo ="Photos";
	
	public static String shared_countrylist ="c_list";
	
	public static String shared_s_tittle ="s_tittle";
	public static String shared_s_time ="s_time";
	public static String shared_s_des ="s_des";
	public static String shared_s_desId ="s_des_id";
	public static String shared_s_from ="s_from";
	public static String shared_s_cost ="s_cost";
	public static String shared_s_ano_cost ="s_ano_cost";
	
	public static String shared_AddItem_Id_page_1 ="Id";
	public static String shared_AddItem_Id_page_2 ="Ids";
	public static String shared_AddItem_Id_page_3 ="Idss";
	
	public static String shared_AddItem_cat_1_id ="cat_id1";
	public static String shared_AddItem_cat_2_id ="cat_id2";
	public static String shared_AddItem_cat_3_id ="cat_id3";
	
	public static String shared_AddItem_cat_1_name ="cat_name1";
	public static String shared_AddItem_cat_2_name ="cat_name2";
	public static String shared_AddItem_cat_3_name ="cat_name3";
	
	
	public static String shared_s_ano_cost_mul ="s_ano_cost_mul";
	public static String shared_s_cost_mul ="s_cost_mul";
	
/*	public static int additem_page_1_1 = 2131099714;
	public static int additem_page_1_2 = 2131099716;
	public static int additem_page_1_3 = 2131099719;
	public static int additem_page_2_1 = 2131099723;
	public static int additem_page_2_2 = 2131099726;
	public static int additem_page_3_1 = 2131099730;
*/
	
//	nexus 5
	
//	11-21 13:04:42.818: D/Add_Item_1---------->(13673): 2131099714
//	11-21 13:06:00.244: D/Add_Item_2---------->(13673): 2131099717
//	11-21 13:06:20.742: D/Add_Item_3---------->(13673): 2131099720

//	11-21 13:06:53.600: D/Add_Item_1---------->(13673): 2131099731


	public static int additem_page_1_1 = 2131099714;
	public static int additem_page_1_2 = 2131099717;
	public static int additem_page_1_3 = 2131099720;

	public static int additem_page_2_1 = 2131099724;
	public static int additem_page_2_2 = 2131099727;
	
	public static int additem_page_3_1 = 2131099731;
	
	//Listing Product
	
	
	

/*	http://192.168.1.253/sivaprakash/shopsy/seller/stats?sellerId=61&viewby=today
	http://192.168.1.253/sivaprakash/shopsy/seller/stats?sellerId=61&viewby=last7
	http://192.168.1.253/sivaprakash/shopsy/seller/stats?sellerId=61&viewby=week
	http://192.168.1.253/sivaprakash/shopsy/seller/stats?sellerId=61&viewby=last30
	http://192.168.1.253/sivaprakash/shopsy/seller/stats?sellerId=61&viewby=month
	http://192.168.1.253/sivaprakash/shopsy/seller/stats?sellerId=61&viewby=all
*/		
/*	list.add("Today");
    list.add("Last 7 days");
    list.add("Week");
    list.add("Last 30 days");
    list.add("This Month");
    list.add("View All");*/
    
	public static String Stats_Today ="Today";
	public static String Stats_Last7Days ="Last 7 days";
	public static String Stats_Week ="Week";
	public static String Stats_Last30Days ="Last 30 days";
	public static String Stats_ThisMonth ="This Month";
	public static String Stats_ViewAll ="View All";
	
	public static String Stats_Today_Res ="today";
	public static String Stats_Last7Days_Res ="last7";
	public static String Stats_Week_Res ="week";
	public static String Stats_Last30Days_Res ="last30";
	public static String Stats_ThisMonth_Res ="month";
	public static String Stats_ViewAll_Res ="all";
	public static String Order_Total_Amount_Zero ="0.00";
	public static String Dispute_Chat_Open ="1";
	public static String Dispute_Chat_Closed ="2";
	
	public static String Alart_Empty ="No data for";
	public static String Alart_Internet ="Check Your Internet Connection";
	public static String Alart_AppName ="Shopsy";
	public static String Alart_Status500 ="Can't able to feach the data try again";
	public static String success ="Success";
	
	public static String AddItemFinal_Response ="Item Listed Successfully";
	public static String ShopsyUserplaypath ="https://play.google.com/store/apps/details?id=com.shopsy";
//	com.bulletnoid.android.widget.StaggeredGridViewDemo
	public static String ShopsyUserName = "com.shopsy";
	
	public static String Session_Type ="session_type";
	public static String Session_Type_country ="ADD_ITEM_FINAL_SHIP";
	public static String Session_Type_country_Id ="ADD_ITEM_FINAL_SHIPID";
	public static String Session_Type_cost ="ADD_ITEM_FINAL_SHIPcost";
	public static String Session_Type_ano_cost ="ADD_ITEM_FINAL_SHIPanocost";
	public static String Session_Type_title ="ADD_ITEM_FINAL_SHIPtitle";
	public static String Session_Type_time ="ADD_ITEM_FINAL_SHIPtime";
	public static String Session_Type_shipfrom ="ADD_ITEM_FINAL_SHIPfrom";
	
	
	
/*	public static String ship_edit_cost ="edit_cost";
	public static String ship_edit_ano_cost ="edit_ano_cost";
	public static String ship_edit_country ="edit_country";*/
	
	
	public static String Session_AddImage1 ="session_addimage";
	public static String Session_AddImage2 ="session_addimage";
	public static String Session_AddImage3 ="session_addimage";
	public static String Session_AddImage4 ="session_addimage";
	public static String Session_AddImage5 ="session_addimage";
	public static String Session_AddTitle ="session_addtitle";
	public static String Session_AddPrice ="session_addprice";
	public static String Session_AddQua ="session_addqua";
	public static String Session_AddDes="session_adddes";
	public static String Session_AddTag ="session_addtag";
	public static String Session_AddMat ="session_addmat";
	
//Google project Id	
//	API key	= AIzaSyDKdzKRknMspcpGgzTVicpF18yrwbpFU2o
	static final String GOOGLE_PROJECT_ID = "383557488182";
	static final String MESSAGE_KEY = "message";
	static final String MESSAGE_KEY_K = "type";
	static final String MESSAGE_Url_1 = "url_key1";
	static final String MESSAGE_Url_2 = "url_key2";
	static final String MESSAGE_Url_3 = "url_key3";
	static final String MESSAGE_Url_4 = "url_key4";
	
	static final String MESSAGE_Url_5 = "url_key5";
	static final String MESSAGE_Url_6 = "url_key6";
	static final String MESSAGE_Url_7 = "url_key7";
	public static String discover_detail_req_pageId = "&page_Id=";
	
	// notification settings
		public static String notifi_settings_sound = "notifi_sett_sound";
		public static String notifi_settings_ = "notifi_sett_";
		public static String notifi_enable_session = "notifi_enable";
		public static String notifi_sound_enable_session = "notifi_sound_enable";
}

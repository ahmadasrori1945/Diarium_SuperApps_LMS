package id.co.telkomsigma.Diarium.util;

/**
 * Created by Gilang on 12-Apr-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    static final String SERVER_URL = "server_access";
    static final String SERVER_DEFAULT_API_URL = "https://apidiarium.digitalevent.id/api/";
    static final String SERVER_DEFAULT_URL = "https://apidiarium.digitalevent.id";
    static final String USER_BUSINESS_CODE = "business_code";
    static final String USER_NIK = "nik";
    static final String USER_PASSWORD = "password";
    static final String USER_JWT_TOKEN = "token_jwt";
    static final String USER_FULL_NAME = "user_full_name";
    static final String USER_NICK_NAME = "user_nick_name";
    static final String USER_IS_LOGIN = "is_login";
    static final String USER_NOTIF = "notification";
    static final String USER_FACE_CODE = "user_face_code";
    public static  String ACTIVITY_KEY;


    private static final String PREFER_NAME = "diarium_telkomsigma";

    public UserSessionManager(Context context){
        this._context = context;

        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }

//    public void setUserSession(int id, String name, String username, String pass){
//
//        editor.putBoolean(USER_IS_LOGIN, true);
//        editor.putBoolean("notifStatus", true);
//        editor.putInt("nik", id);
//        editor.putString(USE, username);
//        editor.putString("password", pass);
//        editor.commit();

//    }

    public void setTempPers(String id){
        editor.putString("pers", id);
        editor.commit();
    }

    public String getTempPers() {
        return pref.getString("pers", "");
    }

    public void setBpjs(String id){
        editor.putString("bpjs", id);
        editor.commit();
    }

    public String getBpjs() {
        return pref.getString("bpjs", "Update your data");
    }

    public void setNpwp(String id){
        editor.putString("npwp", id);
        editor.commit();
    }

    public String getNpwp() {
        return pref.getString("npwp", "Update your data");
    }
    public void setRek(String id){
        editor.putString("rek", id);
        editor.commit();
    }

    public String getRek() {
        return pref.getString("rek", "Update your data");
    }
    public void setKtp(String id){
        editor.putString("ktp", id);
        editor.commit();
    }

    public String getKtp() {
        return pref.getString("ktp", "Update your data");
    }

    public void setCountQuestion(int id){
        editor.putInt("count_question", id);
        editor.commit();
    }

    public int getCountQuestion(){
        return pref.getInt("count_question", 0);
    }

    public void setThemeId(String id){
        editor.putString("themeid", id);
        editor.commit();
    }

    public String getThemeId(){
        return pref.getString("themeid", "");
    }

    public void setEmpId(String id){
        editor.putString("empid", id);
        editor.commit();
    }

    public String getEmpId(){
        return pref.getString("empid", "");
    }

    public void setGeneratedContentId(String id){
        editor.putString("contentId", id);
        editor.commit();
    }

    public String getGeneratedContentId(){
        return pref.getString("contentId", "");
    }

    public void setGeneratedCommentId(String id){
        editor.putString("commentId", id);
        editor.commit();
    }

    public String getGeneratedCommentId(){
        return pref.getString("commentId", "");
    }

    public void setGeneratedPostId(String id){
        editor.putString("postId", id);
        editor.commit();
    }

    public String getGeneratedPostId(){
        return pref.getString("postId", "");
    }

    public void setGenerateActivityId(String id){
        editor.putString("activityId", id);
        editor.commit();
    }

    public String getGenerateActivityId(){
        return pref.getString("activityId", "");
    }

    public void setCountType(String stat){
        editor.putString("countType", stat);
        editor.commit();
    }

    public String getCountType(){
        return pref.getString("countType", "");
    }

    public void setCurrentDate(String emot){
        editor.putString("currentDate", emot);
        editor.commit();
    }


    public String getCurrentDate(){
        return pref.getString("currentDate", "");
    }

    public void setData(String key, String value) {
        editor.putString(ACTIVITY_KEY + key, value);
        editor.commit();
    }

    public String getData(String key) {
        return pref.getString(ACTIVITY_KEY + key, "");
    }

    public void setJob(String job){
        editor.putString("job", job);
        editor.commit();
    }

    public String getJob(){
        return pref.getString("job", "");
    }

    public void setPersonalNumberLoginTeam(String name){
        editor.putString("yglogin", name);
        editor.commit();
    }

    public String getPersonalNumberLoginTeam(){
        return pref.getString("yglogin", "");
    }

    public void setStatus(String name){
        editor.putString("statusnya", name);
        editor.commit();
    }

    public String getStatus(){
        return pref.getString("statusnya", "");
    }

    public void setTodayActivityName(String name){
        editor.putString("todayActivityName", name);
        editor.commit();
    }

    public String getTodayActivityName(){
        return pref.getString("todayActivityName", "");
    }

    public void setTodayActivityPersonalNumber(String name){
        editor.putString("ActivityPersonalNumbe", name);
        editor.commit();
    }

    public String getTodayActivityPersonalNumber(){
        return pref.getString("ActivityPersonalNumbe", "");
    }

    public void setTodayActivity(String name){
        editor.putString("todayActivity", name);
        editor.commit();
    }

    public String getTodayActivity(){
        return pref.getString("todayActivity", "");
    }

    public void setName(String name){
        editor.putString("name", name);
        editor.commit();
    }

    public String getName(){
        return pref.getString("name", "");
    }

    public void setFormatFrom(String stat){
        editor.putString("formatfrom", stat);
        editor.commit();
    }

    public String getFormatFrom(){
        return pref.getString("formatfrom", "");
    }

    public void setFormatUntil(String stat){
        editor.putString("formatuntil", stat);
        editor.commit();
    }

    public String getFormatUntil(){
        return pref.getString("formatuntil", "");
    }

    public void setFrom(String stat){
        editor.putString("from", stat);
        editor.commit();
    }

    public String getFrom(){
        return pref.getString("from", "");
    }

    public void setUntil(String stat){
        editor.putString("until", stat);
        editor.commit();
    }

    public String getUntil(){
        return pref.getString("until", "CO");
    }

    public void setType(String stat){
        editor.putString("type", stat);
        editor.commit();
    }

    public String getType(){
        return pref.getString("type", "CO");
    }

    public void setGalleryId(String emot){
        editor.putString("gallry_id", emot);
        editor.commit();
    }


    public String getGalleryId(){
        return pref.getString("gallery_id", "");
    }

    public void setEventId(String emot){
        editor.putString("event_id", emot);
        editor.commit();
    }


    public String getEventId(){
        return pref.getString("event_id", "");
    }

    public void setEmot(String emot){
        editor.putString("emot", emot);
        editor.commit();
    }


    public String getEmot(){
        return pref.getString("emot", "");
    }

    public void setLatLon(String stat){
        editor.putString("latlon", stat);
        editor.commit();
    }

    public String getLatLon(){
        return pref.getString("latlon", "0");
    }

    public void setStat(String stat){
        editor.putString("stat", stat);
        editor.commit();
    }

    public String getStat(){
        return pref.getString("stat", "CO");
    }

    public void setToken(String token){
        editor.putString(USER_JWT_TOKEN, token);
        editor.commit();
    }

    public String  getToken(){
        return pref.getString(USER_JWT_TOKEN, "-");
    }

    public void setUserBusinessCode(String buscd){
        editor.putString(USER_BUSINESS_CODE, buscd);
        editor.commit();
    }

    public String  getUserBusinessCode(){
        return pref.getString(USER_BUSINESS_CODE, "-");
    }



    public void setUserNik(String id){
        editor.putString(USER_NIK, id);
        editor.commit();
    }

    public String  getUserNIK(){
        return pref.getString(USER_NIK, "-");
    }

    public void setUserFullName(String name){
        editor.putString(USER_FULL_NAME, name);
        editor.commit();
    }

    public String getUserFullName(){
        return pref.getString(USER_FULL_NAME, "-");
    }


    public void setBornDate(String name){
        editor.putString("borndate", name);
        editor.commit();
    }

    public void setStatusClickBornDate(String click){
        editor.putString("borndatestatusclick", click);
        editor.commit();
    }

    public  String getStatusClickBornDate() {
        return pref.getString("borndatestatusclick", "0");
    }

    public  String getBornDate() {
        return pref.getString("borndate", "-");
    }


    public void setIdentifier(String name){
        editor.putString("ide", name);
        editor.commit();
    }

    public  String getIdentifier() {
        return pref.getString("ide", "-");
    }

    public void setAvatar(String name){
        editor.putString("avatar", name);
        editor.commit();
    }

    public  String getAvatar() {
        return pref.getString("avatar", "-");
    }


    public void setUserNickName(String name){
        editor.putString(USER_NICK_NAME, name);
        editor.commit();
    }

    public  String getUserNickName() {
        return pref.getString(USER_NICK_NAME, "-");
    }

    public void setLoginState(boolean state){
        editor.putBoolean(USER_IS_LOGIN, state);
        editor.commit();
    }

    public boolean isLogin(){
        return pref.getBoolean(USER_IS_LOGIN, false);
    }

    public void setUserFaceCode(String id){
        editor.putString(USER_FACE_CODE, id);
        editor.commit();
    }

    public String  getUserFaceCode(){
        return pref.getString(USER_FACE_CODE, "");
    }

    public void setFMResponse(String res){

        editor.putString("fm_response", res);
        editor.commit();
    }

    public String  getFMResponse(){
        return pref.getString("fm_response", "-");
    }

    public void setFMMessage(String res){

        editor.putString("fm_message", res);
        editor.commit();
    }

    public String  getFMMessage(){
        return pref.getString("fm_message", "-");
    }

    public void setFMResult(int res){

        editor.putInt("fm_result", res);
        editor.commit();
    }

    public int  getFMResult(){
        return pref.getInt("fm_result", Activity.RESULT_CANCELED);
    }

    public void setServerURL(String url){

        editor.putString(SERVER_URL, url);
        editor.commit();
    }

    public String getServerURL(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_API_URL);
    }

    public void setCheckedServer(boolean status){

        editor.putBoolean("checked_server", status);
        editor.commit();
    }

    public boolean getNotificationStatus(){
        return pref.getBoolean(USER_NOTIF,false);
    }


    public void logoutUser(){

        editor.clear();
        editor.commit();

    }

}

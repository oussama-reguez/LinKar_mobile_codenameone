/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.linkar.main;

import com.codename1.components.ImageViewer;
import com.codename1.db.Database;
import com.codename1.facebook.FaceBookAccess;
import com.codename1.facebook.User;
import com.codename1.io.AccessToken;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UIBuilder;
import com.linkar.entities.Membre;
import static com.linkar.main.DiscussionForm.SEND_MESSAGE_URL;
import static com.linkar.main.MyApplication.LOGIN_URL;
import static com.linkar.main.MyApplication.connectedMember;
import com.linkar.utils.Json;
import com.linkar.utils.SqlLite;
import java.io.IOException;

/**
 *
 * @author Oussama Reguez
 */

public class LoginForm {
  private   Form form;
   public static String TOKEN;
    private Membre connectedMember =null;
    private Resources theme;
   
    private  void signInWithFB(final Form main) {
        /*
         FaceBookAccess.setClientId("428671887491341");
        FaceBookAccess.setClientSecret("29f3fd42e1050079087e92657920cf23");
        FaceBookAccess.setRedirectURI("http://localhost/linkar/web/app_dev.php/");
       FaceBookAccess.setPermissions(new String[]{"user_location", "user_photos", "friends_photos", "publish_stream", "read_stream", "user_relationships", "user_birthday",
                    "friends_birthday", "friends_relationships", "read_mailbox", "user_events", "friends_events", "user_about_me"});
       */
        FaceBookAccess.setClientId("132970916828080");
        FaceBookAccess.setClientSecret("6aaf4c8ea791f08ea15735eb647becfe");
        FaceBookAccess.setRedirectURI("http://www.codenameone.com/");
        FaceBookAccess.setPermissions(new String[]{"user_location", "user_photos", "friends_photos", "publish_stream", "read_stream", "user_relationships", "user_birthday",
                    "friends_birthday", "friends_relationships", "read_mailbox", "user_events", "friends_events", "user_about_me"});
       
        FaceBookAccess.getInstance().showAuthentication(new ActionListener() {
            
            public void actionPerformed(ActionEvent evt) {
                System.err.println("");
                if (evt.getSource() instanceof  AccessToken) {
                    AccessToken aToken =( AccessToken) evt.getSource();
                     String token = aToken.getToken();
                    String expires = aToken.getExpires();
                    TOKEN = token;
                    System.out.println("recived a token " + token + " which expires on " + expires);
                    //store token for future queries.
                    Storage.getInstance().writeObject("token", token);
                    if (main != null) {
                     User fb= getFacebookUser();
                        
                        main.show();
                    }
                    else{
                        
                    }
                } else {
                    Exception err = (Exception) evt.getSource();
                    err.printStackTrace();
                    Dialog.show("Error", "An error occurred while logging in: " + err, "OK", null);
                }
            }
        });
    }
    public static boolean isFbLogin() {
        return Storage.getInstance().readObject("token") == null;
    }
    public void validateFbLogin(){
        if(Database.exists("membre")){
 //verify if user is logged in with facebook 
    if (isFbLogin()) {
              //check if token has expired and get user fb object 
                //token exists no need to authenticate
            TOKEN = (String) Storage.getInstance().readObject("token");
            FaceBookAccess.setToken(TOKEN);
            //in case token has expired re-authenticate
            FaceBookAccess.getInstance().addResponseCodeListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent evt) {
                    NetworkEvent ne = (NetworkEvent) evt;
                    int code = ne.getResponseCode();
                    
                    if (code == 400) {
                        displayLoginForm(theme);
                    }                    
                }
            });
              //convert fb object to member object 
              
              //update member object in database 
              
              //update member object  in sqlLite 
              
          }
            
//verify if password match in data base !
            
            
           
            
        }
        
          
        
          if (isFbLogin()) {
              
              
              
     //check sql lite
     //check fb
     //sign in then !
     
           
        } else {
            //token exists no need to authenticate
            TOKEN = (String) Storage.getInstance().readObject("token");
            FaceBookAccess.setToken(TOKEN);
            //in case token has expired re-authenticate
            FaceBookAccess.getInstance().addResponseCodeListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent evt) {
                    NetworkEvent ne = (NetworkEvent) evt;
                    int code = ne.getResponseCode();
                    //token has expired
                    if (code == 400) {
                       //change here !
                    }                    
                }
            });
        }
    }
   
       User getFacebookUser(){
        User  me;
         try {
             me = FaceBookAccess.getInstance().getUser(null);
         } catch (IOException ex) {
           return null;
         }
       //convert it to Member object 
                    System.err.println("");
                return me;
    }
    public Membre userToMembre(User f){
        Membre m = new Membre();
        
       m.setFacebookId(f.getId());
        m.setFirst_name(f.getFirst_name());
        m.setLast_name(f.getLast_name());
        m.setGender(f.getGender());
        m.setEmail(f.getEmail());
        return m;
    }
    Membre getMemberFromDb(String username,String password){
      
       ConnectionRequest r = new ConnectionRequest();
            r.setPost(false);
            r.setUrl(LOGIN_URL);
            r.addArgument("username", username);
              r.addArgument("password", password);
            
            NetworkManager.getInstance().addToQueueAndWait(r);
            String response = new String (r.getResponseData());
           
          if(response.equals("error")){
              return null;
          }
            return Json.jsonToMember(response);
  }

    public LoginForm(Resources theme){
this.theme = theme;
//   validateFbLogin()
        
        
         form=generateLoginForm(theme);
        
    }
       public LoginForm( Resources theme, String Username){
this.theme = theme;
//   validateFbLogin()
        
        
         form=generateLoginForm(theme);
        
    }

   Membre getMemberWithFb(String facebookId){
      
       ConnectionRequest r = new ConnectionRequest();
            r.setPost(false);
            r.setUrl(FACEBOOK_SIGNIN_URL);
            r.addArgument("idFacebook", facebookId);
            
            NetworkManager.getInstance().addToQueueAndWait(r);
            String response = new String (r.getResponseData());
           
          if(response.equals("error")){
              return null;
          }
            return Json.jsonToMember(response);
  }
    public static final String FACEBOOK_SIGNIN_URL="http://localhost/linkar_web/web/app_dev.php/rest/fbSignIn";
     public static final String FACEBOOK_SIGNUP_URL="http://localhost/linkar_web/web/app_dev.php/rest/fbSignUp";
    private Form generateLoginForm(Resources theme){
       UIBuilder ui = new UIBuilder();
         UIBuilder.registerCustomComponent("ImageViewer", ImageViewer.class);
       Form form = ui.createContainer(theme, "login").getComponentForm();
        TextField login = (TextField) ui.findByName("txtUsername",form);
        TextField password = (TextField) ui.findByName("txtPassword",form);
      
      Button btnLogin   = (Button) ui.findByName("btnLogin",form);
      btnLogin.addActionListener((evt) -> {
        //  validateLogin(login.getText(),password.getText());
     
    //  Membre k = getMemberWithFb("777");
       connectedMember =getMemberFromDb("dada", "oussama");
       connectedMember.setUsername("dada");
       connectedMember.setPassword("oussamaa");
       //store in db
      
        
       if(connectedMember==null){
          
       }
       else{
            SqlLite.dropDataBase();
       SqlLite.createTableMember();
         SqlLite.insertMember(connectedMember);
       Membre l=SqlLite.getMember();
           new MainForm(theme).show();
       }
        // signInWithFB(this.getForm());
      });
      return form;
    }

   private void displayLoginForm(Resources theme){
       UIBuilder ui = new UIBuilder();
         UIBuilder.registerCustomComponent("ImageViewer", ImageViewer.class);
       form = ui.createContainer(theme, "login").getComponentForm();
        TextField login = (TextField) ui.findByName("txtUsername",form);
        TextField password = (TextField) ui.findByName("txtPassword",form);
      
      Button btnLogin   = (Button) ui.findByName("btnLogin",form);
      btnLogin.addActionListener((evt) -> {
        //  validateLogin(login.getText(),password.getText());
      VerifyCinForm f = new VerifyCinForm(theme);
      
        //  signIn(f.getForm());
      });
     form.show();
    }    
    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Membre getConnectedMember() {
        return connectedMember;
    }

    public void setConnectedMember(Membre connectedMember) {
        this.connectedMember = connectedMember;
    }
    
    
}

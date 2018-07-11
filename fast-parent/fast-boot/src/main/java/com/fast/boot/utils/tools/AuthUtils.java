package com.fast.boot.utils.tools;

import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.fast.boot.utils.lang.StringUtils;
import com.fast.boot.utils.mapper.JsonMapper;
import com.fast.boot.utils.tools.auth.AuthPassword;
import com.fast.boot.utils.tools.auth.DigestsPwd;
import com.fast.boot.utils.tools.mail.MailSenderInfo;
import com.fast.boot.utils.tools.mail.SimpleMailSender;

public class AuthUtils {
	
	private static final int SALT_SIZE = 8;
	public static final int HASH_INTERATIONS = 1024;
	
	public static JsonMapper jsonMapper= JsonMapper.nonEmptyMapper();

	public static void setSessionUser(HttpServletRequest request, String sessionKey, Object user){
		request.getSession().setAttribute(sessionKey, user);
	}
	public static Object getSessionUser(HttpServletRequest request, String sessionKey){
		return request.getSession().getAttribute(sessionKey);
	}
	public static void clearSessionUser(HttpServletRequest request, String sessionKey){
		request.getSession().removeAttribute(sessionKey);
	}
	
	/**
	 * 实现对密码加密操作
	 * @param plainPassword
	 * @return
	 */
	public static AuthPassword entryptPassword(String plainPassword) {
		AuthPassword passwd = new AuthPassword();
		passwd.setPlainPassword(plainPassword);
		byte[] salt = DigestsPwd.generateSalt(SALT_SIZE);
		passwd.setSalt(Hex.encodeHexString(salt));
		byte[] hashPassword = DigestsPwd.sha1(passwd.getPlainPassword().getBytes(),
				salt, HASH_INTERATIONS);
		passwd.setPassword(Hex.encodeHexString(hashPassword));
		return passwd;
	}
	
	
	/*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
    } 
    
    /**
     * 验证密码是否正确
     * @param md5Password
     * @param plainPassword
     * @return
     */
    public static boolean validatePassword(String saltPassword, String salt, String plainPassword) {
    	AuthPassword auth = new AuthPassword();
    	auth.setPassword(saltPassword);
    	auth.setPlainPassword(plainPassword);
    	auth.setSalt(salt);
		if(StringUtils.isNotEmpty(plainPassword) && AuthUtils.validatePassword(auth)) {
			return true;
		}
		return false;
	}
    
    /**
     * 发送邮件配置
     * @param plainPassword
     * @return 
     * @return
     */
    public static boolean sendMail(String email,String sb) {
    	//这个类主要是设置邮件  
		MailSenderInfo mailInfo = new MailSenderInfo();   
		mailInfo.setMailServerHost("smtp.163.com");   
		mailInfo.setMailServerPort("25");   
		mailInfo.setValidate(true);   
		mailInfo.setUserName("zhaozheyouxiang@163.com");   
		mailInfo.setPassword("zhe450728");//您的邮箱密码   
		mailInfo.setFromAddress("zhaozheyouxiang@163.com");   
		mailInfo.setToAddress(email);   //"zhaozheyouxiang@163.com"
		mailInfo.setSubject("激活demo账号");   
		mailInfo.setUrl(sb);   
		//这个类主要来发送邮件  
		//SimpleMailSender sms = new SimpleMailSender();  
		return SimpleMailSender.sendHtmlMail(mailInfo);//发送html格式  
		//return sms.sendTextMail(mailInfo);//发送文体格式   
	}
    
    /**
     * 验证登陆密码是否正确
     * @param passwd
     * @return
     */
    public static boolean validatePassword(AuthPassword passwd) {
    	byte[] salt = null;
		try {
			salt = Hex.decodeHex(passwd.getSalt().toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		byte[] hashPassword = DigestsPwd.sha1(passwd.getPlainPassword().getBytes(),
				salt, HASH_INTERATIONS);
		if (Hex.encodeHexString(hashPassword).equals(passwd.getPassword())) {
			return true;
		}
		return false;
	}


	public static String buildRedirectUrl(String context, String requestURI) {
		// TODO Auto-generated method stub
		return null;
	}
    
}

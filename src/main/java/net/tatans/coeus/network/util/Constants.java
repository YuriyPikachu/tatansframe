package net.tatans.coeus.network.util;
/**
 * @author Yuliang 
 * @time 2014-11-21 
 * @version 1.0
 */
public class Constants {

    /**
     * Account type string.
     */
	public static final String ACCOUNT_TYPE = "net.tatans.hyperion.account";

    /**
     * Authtoken type string.
     */
    public static final String AUTHTOKEN_TYPE = "net.tatans.hyperion.account";
    
    /**
     * 内部Cas URL.
     */
//    public static final String CAS_GRANT_SERVER = "http://192.168.1.249:8090/user/cas/getTicketGrantingTicket.do?";
//    public static final String CAS_SERVICE_SERVER = "http://192.168.1.249:8090/user/cas/getServiceTicket.do?";
    /**
     * 外部部Cas URL.
     */
  //  public static final String CAS_GRANT_SERVER = "http://115.29.11.17:8090/user/cas/getTicketGrantingTicket.do?";
    public static final String CAS_SERVICE_SERVER = "http://115.29.11.17:8090/user/cas/getServiceTicket.do";
    
    /**
     * 外部部Cas URL 用于注册.
     */
    public static final String CAS_GRANT_SERVER = "http://115.29.11.17:8090/user/cas/login.do";
}

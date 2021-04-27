package model;

/**
 * @author dongzhonghua
 * Created on 2021-04-24
 */

public class HostInfo {
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private String hostname;
    private String ip;
    private int port;
    private String homePageUrl;


    public HostInfo(String hostname, String ip, int port, String homePageUrl) {
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.homePageUrl = homePageUrl;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }
}

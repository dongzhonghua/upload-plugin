package model;

import java.util.List;

/**
 * @author dongzhonghua
 * Created on 2021-04-25
 */
public class InstanceResult {

    private String hostname;
    private List<HostInfo> hostInfoList;


    public InstanceResult(String hostname, List<HostInfo> hostInfoList) {
        this.hostname = hostname;
        this.hostInfoList = hostInfoList;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<HostInfo> getHostInfoList() {
        return hostInfoList;
    }

    public void setHostInfoList(List<HostInfo> hostInfoList) {
        this.hostInfoList = hostInfoList;
    }

}

package com.gdc.nms.testing.client;

import java.util.List;

import com.gdc.nms.testing.client.ssh.SSHCredential;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Server")
public class Serv {
    @XStreamAlias("SSH")
    private SSHCredential sshCredential;
    @XStreamAlias("MySQL")
    private MySQLCredential mysqlCredential;
    private List<String> jadeHosts;
    private String glassfishPath;

    public SSHCredential getSshCredential() {
        return sshCredential;
    }

    public void setSshCredential(SSHCredential sshCredential) {
        this.sshCredential = sshCredential;
    }

    public MySQLCredential getMysqlCredential() {
        return mysqlCredential;
    }

    public void setMysqlCredential(MySQLCredential mysqlCredential) {
        this.mysqlCredential = mysqlCredential;
    }

    public List<String> getJadeHosts() {
        return jadeHosts;
    }

    public String[] getJadeHostsArray() {
        if (jadeHosts == null || jadeHosts.size() <= 0) {
            return new String[] { sshCredential.getHostname() };
        }

        String s[] = new String[jadeHosts.size()];

        for (int i = 0; i < jadeHosts.size(); i++) {
            s[i] = jadeHosts.get(i);
        }

        return s;
    }

    public void setJadeHosts(List<String> jadeHosts) {
        this.jadeHosts = jadeHosts;
    }

    public String getGlassfishPath() {
        return glassfishPath;
    }

    public void setGlassfishPath(String glassfishPath) {
        this.glassfishPath = glassfishPath;
    }

}

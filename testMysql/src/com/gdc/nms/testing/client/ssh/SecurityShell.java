package com.gdc.nms.testing.client.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public class SecurityShell {
    private JSch jsch;
    private SSHCredential sshAuth;
    private Session session;
    private String glassfish;
    private List<Channel> channels;

    public SecurityShell(SSHCredential sshAuth) {
        if (sshAuth == null) {
            throw new NullPointerException();
        }
        jsch = new JSch();
        this.sshAuth = sshAuth;
        channels = new ArrayList<Channel>();
    }

    public void connect() throws JSchException {
        if (session == null) {
            session = jsch.getSession(sshAuth.getUsername(), sshAuth.getHostname(), sshAuth.getPort());
            session.setPassword(sshAuth.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            
        }
    }

    /**
     * Open new SSH Channel
     * 
     * @param type
     *            {@link ChannelSftp} {@link ChannelExec} {@link ChannelShell}
     * @return Open channel of type 'type'
     * @throws JSchException
     */
    @SuppressWarnings("unchecked")
    public <T extends Channel> T getChannel(Class<T> type) throws JSchException {

        Channel channel = session.openChannel(type.getSimpleName().toLowerCase().replace("channel", ""));
        channels.add(channel);
        return (T) channel;
    }

    public void executeJar(String filepath, String[] args) throws JSchException {
        ChannelExec channel = getChannel(ChannelExec.class);
        channel.setPty(true);
        StringBuffer command = new StringBuffer("java");
        String cmdArgs = (args == null) ? "" : Arrays.toString(args).replaceAll("[\\[\\],]", "");

        command.append(' ').append("-Xdebug -agentlib:jdwp=transport=dt_socket,address=1044,server=y,suspend=n")
            .append(' ').append("-jar").append(' ').append(glassfish).append("/").append(filepath).append(' ')
            .append(cmdArgs);

        channel.setExtOutputStream(System.err);
        channel.setOutputStream(System.err);

        channel.setCommand(command.toString());
        channel.connect();

    }

    public void uploadFiles(File[] src, String glassfishPath, String path) throws JSchException, SftpException {
        if (src == null || glassfishPath == null) {
            throw new NullPointerException();
        } else if (src.length == 0) {
            return;
        }

        final CountDownLatch latch = new CountDownLatch(src.length);

        for (File f : src) {
            uploadFile(f, glassfishPath, path, new SftpProgressMonitor() {
                @Override
                public void init(int arg0, String arg1, String arg2, long arg3) {
                    System.out.print(arg2);
                }

                @Override
                public void end() {
                    System.out.println(" [OK]");
                    latch.countDown();
                }

                @Override
                public boolean count(long arg0) {
                    return true;
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void uploadFile(File src, String glassfishPath, String path, SftpProgressMonitor monitor)
            throws JSchException, SftpException {

        if (src == null) {
            throw new NullPointerException();
        }

        if (glassfishPath == null) {
            glassfishPath = "";
        }

        glassfishPath = glassfishPath.concat("/").concat("domains/domain1/applications/j2ee-apps/");

        ChannelSftp channel = getChannel(ChannelSftp.class);
        channel.setPty(true);
        channel.connect();

        Vector<? extends LsEntry> ls = channel.ls(glassfishPath);

        String nmsPath = "";

        for (LsEntry s : ls) {
            if (s.getFilename().contains("nms")) {
                nmsPath = s.getFilename();
            }
        }
        
        if (glassfish == null || glassfish.isEmpty()) {
            glassfish = glassfishPath.concat(nmsPath);
        }

        glassfishPath = glassfishPath.concat(nmsPath).concat("/").concat(path);

        if (!glassfishPath.isEmpty()) {
            try {
                channel.stat(glassfishPath);
            } catch (SftpException e1) {
                try {
                    channel.mkdir(glassfishPath);
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            String _path = (path.isEmpty()) ? glassfishPath : glassfishPath + "/";
            channel.put(new FileInputStream(src), _path + src.getName(), monitor, ChannelSftp.OVERWRITE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
        }
    }

    public void disconnect() {

        for (Channel c : channels) {
            c.disconnect();
        }

        if (session != null) {
            session.disconnect();
        }
    }

}
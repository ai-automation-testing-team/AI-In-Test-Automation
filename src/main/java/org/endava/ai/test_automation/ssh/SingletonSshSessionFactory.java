package org.endava.ai.test_automation.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;

public class SingletonSshSessionFactory extends JschConfigSessionFactory {

    private static SingletonSshSessionFactory instance;


    private SingletonSshSessionFactory() {
    }


    public static synchronized SingletonSshSessionFactory getInstance() {
        if (instance == null) {
            instance = new SingletonSshSessionFactory();
        }
        return instance;
    }


    @Override
    protected void configure(OpenSshConfig.Host host, Session session) {
        // Do not verify host key (you might want to do this in a real-world application for security)
        session.setConfig("StrictHostKeyChecking", "no");
    }


    @Override
    protected JSch getJSch(final OpenSshConfig.Host hc, FS fs) throws JSchException {
        JSch defaultJSch = super.getJSch(hc, fs);
        defaultJSch.addIdentity("~/.ssh/id_rsa"); // Path to your private SSH key
        return defaultJSch;
    }

}

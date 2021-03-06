package com.mikesantos.FTPServerSetup;

import com.guichaguri.minimalftp.FTPConnection;

import com.guichaguri.minimalftp.api.IFileSystem;
import com.guichaguri.minimalftp.api.IUserAuthenticator;

import java.security.MessageDigest;
import java.util.Arrays;

public class SimpleAuth implements IUserAuthenticator {

    private IFileSystem<?> fileSystem;
    private String username;
    private byte[] password;


    public SimpleAuth(IFileSystem<?> fileSystem, String username, String password) {
        this.fileSystem = fileSystem;
        this.username = username;
        this.password = toMD5(password);
    }

    private byte[] toMD5(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(pass.getBytes("UTF-8"));
        } catch(Exception ex) {
            return pass.getBytes();
        }
    }

    @Override
    public boolean needsUsername(FTPConnection con) {
        return true;
    }

    @Override
    public boolean needsPassword(FTPConnection con, String username) {
        return true;
    }

    @Override
    public IFileSystem<?> authenticate(FTPConnection con, String username, String password) throws AuthException {
        if(username.equalsIgnoreCase(this.username)) {
            byte[] inputPass = toMD5(password);
            if(Arrays.equals(this.password, inputPass)) {
                return fileSystem;
            }
        }
        throw new AuthException();
    }
}

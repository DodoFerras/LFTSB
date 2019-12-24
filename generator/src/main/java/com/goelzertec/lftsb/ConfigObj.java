package com.goelzertec.lftsb;

public class ConfigObj {
    private String serverHost, queryUsername, queryPassword;
    private int queryPort, localPort;

    private String bannerFile;
    private String wellcomeMessage, wellComeTextColor, onlineClientsTextColor, falseClientName;
    private int onlineClientsXPosition, onlineClientsYPosition, onlineClientsTextSize, wellcomeXPosition, wellcomeYPosition, wellComeTextSize;

    /*public ConfigObj() {
        this.queryPassword = "a";
        this.serverHost = "a";
        this.queryUsername ="";
        queryPort = 5174;
        bannerFile = "a";
        wellcomeMessage = "a";
        wellcomeXPosition = "a";
        wellcomeYPosition = "a";
        onlineClientsYPosition = "a";
        onlineClientsXPosition= "a";
        onlineClientsTextSize = "a";
        wellComeTextSize= "a";
        onlineClientsTextColor= "a";
        wellComeTextColor= "a";
    }*/

    public String getServerHost() {
        return serverHost;
    }

    public String getQueryUsername() {
        return queryUsername;
    }

    public String getQueryPassword() {
        return queryPassword;
    }

    public int getQueryPort() {
        return queryPort;
    }

    public String getBannerFile() {
        return bannerFile;
    }

    public String getWellcomeMessage() {
        return wellcomeMessage;
    }

    public String getWellComeTextColor() {
        return wellComeTextColor;
    }

    public String getOnlineClientsTextColor() {
        return onlineClientsTextColor;
    }

    public int getOnlineClientsXPosition() {
        return onlineClientsXPosition;
    }

    public int getOnlineClientsYPosition() {
        return onlineClientsYPosition;
    }

    public int getOnlineClientsTextSize() {
        return onlineClientsTextSize;
    }

    public int getWellcomeXPosition() {
        return wellcomeXPosition;
    }

    public int getWellcomeYPosition() {
        return wellcomeYPosition;
    }

    public int getWellComeTextSize() {
        return wellComeTextSize;
    }

    public String getFalseClientName() {
        return falseClientName;
    }

    public int getLocalPort() {
        return localPort;
    }
}

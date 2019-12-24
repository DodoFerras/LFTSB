package com.goelzertec.lftsb;

public class UIPacketPost {
    private String password, action, selector;
    private String clientName, timezone, onlineClients, contentd;

    public String getAction() {
        return action;
    }

    public String getSelector() {
        return selector;
    }

    public String getClientName() {
        return clientName;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getOnlineClients() {
        return onlineClients;
    }

    public String getContentd() {
        return contentd;
    }

    public boolean validate() {
        /*
            VALIDAR ESSA MERDA URGENTEMENTE
         */
        String[] contents = {password, action, selector, clientName, timezone, onlineClients, timezone};
        for (String s: contents) {
            if (s == null || s.isEmpty())
                return false;
        }
        if (!password.equals("senha"))
            return false;
        return true;
    }

    public boolean isPrimary() {
        return this.getSelector().equalsIgnoreCase("main");
    }
}
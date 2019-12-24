package com.goelzertec.lftsb;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServerInfo;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LFTSB {
    public static LFTSB instance;
    private static final int uiPort = 5050;
    private HttpServer httpServer;
    private TS3Query query;
    private TS3Api api;
    private ArrayList<TSClientInfo> onlineClients;
    private int maxClients, clientCounter = 0;
    private ConfigObj cfg;
    public LFTSB() {
        File cfgFile = new File("config.json");
        if (cfgFile.exists()) {
            try {
                FileReader fileReader = new FileReader(cfgFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                Gson gson = new Gson();
                this.cfg = gson.fromJson(bufferedReader, ConfigObj.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Tu fez alguma merda na config seu inutil");
                System.exit(0);
            }
        } else {
            System.out.println("Cade a merda da config seu resto de aborto do caralho");
            System.exit(0);
        }
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(cfg.getLocalPort()), 0);
            HttpContext context = this.httpServer.createContext("/lftsb/");
            context.setHandler(this::httpHandler);
            this.httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.onlineClients = new ArrayList();

        final TS3Config config = new TS3Config();
        config.setHost(cfg.getServerHost());
        config.setQueryPort(cfg.getQueryPort());
        query = new TS3Query(config);
        query.connect();

        if (!query.isConnected()) {
            System.out.println("Nao conectado");
            System.exit(0);
        }

        RandomString gen = new RandomString(12, ThreadLocalRandom.current());
        api = query.getApi();
        api.login(cfg.getQueryUsername(), cfg.getQueryPassword());
        api.selectVirtualServerById(1);
        api.setNickname("Banner bot: " +  gen.nextString());

        if (!query.isConnected()) {
            System.out.println("Nao conectado");
            System.exit(0);
        }
        this.refresh();
    }

    private void refresh() {
        List<Client> clients = api.getClients();
        if (!clients.isEmpty())
            onlineClients.clear();
        for (Client client: clients) {
            TSClientInfo clientInfo = new TSClientInfo(client.getNickname(), client.getIp(), client.getUniqueIdentifier());
            onlineClients.add(clientInfo);
        }

        VirtualServerInfo serverInfo = api.getServerInfo();
        if (this.maxClients == 0)
            this.maxClients = serverInfo.getMaxClients();
        this.clientCounter = serverInfo.getClientsOnline();
    }

    private TSClientInfo getUserByIP(String ip) {
        if (!onlineClients.isEmpty()) {
            for (TSClientInfo clientInfo : onlineClients) {
                if (clientInfo.ip.equalsIgnoreCase(ip))
                    return clientInfo;
            }
        }
        return null;
    }

    private void httpHandler(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        boolean isGetMethod = exchange.getRequestMethod().equalsIgnoreCase("GET");
        if (isGetMethod) {
            String reqIp = exchange.getRemoteAddress().getHostString();
            System.out.println(reqIp);

            this.refresh();

            TSClientInfo a = getUserByIP(reqIp);
            if (a != null) {
                sendImage(a, exchange);
            } else {
                if (!cfg.getFalseClientName().equalsIgnoreCase("excluded"))
                    sendImage(new TSClientInfo(cfg.getFalseClientName(), "0000", "00"), exchange);
            }
        }
    }

    private void sendHttpResponse(String response, HttpExchange exchange) throws IOException {
        String httpRes = (response != null && !response.isEmpty() ? response : "fail") ;
        exchange.sendResponseHeaders(200, httpRes.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(httpRes.getBytes());
        os.close();
    }

    private void sendImage(TSClientInfo tsClientInfo, HttpExchange exchange) throws IOException {
        BufferedImage image = ImageIO.read(new File(cfg.getBannerFile()));
        Graphics g = image.getGraphics();

        //Boas vindas
        g.setFont(g.getFont().deriveFont((float)cfg.getWellComeTextSize()));
        g.setColor(Color.decode(cfg.getWellComeTextColor()));

        String msg = cfg.getWellcomeMessage() + tsClientInfo.name;
        System.out.println(msg.length());

        g.drawString(msg, cfg.getWellcomeXPosition(), cfg.getWellcomeYPosition());

        //Clients online
        g.setFont(g.getFont().deriveFont((float)cfg.getOnlineClientsTextSize()));
        g.setColor(Color.RED);
        g.setColor(Color.decode(cfg.getOnlineClientsTextColor()));
        g.drawString(this.clientCounter + "/" + this.maxClients , cfg.getOnlineClientsXPosition(), cfg.getOnlineClientsYPosition());

        //Finaliza a edição
        g.dispose();

        File temp = File.createTempFile("../final", ".png");
        ImageIO.write(image, "png", temp);
        exchange.getResponseHeaders().set("Content-Type", "image/png; charset=UTF-8");

        //File file = new File("../test.png");
        exchange.sendResponseHeaders(200, temp.length());
        OutputStream outputStream = exchange.getResponseBody();
        Files.copy(temp.toPath(), outputStream);
        outputStream.close();
    }

    public static void main(String[] args) {
        instance = new LFTSB();
    }

    private class TSClientInfo {
        private String name, ip, uid;
        public TSClientInfo(String name, String ip, String uid) {
            this.ip = ip;
            this.name = name;
            this.uid = uid;
        }
    }
}

/*
 * Copyright Evan Summers
 * 
 */
package crocserver.gtalk;

import bizstat.server.*;
import bizstat.entity.Contact;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import vellum.config.PropertiesStringMap;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class GtalkConnection implements MessageListener {
    Logr logger = LogrFactory.getLogger(GtalkConnection.class);
    PropertiesStringMap properties;
        
    String username;
    String password;
    
    XMPPConnection connection;    
    
    public GtalkConnection(PropertiesStringMap properties) {
        username = properties.getString("gtalkUsername", System.getProperty("gtalk.username"));
        password = properties.getString("gtalkPassword", System.getProperty("gtalk.password"));
        logger.info("gtalkUsername", username);
    }

    public void open() {
        ConnectionConfiguration connectionConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        connectionConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
        connection = new XMPPConnection(connectionConfig);
        try {
            connection.connect();
            connection.login(username, password);
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
        } catch (Exception e) {
            logger.warn(e, null);
            e.printStackTrace(System.err);
        }  
    }

    public void sendMessage(String im, String message) throws Exception {
        logger.info("sendMessage", im);
        ChatManager chatManager = connection.getChatManager();
        Chat chat = chatManager.createChat(im, this);
        Message msg = new Message(im, Message.Type.chat);
        msg.setBody(message);
        chat.sendMessage(msg);
    }
    
    @Override
    public void processMessage(Chat chat, Message message) {
        logger.info("processMesssage", message.getClass(), message.getType(), message.getError());
        if (message.getType().equals(Message.Type.chat) && message.getBody() != null) {
            logger.info(message.getBody());
            try {
                Message reply = new Message(chat.getParticipant(), Message.Type.chat);
                reply.setBody("ack: " + message.getBody());
                chat.sendMessage(reply);
            } catch (XMPPException e) {
                logger.warn(e, null);
            }
        } else {
            logger.info("processMesssage", message.getType());
        }
    }

    public void close() {
        connection.disconnect();
    }
}

package eu.siacs.conversations.xmpp.stanzas;

import android.util.Log;
import android.util.Pair;

import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.parser.AbstractParser;
import eu.siacs.conversations.xml.Element;
import eu.siacs.conversations.xml.LocalizedContent;
import eu.siacs.conversations.xml.Namespace;

public class MessagePacket extends AbstractAcknowledgeableStanza {
    public static final int TYPE_CHAT = 0;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_GROUPCHAT = 3;
    public static final int TYPE_ERROR = 4;
    public static final int TYPE_HEADLINE = 5;

    private final String TAG = "MessagePacket_TAG";

    public MessagePacket() {
        super("message");
    }

    public LocalizedContent getBody() {
        return findInternationalizedChildContentInDefaultNamespace("body");
    }

    public void setBody(String text) {
        this.children.remove(findChild("body"));
        Element body = new Element("body");
        body.setContent(text);
        this.children.add(0, body);
    }


    public void setForwardedElement(String text) {
        Element forwarded = new Element("forwarded", Namespace.FORWARD);
        forwarded.setContent(text);
        Log.d(TAG, "Forwarded Element Added");
        this.children.add(forwarded);
    }

    public void setRemoveElement(final Message message){
        this.children.clear();
        Element remove = new Element("remove", Namespace.REMOVE);
        remove.setAttribute("id", message.getParentMsgId());
        this.children.add(remove);
    }

    public void setReplyElement(final Message message) {
        Element reply = new Element("reply", Namespace.REPLY);
        reply.setAttribute("to", message.getContact().getJid());
        reply.setAttribute("id", message.getParentMsgId());

        Log.d(TAG, "MESSAGE PARENT ID " + message.getParentMsgId());


        this.children.add(reply);
    }

    public void setTranslationStatus(String status) {
        Log.d(TAG, "setTranslationStatus : " + status);
        Element translation = new Element("translation");
        translation.setContent(status);
        this.children.add(translation);
    }

    public void setAxolotlMessage(Element axolotlMessage) {
        this.children.remove(findChild("body"));
        this.children.add(0, axolotlMessage);
    }

    public void setType(int type) {
        switch (type) {
            case TYPE_CHAT:
                this.setAttribute("type", "chat");
                break;
            case TYPE_GROUPCHAT:
                this.setAttribute("type", "groupchat");
                break;
            case TYPE_NORMAL:
                break;
            case TYPE_ERROR:
                this.setAttribute("type", "error");
                break;
            default:
                this.setAttribute("type", "chat");
                break;
        }
    }

    public int getType() {
        String type = getAttribute("type");
        if (type == null) {
            return TYPE_NORMAL;
        } else if (type.equals("normal")) {
            return TYPE_NORMAL;
        } else if (type.equals("chat")) {
            return TYPE_CHAT;
        } else if (type.equals("groupchat")) {
            return TYPE_GROUPCHAT;
        } else if (type.equals("error")) {
            return TYPE_ERROR;
        } else if (type.equals("headline")) {
            return TYPE_HEADLINE;
        } else {
            return TYPE_NORMAL;
        }
    }

    public Pair<MessagePacket, Long> getForwardedMessagePacket(String name, String namespace) {
//
//        Log.d(TAG, "getForwardedMessagePacket Called : " + name + "  namespace : " + namespace);
//

        Element wrapper = findChild(name, namespace);
        if (wrapper == null) {
            return null;
        }
        Element forwarded_1 = findChild("forwarded", "urn:xmpp:forward:0");

        if (forwarded_1 == null) {
            Log.d(TAG, "FORWARDED NOT FOUND..");
            return null;
        }
        MessagePacket packet = create(forwarded_1.findChild("message"));
        if (packet == null) {
            return null;
        }
        Long timestamp = AbstractParser.parseTimestamp(forwarded_1, null);
        return new Pair(packet, timestamp);
    }

    public static MessagePacket create(Element element) {
        if (element == null) {
            return null;
        }
        MessagePacket packet = new MessagePacket();
        packet.setAttributes(element.getAttributes());
        packet.setChildren(element.getChildren());
        return packet;
    }
}

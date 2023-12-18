package eu.siacs.conversations.entities;

import android.content.ContentValues;
import android.database.Cursor;

public class CallLog {
    public static final String TABLENAME = "calllogs";
    public static final String CONTACT_NAME = "contactname";
    public static final String CONTACT_JID = "contactjid";
    public static final String DURATION = "duration";
    public static final String STATUS = "status";
    public static final String TIME = "time";
    public static final String SESSION_ID = "session_id";

    private String contactName;
    private String duration;
    private String status;
    private String time;

    private String jid;

    private String sessionId;


    public CallLog(String sessionId, String contactName, String jid, String duration, String time, String status) {
        this.sessionId = sessionId;
        this.contactName = contactName;
        this.jid = jid;
        this.duration = duration;
        this.time = time;
        this.status = status;
    }

    public static CallLog fromCursor(final Cursor cursor) {
        return new CallLog(
                cursor.getString(cursor.getColumnIndex(SESSION_ID)),
                cursor.getString(cursor.getColumnIndex(CONTACT_NAME)),
                cursor.getString(cursor.getColumnIndex(CONTACT_JID)),
                cursor.getString(cursor.getColumnIndex(DURATION)),
                cursor.getString(cursor.getColumnIndex(TIME)),
                cursor.getString(cursor.getColumnIndex(STATUS)));
    }


    public ContentValues getContentValues() {

        final ContentValues values = new ContentValues();
        values.put(SESSION_ID, sessionId);
        values.put(CONTACT_NAME, contactName);
        values.put(CONTACT_JID, jid);
        values.put(DURATION, duration);
        values.put(TIME, time);
        values.put(STATUS, status);
        return values;
    }


    public String getContactName() {
        return contactName;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}

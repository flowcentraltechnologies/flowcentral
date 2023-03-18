package com.flowcentraltech.flowcentral.integration.endpoint.data;

public class EventMessage {

    private String text;

    private String fileName;

    private byte[] file;

    public EventMessage(String text, String fileName, byte[] file) {
        this.text = text;
        this.fileName = fileName;
        this.file = file;
    }

    public EventMessage(String fileName, byte[] file) {
        this.fileName = fileName;
        this.file = file;
    }

    public EventMessage(byte[] file) {
        this.file = file;
    }

    public EventMessage(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public String getText() {
        return text;
    }

    public byte[] getFile() {
        return file;
    }
    
    public boolean isText() {
        return text != null;
    }
    
    public boolean isFile() {
        return file != null;
    }
}
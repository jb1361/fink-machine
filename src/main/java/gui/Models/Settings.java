package gui.Models;

import javafx.scene.paint.Paint;

public class Settings {
    double nodeRadius;
    int snapPadding;
    int targetPadding;
    boolean convertSpecialCharacters;
    Paint stateColor;
    Paint acceptStateColor;
    Paint linkColor;
    Paint selfLinkColor;
    Paint textColor;

    public Settings() {
        // Default settings
        nodeRadius = 30;
        snapPadding = 6;
        targetPadding = 6;
        convertSpecialCharacters = true;
        stateColor = Paint.valueOf("#000000");
        acceptStateColor = Paint.valueOf("#000000");
        linkColor = Paint.valueOf("#000000");
        selfLinkColor = Paint.valueOf("#000000");
        textColor = Paint.valueOf("#000000");
    }

    public double getNodeRadius() {
        return nodeRadius;
    }

    public void setNodeRadius(double nodeRadius) {
        this.nodeRadius = nodeRadius;
    }

    public int getSnapPadding() {
        return snapPadding;
    }

    public void setSnapPadding(int snapPadding) {
        this.snapPadding = snapPadding;
    }

    public int getTargetPadding() {
        return targetPadding;
    }

    public void setTargetPadding(int targetPadding) {
        this.targetPadding = targetPadding;
    }

    public boolean isConvertSpecialCharacters() {
        return convertSpecialCharacters;
    }

    public void setConvertSpecialCharacters(boolean convertSpecialCharacters) {
        this.convertSpecialCharacters = convertSpecialCharacters;
    }

    public Paint getStateColor() {
        return stateColor;
    }

    public void setStateColor(Paint stateColor) {
        this.stateColor = stateColor;
    }

    public Paint getAcceptStateColor() {
        return acceptStateColor;
    }

    public void setAcceptStateColor(Paint acceptStateColor) {
        this.acceptStateColor = acceptStateColor;
    }

    public Paint getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(Paint linkColor) {
        this.linkColor = linkColor;
    }

    public Paint getSelfLinkColor() {
        return selfLinkColor;
    }

    public void setSelfLinkColor(Paint selfLinkColor) {
        this.selfLinkColor = selfLinkColor;
    }

    public Paint getTextColor() {
        return textColor;
    }

    public void setTextColor(Paint textColor) {
        this.textColor = textColor;
    }


}

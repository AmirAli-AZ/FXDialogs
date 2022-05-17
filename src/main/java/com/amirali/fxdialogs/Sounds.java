package com.amirali.fxdialogs;

public enum Sounds {

    Achievement("achievement-message-tone.mp3"),
    Beep("beep-472.mp3"),
    ChimesGlassy("chimes-glassy-456.mp3"),
    Clearly("clearly-602.mp3"),
    Elegant("elegant-notification-sound.mp3"),
    Hangover("hangover-sound.mp3"),
    JustLikeMagic("just-like-magic-506.mp3"),
    LongExpected("long-expected-548.mp3"),
    Magic("message-ringtone-magic.mp3"),
    NiceCut("nice-cut-514.mp3"),
    SwiftGesture("notification-tone-swift-gesture.mp3"),
    Pristine("pristine-609.mp3"),
    SoProud("so-proud-notification.mp3"),
    SystemFault("system-fault-518.mp3"),
    Upset("upset-sound-tone.mp3"),
    Deduction("deduction-588.mp3"),
    DoneForYou("done-for-you-612.mp3"),
    FingerLicking("fingerlicking-message-tone.mp3"),
    Goes("goes-without-saying-608.mp3"),
    IllMakeItPossible("ill-make-it-possible-notification.mp3"),
    PointBlank("point-blank-589.mp3"),
    Succeeded("succeeded-message-tone.mp3"),
    When("when-604.mp3");

    private final String path;

    public String getPath() {
        return path;
    }

    Sounds(String path) {
        this.path = path;
    }
}

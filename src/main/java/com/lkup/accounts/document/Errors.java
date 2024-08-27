package com.lkup.accounts.document;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Errors {

    private Object fatal;
    private Object network;
    private Object componentNotLoad;
    private Object userMsgNotSentError;
    private Object fullChatfailure;
    private Object chatAgain;
    private String connectionClosed;
    private String tobiError;

}

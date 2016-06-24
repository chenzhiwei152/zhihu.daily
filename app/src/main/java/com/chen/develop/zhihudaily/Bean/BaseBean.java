package com.chen.develop.zhihudaily.Bean;

import java.io.Serializable;

public class BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;

public ResponseHeader responseHeader;

public class ResponseHeader {
    public String errorCode = "";
    public String message = "";
}
}

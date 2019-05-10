package cn.shaoqunliu.c.hub.mgr.exception;

/*
 * we can check if the exception is instance of this abstract exception class
 * to check if the exception thrown at our business logic instead other part
 * of this application, such as Spring framework and etc.
 */
public abstract class AbstractMgrException extends Exception {

    public AbstractMgrException(String message) {
        super(message);
    }
}

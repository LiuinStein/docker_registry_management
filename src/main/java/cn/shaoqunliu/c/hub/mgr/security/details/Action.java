package cn.shaoqunliu.c.hub.mgr.security.details;

public enum Action {
    NULL(0, "null"),
    PULL(1, "pull"),
    PUSH(2, "push"),
    BOTH(3, "pull,push");

    private int val;
    private String str;

    Action(int v, String s) {
        val = v;
        str = s;
    }

    public int value() {
        return val;
    }

    public static Action of(int v) {
        if (v < 0 || v > 3) {
            throw new IllegalArgumentException("Bad action");
        }
        Action[] mapper = {Action.NULL, Action.PULL, Action.PUSH, Action.BOTH};
        return mapper[v];
    }

    public static Action of(String str) {
        switch (str.toLowerCase()) {
            case "":
            case "null":
                return Action.NULL;
            case "pull":
                return Action.PULL;
            case "push":
                return Action.PUSH;
            case "pull,push":
                return Action.BOTH;
            default:
                throw new IllegalArgumentException("Bad action");
        }
    }

    @Override
    public String toString() {
        return str;
    }
}
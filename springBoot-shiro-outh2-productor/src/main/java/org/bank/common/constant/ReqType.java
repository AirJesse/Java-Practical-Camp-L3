package org.bank.common.constant;


/**
 * 系统请求类型
 */
public enum ReqType {
    OUTHTOKEN("OuthToken"),  SYSTEM("System"),OUTHCODE("outhCode");

    private String type;

    private ReqType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}

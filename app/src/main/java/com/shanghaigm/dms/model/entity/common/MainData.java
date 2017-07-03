package com.shanghaigm.dms.model.entity.common;

/**
 * Created by Tom on 2017/5/15.
 */

public class MainData {
    
    //账户
    private String account;
    //密码
    private String password;
    //岗位
    private String jobCode;
    //职位判别
    private String roleCode;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }
}

package com.wl4g.devops.common.bean.umc.temple;

import java.io.Serializable;

/**
 * @author vjay
 * @date 2019-06-10 14:30:00
 */
public class BaseTemple implements Serializable {

    private static final long serialVersionUID = 381411777614066880L;

    private String id;

    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.xiex.entity.xiehuan;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiex
 * @since 2020-08-02
 */
@Data
@Accessors(chain = true)
public class Notice  {

    private static final long serialVersionUID = 1L;

    private String contents;

    private String level;

    private LocalDateTime date;

    private String status;

    private LocalDateTime statusdate;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStatusdate() {
        return statusdate;
    }

    public void setStatusdate(LocalDateTime statusdate) {
        this.statusdate = statusdate;
    }
}

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
public class Register {

    private static final long serialVersionUID = 1L;

    private String studentid;

    private String month;

    private LocalDateTime statusdate;


}

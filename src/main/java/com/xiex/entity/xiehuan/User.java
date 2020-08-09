package com.xiex.entity.xiehuan;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class User  {

    private static final long serialVersionUID = 1L;

    @TableField("studentId")
    private String studentId;

    private String name;

    private String password;

    private Integer isadmin;

    @TableField("TimesBorrowed")
    private Integer TimesBorrowed;

    @TableField("NumBorrowed")
    private Integer NumBorrowed;

    @TableField("teacherId")
    private String teacherId;

    @TableField("classId")
    private String classId;

    private String phone;

    @TableField("familyName")
    private String familyName;

    @TableField("familyPhone")
    private String familyPhone;

    private String address;


}

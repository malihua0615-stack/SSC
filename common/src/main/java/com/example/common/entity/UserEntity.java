package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码（BCrypt加密） */
    private String password;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 头像URL */
    private String avatar;

    /** 昵称 */
    private String nickname;

    /** 真实姓名 */
    private String realName;

    /** 性别：0未知 1男 2女 */
    private Integer gender;

    /** 生日 */
    private LocalDate birthday;

    /** 状态：0禁用 1正常 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 逻辑删除：0未删 1已删 */
    private Integer deleted;
}
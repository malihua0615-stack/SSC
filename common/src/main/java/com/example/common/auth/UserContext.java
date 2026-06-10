package com.example.common.auth;

import com.example.common.entity.UserEntity;

public class UserContext {

    private static final ThreadLocal<UserEntity>  userEntityThreadLocal = new ThreadLocal<>();
    public  static UserEntity getUserEntity(){
        return userEntityThreadLocal.get();
    }
    public static void setUserEntity(UserEntity userEntity){
        userEntityThreadLocal.set(userEntity);
    }
    public static void removeUserEntity(){
        userEntityThreadLocal.remove();
    }
}

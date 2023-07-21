package com.zhanxin.tbiops.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * @author by fengww
 * @Classname Foo
 * @Description
 * @Date 2023/7/21 23:40
 */
@Entity(name = "foo")
@Data
public class Foo {

    @javax.persistence.Id
    @GeneratedValue(generator = "JDBC", strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;


}

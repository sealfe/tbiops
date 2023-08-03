package com.zhanxin.tbiops.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "error_message_convert")
public class ErrorMessageConvert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "error_message", columnDefinition = "varchar(4000) default ''")
    private String errorMessage;

    @Column(name = "cn_message", columnDefinition = "varchar(2000) default ''")
    private String cnMessage;

    // default value is current time
    @Column(name = "create_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;


    @Column(name = "method_url", columnDefinition = "varchar(255) default ''")
    private String methodUrl;

    public String key() {
        return errorMessage + "_" + methodUrl;
    }
}
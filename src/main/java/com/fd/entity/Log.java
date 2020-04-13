package com.fd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "yx_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log implements Serializable {

    @Id
    private String id;

    private String adminname;

    private Date date;

    private String operation;

    private String status;


}
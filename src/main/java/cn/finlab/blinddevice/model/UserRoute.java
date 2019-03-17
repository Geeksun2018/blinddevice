package cn.finlab.blinddevice.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRoute {
    private Integer id;

    private Date startTime;

    private Date endTime;

    private Integer eid;

}

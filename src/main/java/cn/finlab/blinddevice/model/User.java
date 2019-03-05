package cn.finlab.blinddevice.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Getter
@Setter
@AllArgsConstructor
public class User {
    @NotNull
    @Length(max = 11, min = 11, message = "手机号的长度必须是11位.")
    private String username;

    @NotNull
    @Length(max = 32,min = 32,message = "密码不合法")
    private String password;

    private String salt;

    private Integer id;

    public User(){

    }

    @Override
    public String toString(){
        String ret=null;
        ObjectMapper mapper=new ObjectMapper();
        try {
            ret=mapper.writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}

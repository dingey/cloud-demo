package com.d.goods.dto;

import com.d.goods.config.FormModelResolver;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @auther ding
 * @date 2019/1/23 17:01
 * @since 0.0.1
 */
@Data
public class GoodsDTO {
    Integer id;
    String name;
    Date start;
    Date end;
    Integer from;
    @FormModelResolver.LargeThan(name = "from", largeValue = 2L, message = "终点必须大于开始位置")
    Integer to;
}

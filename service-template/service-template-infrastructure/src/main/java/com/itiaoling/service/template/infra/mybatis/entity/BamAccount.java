package service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 样例
 *
 * @author charles
 * @since 2023-08-21
 */
@Data
@TableName("t_bam_accout")
public class BamAccount {

    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目编码
     */
    private String projectCode;

}

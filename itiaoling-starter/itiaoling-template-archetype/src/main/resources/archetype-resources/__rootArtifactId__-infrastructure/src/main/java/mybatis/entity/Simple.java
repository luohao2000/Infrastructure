#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.mybatis.entity;

// import com.baomidou.mybatisplus.annotation.IdType;
// import com.baomidou.mybatisplus.annotation.TableId;
// import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 样例
 *
 * @author charles
 * @since 2023-08-21
 */
@Data
// @TableName("t_simple")
public class Simple {

    /**
     * 自增主键ID
     */
    // @TableId(value = "id", type = IdType.AUTO)
    private Long id;

}

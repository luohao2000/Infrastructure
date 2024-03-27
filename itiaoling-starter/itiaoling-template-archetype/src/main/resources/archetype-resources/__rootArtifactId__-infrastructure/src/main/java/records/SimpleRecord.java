#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.records;

/**
 * @author charles
 * @since 2023/12/14
 */
public record SimpleRecord(Long id, String name, String email) {
}

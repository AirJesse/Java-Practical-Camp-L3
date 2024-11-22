package org.bank.domain.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.resp
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class PageVO<T>  {
	/**
	 * serial_number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总记录数
	 */
	private Long totalRows;
	/**
	 * 总页数
	 */
	private Integer totalPages;
	/**
	 * 当前第几页
	 */
	private Integer pageNum;
	/**
	 * 每页记录数
	 */
	private Integer pageSize;
	/**
	 * 当前页记录数
	 */
	private Integer curPageSize;
	/**
	 * 数据列表
	 */
	private List<T> list;

}

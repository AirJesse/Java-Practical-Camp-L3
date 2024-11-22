package org.bank.common.util;

import com.github.pagehelper.Page;
import org.bank.domain.vo.resp.PageVO;

import java.util.List;


/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.utils
 * @Author: lizongle
 * @Description:分页工具类
 * @Version: 1.0
 */
public class PageUtils {
	private PageUtils() {}
    public static <T> PageVO<T> getPageVO(List<T> list) {
        PageVO<T> result = new PageVO<>();
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            result.setTotalRows(page.getTotal());
            result.setTotalPages(page.getPages());
            result.setPageNum(page.getPageNum());
            result.setPageSize(page.getPageSize());
            result.setCurPageSize(page.size());
            result.setList(page.getResult());
        }
        return result;
    }
}

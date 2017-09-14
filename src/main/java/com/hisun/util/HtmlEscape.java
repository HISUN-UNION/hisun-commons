package com.hisun.util;

import org.springframework.web.util.HtmlUtils;

/**
 * <p>类名称:HtmlEscape</p>
 * <p>类描述:</p>
 * <p>公司:湖南海数互联信息技术有限公司</p>
 *
 * @创建者:init
 * @创建人:16/7/1上午9:38
 * @创建人联系方式:init@hn-hisun.com
 */
public class HtmlEscape {

    public static String htmlEscape(String source){
        return HtmlUtils.htmlEscape(source);
    }
}

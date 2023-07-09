package com.yilan.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@Data
@TableName("blog")
public class BlogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 鏍囬?锛岄暱搴︽渶灏忎负2锛屾渶澶т负50
	 */
	private String title;
	/**
	 * 鎽樿?锛岄暱搴︽渶灏忎负2锛屾渶澶т负300
	 */
	private String summary;
	/**
	 * 鍐呭?锛岄暱搴︽渶灏忎负2
	 */
	private String content;
	/**
	 * 灏唌d杞?垚html鐨勫唴瀹癸紝闀垮害鏈?皬涓?
	 */
	private String htmlContent;
	/**
	 * 澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯
	 */
	private Integer userId;
	/**
	 * 鏂囩珷鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 鏂囩珷鏈?悗涓??淇?敼鏃堕棿锛屽垵濮嬪?涓虹┖
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 璁块棶閲忋?闃呰?閲忥紝鍒濆?鍊间负0
	 */
	private Integer readSize;
	/**
	 * 璇勮?閲忥紝鍒濆?鍊间负0
	 */
	private Integer commentSize;
	/**
	 * 鐐硅禐閲忥紝鍒濆?鍊间负0
	 */
	private Integer voteSize;
	/**
	 * 澶栭敭瀵瑰簲鍒嗙被琛?紝涓??涓?叧绯
	 */
	private Integer catalogId;
	/**
	 * 鐘舵?锛?-鍙戝竷 1-鑽夌? 2-澶勪簬琚?妇鎶ョ殑瀹℃牳鐘舵? 3-纭??涓鸿繚瑙勬枃绔
	 */
	private String status;
	/**
	 * 鏉冮檺锛?-鍏?紑 1-濂藉弸鍙?? 2-绉佸瘑
	 */
	private String authority;

}

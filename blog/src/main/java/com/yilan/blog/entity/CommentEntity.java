package com.yilan.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * 
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@Data
@TableName("comment")
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 璇勮?鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负200
	 */
	private String content;
	/**
	 * 澶栭敭瀵瑰簲鐢ㄦ埛琛?紝涓??涓
	 */
	private Integer userId;
	/**
	 * 璇勮?鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撹嚜鍔ㄥ垱寤
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 鐖惰瘎璁篿d锛堝洖澶嶈瘎璁虹殑鏃跺?鐢ㄥ埌锛夛紝褰揂鏁版嵁鐨刾arent_id绛変簬B鏁版嵁鐨刬d鏃讹紝鍒橞鏁版嵁鏄疉鏁版嵁鐨勭埗璇勮?
	 */
	private Integer parentId;
	/**
	 * 澶栭敭瀵瑰簲blog琛?紝澶氬?涓??搴
	 */
	private Integer blogId;

	@TableField(exist = false)
	private List<CommentEntity> children;

}

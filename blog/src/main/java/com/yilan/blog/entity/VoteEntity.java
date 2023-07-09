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
@TableName("vote")
public class VoteEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯
	 */
	private Integer userId;
	/**
	 * 鍒涘缓鏃堕棿锛岄粯璁ょ敱鏁版嵁搴撳垱寤
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 澶栭敭瀵瑰簲blog琛?紝缁欐煇鍗氭枃鐐硅禐
	 */
	private Integer blogId;

}

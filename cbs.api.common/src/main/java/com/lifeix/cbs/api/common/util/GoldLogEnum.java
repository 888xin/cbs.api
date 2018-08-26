package com.lifeix.cbs.api.common.util;

/**
 * 日志枚举
 * @author jacky
 *
 */
public enum GoldLogEnum{
	
	RECHARGE(1, "充值：{#thing}"),

	FREEZE(2, "冻结：{#thing}"),

	PAYMENT(3, "支付：{#thing}"),
	
	PRISE(4,"奖励：{#thing}"),
	
	SYSTEM(5, "系统：{#thing}"),

	EARN(8, "赚取：{#thing}"),

	OTHER(9, "其他：{#thing}");

	private final int type;
	
	private final String template;
	
	private GoldLogEnum(int type, String template) {
		this.type = type;
		this.template = template;
	}

	public int getType() {
		return type;
	}

	public String getTemplate() {
		return template;
	}
	
}
package com.open.capacity.pojo;

 

import java.io.Serializable;
import java.math.BigDecimal;
 
public class Product  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	 
	private String id;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private String productFrom;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductFrom() {
        return productFrom;
    }

    public void setProductFrom(String productFrom) {
        this.productFrom = productFrom == null ? null : productFrom.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	 
}
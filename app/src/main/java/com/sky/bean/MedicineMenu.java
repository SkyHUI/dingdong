package com.sky.bean;

/**
 * 项目名称：com.sky.bean
 * 类描述：医药菜单
 * 创建人：Sky
 * 创建时间：2017/3/29 11:42
 */
public class MedicineMenu {

    /**
     * 疾病库
     */
    public static final int TYPE_DESEASE = 1;

    /**
     *医药库
     */
    public static final int TYPE_DRUG = 2;

    /**
     * 检查结果
     */
    public static final int TYPE_INSPECTION = 3;

    private int id;

    private String name;

    private int type;

    public MedicineMenu() {
        super();
    }

    public MedicineMenu(int id, String name, int type) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MedicineMenu [id=" + id + ", name=" + name + ", type=" + type + "]";
    }


}


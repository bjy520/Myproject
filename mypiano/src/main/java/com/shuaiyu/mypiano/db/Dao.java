package com.shuaiyu.mypiano.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

///**
// *
// * 因为导入的是已经创建好表的数据库，
// * 所以要在类前注解:@Entity(nameInDb = "productinfo",createInDb = false),
// * 其中nameInDb = "tablename"是声明表名，不作此声明greenDAO默认操作的是库名的同名的表;
// * 另一句createInDb = false是声明不再创建这个表，
// * 如果不这样声明greenDAO就会在UserDao文件中加入createTable方法，
// * 继而创建一个名叫tablename的同名表，然后就会出现table already exists的错误。
// */
//@Entity(nameInDb = "Zdftyzdrxdxl",createInDb = false)
//public class InspecBean {
//
//    @Property(nameInDb = "id")
//    @Id(autoincrement = true)
//    private Long id;
//    @Property(nameInDb = "Sid")
//    private String mac;
//    @Property(nameInDb = "Dict_xlname_zdft")
//    private String name;
//    @Property(nameInDb = "TotleXxNum")
//    private Integer num;
//
//}
@Entity
public class Dao {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "keysounds")
    private String soundPath;//昵称
    private int tag;
    @Generated(hash = 1256985550)
    public Dao(Long id, String soundPath, int tag) {
        this.id = id;
        this.soundPath = soundPath;
        this.tag = tag;
    }
    @Generated(hash = 384647155)
    public Dao() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSoundPath() {
        return this.soundPath;
    }
    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }
    public int getTag() {
        return this.tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
}
